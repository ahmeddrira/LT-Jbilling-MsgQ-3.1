/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/

package com.sapienter.jbilling.server.process;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.BillingProcessRunEntityLocal;
import com.sapienter.jbilling.interfaces.BillingProcessRunEntityLocalHome;
import com.sapienter.jbilling.interfaces.BillingProcessRunTotalEntityLocal;
import com.sapienter.jbilling.interfaces.BillingProcessRunTotalEntityLocalHome;
import com.sapienter.jbilling.interfaces.BillingProcessRunTotalPMEntityLocal;
import com.sapienter.jbilling.interfaces.BillingProcessRunTotalPMEntityLocalHome;
import com.sapienter.jbilling.interfaces.PaymentMethodEntityLocalHome;
import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.util.EventLogger;

public class BillingProcessRunBL {
    private JNDILookup EJBFactory = null;
    private BillingProcessRunEntityLocalHome billingProcessRunHome = null;
    private BillingProcessRunTotalEntityLocalHome billingProcessRunTotalHome = null;
    private BillingProcessRunTotalPMEntityLocalHome billingProcessRunTotalPMHome = null;
    private BillingProcessRunEntityLocal billingProcessRun = null;
    private Logger log = null;
    private EventLogger eLogger = null;

    public class DateComparator implements Comparator {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {
            BillingProcessRunEntityLocal a = (BillingProcessRunEntityLocal) o1;
            BillingProcessRunEntityLocal b = (BillingProcessRunEntityLocal) o2;
        
            if (a.getStarted().after(b.getStarted())) {
                return 1;
            } else if (a.getStarted().before(b.getStarted())) {
                return -1;
            }
            return 0;
        }

    }
    
    public BillingProcessRunBL(Integer billingProcessRunId) 
            throws NamingException, FinderException {
        init();
        set(billingProcessRunId);
    }
    
    public BillingProcessRunBL() throws NamingException {
        init();
    }
    
    public BillingProcessRunBL(BillingProcessRunEntityLocal run) 
            throws NamingException {
        init();
        billingProcessRun = run;
    }
    
    private void init() throws NamingException {
        log = Logger.getLogger(BillingProcessRunBL.class);     
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        billingProcessRunHome = (BillingProcessRunEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                BillingProcessRunEntityLocalHome.class,
                BillingProcessRunEntityLocalHome.JNDI_NAME);

        billingProcessRunTotalHome = (BillingProcessRunTotalEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                BillingProcessRunTotalEntityLocalHome.class,
                BillingProcessRunTotalEntityLocalHome.JNDI_NAME);

        billingProcessRunTotalPMHome = (BillingProcessRunTotalPMEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                BillingProcessRunTotalPMEntityLocalHome.class,
                BillingProcessRunTotalPMEntityLocalHome.JNDI_NAME);

    }

    public BillingProcessRunEntityLocal getEntity() {
        return billingProcessRun;
    }
    
    public void set(Integer id) throws FinderException {
        billingProcessRun = billingProcessRunHome.findByPrimaryKey(id);
    }

    /**
     * Finds the run based on the process id. Assumes that there is
     * only one run associated with the process
     * @param id
     */
    public void setProcess(Integer id) 
            throws SessionInternalError, FinderException, NamingException {
        // get the only run that should be here
        BillingProcessBL process = new BillingProcessBL(id);
        Collection runs = process.getEntity().getRuns();
        if (runs.size() != 1) {
            throw new SessionInternalError("Process " + id +
                    " should have 1 run. It has " + runs.size());
        }
        billingProcessRun = (BillingProcessRunEntityLocal) runs.iterator().next();
    }
    
    public Integer create(Date runDate) 
            throws CreateException {
        if (runDate == null) {
            throw new CreateException("run date can't ge null");
        }

        billingProcessRun = billingProcessRunHome.create(runDate);
        return billingProcessRun.getId();
    }
    
    /**
     * Adds the payment total to the run totals
     * @param currencyId
     * @param methodId
     * @param total
     * @param ok
     * @throws CreateException
     */
    public void updateNewPayment(Integer currencyId, Integer methodId, 
            Float total, boolean ok) 
            throws CreateException, NamingException, FinderException {
        // update the payments total
        BillingProcessRunTotalEntityLocal totalRow = findOrCreateTotal(currencyId);
        
        BigDecimal tmpValue = null;
        if (ok) {
        	tmpValue = new BigDecimal(totalRow.getTotalPaid().toString());
        	tmpValue = tmpValue.add(new BigDecimal(total.toString()));
            totalRow.setTotalPaid(new Float(tmpValue.toString()));
            // the payment is good, update the method total as well
            BillingProcessRunTotalPMEntityLocal pm = findOrCreateTotalPM(
                    methodId, totalRow);
            tmpValue = new BigDecimal(pm.getTotal().toString());
            tmpValue = tmpValue.add(new BigDecimal(total.toString()));
            pm.setTotal(new Float(tmpValue.toString()));
            PaymentMethodEntityLocalHome paymentMethodHome = 
                (PaymentMethodEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                PaymentMethodEntityLocalHome.class,
                PaymentMethodEntityLocalHome.JNDI_NAME);
            // link it to the payment method table
            pm.setPaymentMethod(paymentMethodHome.findByPrimaryKey(methodId));
            
        } else {
        	tmpValue = new BigDecimal(totalRow.getTotalNotPaid().toString());
        	tmpValue = tmpValue.add(new BigDecimal(total.toString()));
            totalRow.setTotalNotPaid(new Float(tmpValue.toString()));
        }
       
    }
    
    /**
     * Adds an invoice to the run totals
     */
    public void updateNewInvoice(Integer currencyId, Float invoiceTotal) 
            throws CreateException {
        // add one to the number of invoices generated
        Integer alreadyDone = billingProcessRun.getInvoiceGenerated();
        if (alreadyDone == null) { // first invoice
            billingProcessRun.setInvoiceGenerated(new Integer(1));
        } else {  // new invoice
            billingProcessRun.setInvoiceGenerated(new Integer(alreadyDone.
                    intValue() + 1));
        }
        
        // add the total to the total invoiced
        BillingProcessRunTotalEntityLocal totalRow = findOrCreateTotal(currencyId);
        BigDecimal tmpValue = new BigDecimal(totalRow.getTotalInvoiced().toString());
        tmpValue = tmpValue.add(new BigDecimal(invoiceTotal.toString()));
        totalRow.setTotalInvoiced(new Float( tmpValue.toString() ));
    }
    
    private BillingProcessRunTotalEntityLocal findOrCreateTotal(Integer currencyId) 
            throws CreateException {
        BillingProcessRunTotalEntityLocal ret = null;
        for (Iterator it = billingProcessRun.getTotals().iterator(); it.hasNext(); ) {
            BillingProcessRunTotalEntityLocal row = 
                (BillingProcessRunTotalEntityLocal) it.next();
            if (row.getCurrencyId().equals(currencyId)) {
                ret = row;
                break;
            }
        }
        
        if (ret == null) { // not present for this currency
            ret = billingProcessRunTotalHome.create(
                new Float(0), new Float(0), 
                new Float(0), currencyId);
            billingProcessRun.getTotals().add(ret);
        }
        return ret;
    }

    private BillingProcessRunTotalPMEntityLocal findOrCreateTotalPM(
            Integer methodId, BillingProcessRunTotalEntityLocal total) 
            throws CreateException {
        BillingProcessRunTotalPMEntityLocal ret = null;
        for (Iterator it = total.getTotalsPaymentMethod().iterator(); it
                .hasNext();) {
            BillingProcessRunTotalPMEntityLocal row = (BillingProcessRunTotalPMEntityLocal) it
                    .next();
            if (row.getPaymentMethod().getId().equals(methodId)) {
                ret = row;
                break;
            }
        }

        if (ret == null) { // not present for this currency
            ret = billingProcessRunTotalPMHome.create(new Float(0));
            // link it to the total row
            total.getTotalsPaymentMethod().add(ret);
        }
        return ret;
    }

    // called when the run is over, to update the dates only
    public void update(Date runDate) {
        billingProcessRun.setRunDate(runDate);
        billingProcessRun.setFinished(Calendar.getInstance().getTime());
    }
    
    
    
    public BillingProcessRunDTOEx getDTO(Integer language) 
            throws NamingException, FinderException {
        BillingProcessRunDTOEx dto = new BillingProcessRunDTOEx();
        
        dto.setId(billingProcessRun.getId());
        dto.setFinished(billingProcessRun.getFinished());
        dto.setInvoiceGenerated(billingProcessRun.getInvoiceGenerated());
        dto.setStarted(billingProcessRun.getStarted());
        dto.setRunDate(billingProcessRun.getRunDate());
        dto.setPaymentFinished(billingProcessRun.getPaymentFinished());
        // now the totals
        if (!billingProcessRun.getTotals().isEmpty()) {
            for (Iterator tIt = billingProcessRun.getTotals().iterator(); 
                    tIt.hasNext();) {
                BillingProcessRunTotalEntityLocal totalRow = 
                        (BillingProcessRunTotalEntityLocal) tIt.next();
                BillingProcessRunTotalDTOEx totalDto = getTotalDTO(totalRow,
                        language);
                dto.getTotals().add(totalDto);
            }
        }
 
        return dto;
    }
    
    public BillingProcessRunTotalDTOEx getTotalDTO(
            BillingProcessRunTotalEntityLocal row, Integer languageId) 
            throws NamingException, FinderException {
        BillingProcessRunTotalDTOEx retValue = 
                new BillingProcessRunTotalDTOEx();
        retValue.setCurrencyId(row.getCurrencyId());
        retValue.setId(row.getId());
        retValue.setTotalInvoiced(row.getTotalInvoiced());
        retValue.setTotalNotPaid(row.getTotalNotPaid());
        retValue.setTotalPaid(row.getTotalPaid());
        
        // now go over the totals by payment method
        Hashtable totals = new Hashtable();
        for (Iterator it = row.getTotalsPaymentMethod().iterator(); 
                it.hasNext();) {
            BillingProcessRunTotalPMEntityLocal pmTotal =
                    (BillingProcessRunTotalPMEntityLocal) it.next();
            totals.put(pmTotal.getPaymentMethod().getDescription(languageId),
                    pmTotal.getTotal());
                    
        }
        retValue.setPmTotals(totals);
        
        // add the currency name, it's handy on the client side
        CurrencyBL currency = new CurrencyBL(retValue.getCurrencyId());
        retValue.setCurrencyName(currency.getEntity().getDescription(
                languageId));
        
        return retValue;
    }
}
