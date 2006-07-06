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

/*
 * Created on 17-Apr-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.process;

import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.BillingProcessSession;
import com.sapienter.jbilling.interfaces.BillingProcessSessionHome;
import com.sapienter.jbilling.interfaces.InvoiceSession;
import com.sapienter.jbilling.interfaces.InvoiceSessionHome;
import com.sapienter.jbilling.interfaces.OrderSession;
import com.sapienter.jbilling.interfaces.OrderSessionHome;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.entity.BillingProcessConfigurationDTO;
import com.sapienter.jbilling.server.entity.BillingProcessDTO;
import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.entity.OrderDTO;
import com.sapienter.jbilling.server.entity.OrderProcessDTO;
import com.sapienter.jbilling.server.entity.PaymentDTO;
import com.sapienter.jbilling.server.invoice.InvoiceDTOEx;
import com.sapienter.jbilling.server.order.OrderDTOEx;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.util.Constants;

/**
 * Points to testOrders:
 * Orders :
 * - next billable day
 * - to_process 
 * - start/end of billing period
 * - invoice has (not) generated
 * - billing process relationship
 * - some amounts of the generated invoice
 * Invoices :
 * - if the invoice has been processed or no
 * - to_process
 * - delegated_invoice_id is updated
 * @author Emil
 */
public class BillingProcessTest extends TestCase {

    OrderSession remoteOrder = null;
    InvoiceSession remoteInvoice = null;
    BillingProcessSession remoteBillingProcess = null;
    UserSession remoteUser = null;
    GregorianCalendar cal;
    Date processDate = null;
    Integer entityId = null;
    Integer languageId = null;

    public BillingProcessTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        // once it run well ;) let's get the order interface
        OrderSessionHome orderHome =
            (OrderSessionHome) JNDILookup.getFactory(true).lookUpHome(
                OrderSessionHome.class,
                OrderSessionHome.JNDI_NAME);
        remoteOrder = orderHome.create();
            
        InvoiceSessionHome invoiceHome =
            (InvoiceSessionHome) JNDILookup.getFactory(true).lookUpHome(
                InvoiceSessionHome.class,
                InvoiceSessionHome.JNDI_NAME);
        remoteInvoice = invoiceHome.create();

        BillingProcessSessionHome billingProcessHome =
                (BillingProcessSessionHome) JNDILookup.getFactory(true).lookUpHome(
                BillingProcessSessionHome.class,
                BillingProcessSessionHome.JNDI_NAME);
        remoteBillingProcess = billingProcessHome.create();

        UserSessionHome userHome = (UserSessionHome) JNDILookup.getFactory(
                true).lookUpHome(UserSessionHome.class, 
                    UserSessionHome.JNDI_NAME);
        remoteUser = userHome.create();

        entityId = new Integer(1);
        languageId = new Integer(1);
        cal = new GregorianCalendar();
    }
    /*    
    public void testRun() {
        try {
            // get the latest process
            BillingProcessDTOEx lastDto = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);
        
            // set the configuration to something we are sure about
            BillingProcessConfigurationDTO configDto = remoteBillingProcess.
                    getConfigurationDto(entityId);
            cal.set(2003, GregorianCalendar.APRIL, 1); 
            configDto.setNextRunDate(cal.getTime());
            configDto.setRetries(new Integer(1));
            configDto.setDaysForRetry(new Integer(5));
            configDto.setGenerateReport(new Integer(0));
            remoteBillingProcess.createUpdateConfiguration(new Integer(1),
                 configDto);
            
            // run trigger for March 1st     
            cal.set(2003, GregorianCalendar.MARCH, 1); 
            remoteBillingProcess.trigger(cal.getTime());

            // get latest run (b)            
            BillingProcessDTOEx lastDtoB = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);
            
            // no new process should have run
            assertTrue("1 - No new process", lastDto.getId().intValue() == 
                    lastDtoB.getId().intValue());
            // no retry should have run
            assertTrue("2 - No run", lastDto.getRuns().size() == 
                    lastDtoB.getRuns().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
    }

    public void testReview() {
        try {
            // get the latest process
            BillingProcessDTOEx lastDto = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);
            
            // get the review
            BillingProcessDTOEx reviewDto = remoteBillingProcess.getReviewDto(
                    entityId, languageId);
            
            // not review should be there
            assertNull("3 - Review shouldn't be there", reviewDto);
            
            // set the configuration to something we are sure about
            BillingProcessConfigurationDTO configDto = remoteBillingProcess.
                    getConfigurationDto(entityId);
            configDto.setDaysForReport(new Integer(5));
            configDto.setGenerateReport(new Integer(1));
            configDto.setRetries(new Integer(0));
            remoteBillingProcess.createUpdateConfiguration(new Integer(1),
                 configDto);

            // run trigger for Apr 1st     
            cal.set(2003, GregorianCalendar.APRIL, 1); 
            remoteBillingProcess.trigger(cal.getTime());
            
           // get the latest process
            BillingProcessDTOEx lastDtoB = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);
                    
            // no new process should have run
            assertTrue("4 - No new process", lastDto.getId().intValue() == 
                    lastDtoB.getId().intValue());
                    
            // get the review
            reviewDto = remoteBillingProcess.getReviewDto(
                    entityId, languageId);

            // now review should be there
            assertNotNull("5 - Review should be there", reviewDto);
            
            // the review should have invoices 
            assertTrue("6 - Invoices in review", reviewDto.getGrandTotal().
                    getInvoiceGenerated().intValue() > 0);
            
            
            // disapprove the review
            remoteBillingProcess.setReviewApproval(new Integer(1), entityId,
                    new Boolean(false));
                    
            // run trigger for March 26st     
            cal.set(2003, GregorianCalendar.MARCH, 26); 
            remoteBillingProcess.trigger(cal.getTime());
            
            // get the latest process
            lastDtoB = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);
            
            // no new process should have run
            assertTrue("7 - No new process, too early", lastDto.getId().intValue() == 
                    lastDtoB.getId().intValue());
 
            // get the review
            BillingProcessDTOEx reviewDto2 = remoteBillingProcess.getReviewDto(
                    entityId, languageId);
           
            assertEquals("8 - No new review run", reviewDto.getId(), 
                    reviewDto2.getId());
                    
            // status of the review should still be disapproved
            configDto = remoteBillingProcess.
                    getConfigurationDto(entityId);
            assertEquals("9 - Review still disapproved", configDto.getReviewStatus(),
                    Constants.REVIEW_STATUS_DISAPPROVED);

            // run trigger for March 27st     
            cal.set(2003, GregorianCalendar.MARCH, 27); 
            remoteBillingProcess.trigger(cal.getTime());

            // get the latest process
            lastDtoB = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);
            
            // no new process should have run
            assertEquals("10 - No new process, review disapproved", lastDto.getId(),  
                    lastDtoB.getId());

            // get the review
            reviewDto2 = remoteBillingProcess.getReviewDto(
                    entityId, languageId);
            // since the last one was disapproved, a new one has to be created
            assertFalse("11 - New review run", reviewDto.getId().intValue() == 
                    reviewDto2.getId().intValue());

            // status of the review should now be generated
            configDto = remoteBillingProcess.
                    getConfigurationDto(entityId);
            assertEquals("12 - Review generated", configDto.getReviewStatus(),
                    Constants.REVIEW_STATUS_GENERATED);
            
            // run trigger for March 28     
            cal.set(2003, GregorianCalendar.MARCH, 28); 
            remoteBillingProcess.trigger(cal.getTime());
            
            // get the review
            reviewDto = remoteBillingProcess.getReviewDto(
                    entityId, languageId);
            // the status is generated, so it should not be a new review                    
            assertEquals("13 - No new review run", reviewDto.getId(), 
                    reviewDto2.getId());

            // run trigger for Apr 1     
            cal.set(2003, GregorianCalendar.APRIL, 1); 
            remoteBillingProcess.trigger(cal.getTime());
                    
            // get the latest process
            lastDtoB = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);
                    
            // no new process should have run
            assertEquals("14 - No new process, review not yet revied", lastDto.getId(),  
                    lastDtoB.getId());

            // disapprove the review so it should run again 
            remoteBillingProcess.setReviewApproval(new Integer(1), entityId,
                    new Boolean(false));

            //
            //  Test the 'days for review'
            // 
            // Too early 
            // run trigger for March 15     
            cal.set(2003, GregorianCalendar.MARCH, 15); 
            remoteBillingProcess.trigger(cal.getTime());
            // get the review
            reviewDto2 = remoteBillingProcess.getReviewDto(
                    entityId, languageId);
            // it's too early, no new review should have run
            assertTrue("14.1 - No new review run", reviewDto.getId().intValue() == 
                    reviewDto2.getId().intValue());
             

            //
            //  Run the review and approve it to allow the process to run
            //              
            // run trigger for Apr 1 - retry days
            cal.clear();
            cal.set(2003, GregorianCalendar.APRIL, 1);
            cal.add(GregorianCalendar.DATE, -5); 
            remoteBillingProcess.trigger(cal.getTime());

            // get the review
            reviewDto2 = remoteBillingProcess.getReviewDto(
                    entityId, languageId);
            // since the last one was disapproved, a new one has to be created
            assertFalse("14.2 - New review run", reviewDto.getId().intValue() == 
                    reviewDto2.getId().intValue());
            
            // finally, approve the review
            remoteBillingProcess.setReviewApproval(new Integer(1), entityId,
                    new Boolean(true));

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
    }
   
    
    public void testProcess() throws Exception {
        try {
            // get the latest process
            BillingProcessDTOEx lastDto = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);
            
            // set up the billing porcess date for april 1st
            // set the configuration to something we are sure about
            BillingProcessConfigurationDTO configDto = remoteBillingProcess.
                    getConfigurationDto(entityId);
            cal.set(2003, GregorianCalendar.APRIL, 1); 
            configDto.setNextRunDate(cal.getTime());
            configDto.setPeriodUnitId(Constants.PERIOD_UNIT_MONTH);
            configDto.setPeriodValue(new Integer(1));
            remoteBillingProcess.createUpdateConfiguration(new Integer(1),
                 configDto);
            
            // get the review, so we can later check that what id had
            // is the same that is generated in the real process
            BillingProcessDTOEx reviewDto = remoteBillingProcess.getReviewDto(
                    entityId, languageId);
            
            // run trigger for Apr 1     
            cal.set(2003, GregorianCalendar.APRIL, 1); 
            remoteBillingProcess.trigger(cal.getTime());

            // get the latest process
            BillingProcessDTOEx lastDtoB = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);

            // this is the one and only new process run
            assertFalse("15 - New Process", lastDto.getId().equals(lastDtoB.getId()));
            // initially, runs should be 1
            assertEquals("16 - Only one run", lastDtoB.getRuns().size(), 1);

            // chech that the next billing date is updated
            configDto = remoteBillingProcess.
                    getConfigurationDto(entityId);
            cal.add(GregorianCalendar.MONTH, 1);
            assertTrue("17 - Next billing date for May 1st", 
                    configDto.getNextRunDate().equals(Util.truncateDate(
                        cal.getTime())));
            
            // verify that what just have run, is the same that was displayed
            // in the review
            assertEquals("17.1 - Review invoices = Process invoices",
                    reviewDto.getGrandTotal().getInvoiceGenerated().intValue(),
                    lastDtoB.getGrandTotal().getInvoiceGenerated().intValue());
            
            BillingProcessRunTotalDTOEx aTotal = (BillingProcessRunTotalDTOEx)
                    reviewDto.getGrandTotal().getTotals().get(0);
            BillingProcessRunTotalDTOEx bTotal = (BillingProcessRunTotalDTOEx)
                    lastDtoB.getGrandTotal().getTotals().get(0);
            assertEquals("17.2 - Review invoiced = Process invoiced",
                    aTotal.getTotalInvoiced().floatValue(),
                    bTotal.getTotalInvoiced().floatValue(),
                    0.01F);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
    }



    
    public void testOrdersProcessedDate() {
        String dates[] = { 
            "2003-03-15", "2003-01-15", "2003-01-15", "2003-05-01", // 1 - 4            "2003-03-01", "2003-03-15", "2003-04-01", "2003-05-20", // 5 - 8
            "2003-04-01", "2003-04-01", "2003-04-12", "2003-03-31", // 9 - 12
            null,         null       , "2003-06-20", "2003-03-15", // 13 - 16
            "2003-04-01", "2003-04-01", null,         "2003-02-01", // 17 - 20
            "2003-04-10", null       , "2003-04-15", "2003-02-15", // 21 - 24
            null       , "2003-04-11", null }; // 25 - 27
        
        try {
            for (int f = 0; f < dates.length; f++) {
                OrderDTO order = remoteOrder.getOrder(new Integer(f + 1));
                
                if (order.getNextBillableDay() != null) {
                           
                    if (dates[f] == null ){
                        assertNull("Order " + order.getId(),order.getNextBillableDay());
                    } else {
                        assertEquals("Order " + order.getId(), parseDate(dates[f]),
                                order.getNextBillableDay());
                    } 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
    }
    
    public void testOrdersFlaggedOut() {
        int orders[] = { 1, 2, 5, 6, 8, 9, 12, 13, 15, 16, 17, 19, 20, 21, 24, 25, 27 }; 

        try {
            for (int f = 0; f < orders.length; f++) {
                OrderDTO order = remoteOrder.getOrder(new Integer(orders[f]));
                assertEquals("Order " + order.getId(), order.getStatusId(), 
                        Constants.ORDER_STATUS_FINISHED); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
            
    }
    
    public void testOrdersStillIn() {
        int orders[] = { 3, 4, 7, 10, 11, 14, 18, 22, 23, 26 }; 

        try {
            for (int f = 0; f < orders.length; f++) {
                OrderDTO order = remoteOrder.getOrder(new Integer(orders[f]));
                assertEquals("Order " + order.getId(), order.getStatusId(), 
                        Constants.ORDER_STATUS_ACTIVE); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
           
    }
    
    public void testPeriodsBilled() {
        String dateRanges[][] = {
            { null, null, "1" }, // 6 
            { "2003-01-01", "2003-04-01", "3"  }, // 7
            { null, null, "1" }, // 9
            { "2003-03-01", "2003-04-01", "1" }, // 10
            { "2003-03-15", "2003-04-12", "4" }, // 11
            { null, null, "1" }, // 17
            { "2003-01-01", "2003-04-01", "1" }, // 18
            { null, null, "1" }, // 24
            { "2003-03-01", "2003-04-01", "1" }, // 25
            { "2003-04-04", "2003-04-11", "1" }, // 26
            { "2003-03-01", "2003-04-01", "1" }, // 27
        };
        
        int orders[] = { 6, 7, 9, 10, 11, 17, 18, 24, 25, 26, 27};
       
        try {
            // get the latest process
            BillingProcessDTOEx lastDto = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);
            
            for (int f = 0; f < orders.length; f++) {
                OrderDTOEx order = remoteOrder.getOrderEx(
                        new Integer(orders[f]), languageId);
                Date from = parseDate(dateRanges[f][0]);
                Date to = parseDate(dateRanges[f][1]);
                Integer number = Integer.valueOf(dateRanges[f][2]);
                
                OrderProcessDTO period = (OrderProcessDTO)
                        order.getPeriods().get(0);
                assertEquals("(from) Order " + order.getId(),period.getPeriodStart(),
                        from);
                assertEquals("(to) Order " + order.getId(),period.getPeriodEnd(), to);
                assertEquals("(number) Order " + order.getId(), number, 
                        period.getPeriodsIncluded());
 
                BillingProcessDTO process = (BillingProcessDTO)
                        order.getProcesses().get(0);
                assertEquals("(process) Order " + order.getId(),lastDto.getId(), 
                        process.getId()); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
        
    }
    
    public void testExcluded() {
        int orders[] = {1,2,3,4,5,8,12,13,14,15,16,19,20,21,22,23};
        
        try {
            for (int f = 0; f < orders.length; f++) {
                OrderDTOEx order = remoteOrder.getOrderEx(
                        new Integer(orders[f]), languageId);
                
                assertTrue("1 - Order " + order.getId(),order.getPeriods().isEmpty());
                assertTrue("2 - Order " + order.getId(),order.getProcesses().isEmpty());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }

    }
    
    
    public void testInvoicesFlaggedOut() {
        int invoices[] = { 3, 4, 6, 7, 8, 9, 10, 11, 13, 14, 17, 18, 19, 
                20, 21, 22, 23 };

        try {
            for (int f = 0; f < invoices.length; f++) {
                InvoiceDTO invoice = remoteInvoice.getInvoice(
                        new Integer(invoices[f]));
                assertEquals("Invoice " + invoice.getId(), 
                        new Integer(0), invoice.getToProcess()); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
            
    }

    public void testInvoicesStillIn() {
        int invoices[] = { 1, 2, 5, 12, 15, 16 };

        try {
            for (int f = 0; f < invoices.length; f++) {
                InvoiceDTO invoice = remoteInvoice.getInvoice(
                        new Integer(invoices[f]));
                assertEquals("Invoice " + invoice.getId(), 
                        invoice.getToProcess(), new Integer(1)); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
            
    }
    
    public void testInvoicesDelegated() {
        int invoices[] = { 4, 6, 8, 9, 10, 11, 17, 18, 19, 20, 21, 22 };

        try {
            for (int f = 0; f < invoices.length; f++) {
                InvoiceDTOEx invoice = remoteInvoice.getInvoiceEx(
                        new Integer(invoices[f]), new Integer(1));
                assertNotNull("Invoice " + invoice.getId(), 
                        invoice.getDelegatedInvoiceId()); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
         
    }

    public void testInvoicesNotDelegated() {
        int invoices[] = { 1,2,5,12,13,15,16 };

        try {
            for (int f = 0; f < invoices.length; f++) {
                InvoiceDTOEx invoice = remoteInvoice.getInvoiceEx(
                        new Integer(invoices[f]), new Integer(1));
                assertNull("Invoice " + invoice.getId(), 
                        invoice.getDelegatedInvoiceId()); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
         
    }

// This should work when data of the order lines makes sense (quantity * price = total).
 //  Yet, the periods have to be added in this function
        public void testGeneratedInvoiceTotals() {
        try {
            
            for (Iterator it = remoteBillingProcess.getGeneratedInvoices(
                    new Integer(5)).iterator(); it.hasNext(); ) {
                InvoiceDTOEx invoice = (InvoiceDTOEx) it.next();
                float orderTotal = 0F;
                for (Iterator it2 = invoice.getOrders().iterator(); 
                        it2.hasNext();){
                    OrderDTO order = (OrderDTO) it2.next();
                    OrderDTOEx orderDto = remoteOrder.getOrderEx(order.getId(),
                            languageId);
                    orderTotal += orderDto.getTotal().floatValue();
                }
    
                assertEquals("Orders total = Invoice " + invoice.getId() + " total", orderTotal, 
                        invoice.getTotal().floatValue() - 
                            invoice.getCarriedBalance().floatValue(), 0.005F);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
    }

    public void testPayments() {
        try {
            
            Collection invoices = remoteBillingProcess.getGeneratedInvoices(
                remoteBillingProcess.getLast(entityId));
            
            for (Iterator it = invoices.iterator(); it.hasNext();) {
                InvoiceDTOEx invoice = (InvoiceDTOEx) it.next();
                
                if (invoice.getUserId().intValue() == 9) { // Chisky Peters has a cc to pay with
                    assertFalse("Invoice " + invoice.getId(), invoice.getPayments()
                            .isEmpty());
                    PaymentDTO payment = (PaymentDTO) invoice.getPayments().get(0);

                    if (payment.getResultId().equals(Constants.RESULT_OK)) {
                        assertEquals("(to_process) Invoice " + invoice.getId(),
                            new Integer(0), invoice.getToProcess()); 
                    } else {
                        assertEquals("(to_process) Invoice " + invoice.getId(),
                            new Integer(1), invoice.getToProcess()); 
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
    }


    public void testRetry() {
        try {
            // set up the billing porcess date for april 1st
            // set the configuration to something we are sure about
            BillingProcessConfigurationDTO configDto = remoteBillingProcess.
                    getConfigurationDto(entityId);
            configDto.setRetries(new Integer(1));
            configDto.setDaysForRetry(new Integer(5));
            configDto.setGenerateReport(new Integer(0));
            remoteBillingProcess.createUpdateConfiguration(new Integer(1),
                 configDto);
                 
            // get the latest process
            BillingProcessDTOEx lastDto = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);


            // run trigger for Apr 5     
            cal.set(2003, GregorianCalendar.APRIL, 5); 
            remoteBillingProcess.trigger(cal.getTime());
            
            // get the latest process
            BillingProcessDTOEx lastDtoB = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);

            assertEquals("18 - No retries", 1, lastDtoB.getRuns().size());
            assertTrue("18b - No new Process", lastDtoB.getId().equals(lastDto.getId()));
            
            // run trigger for Apr 6
            cal.set(2003, GregorianCalendar.APRIL, 6); 
            remoteBillingProcess.trigger(cal.getTime());
                      
            // get the latest process
            BillingProcessDTOEx lastDtoC = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);

            // now a retry had to run           
            assertEquals("19 - First retry", 2, lastDtoC.getRuns().size());
            assertTrue("20 - No new Process", lastDtoB.getId().equals(lastDtoC.getId()));
            
            // run trigger for Apr 16
            cal.set(2003, GregorianCalendar.APRIL, 16); 
            remoteBillingProcess.trigger(cal.getTime());

            // get the latest process
            lastDtoC = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);

            assertEquals("21 - No new retry", 2, lastDtoC.getRuns().size());
            assertTrue("22 - No new Process", lastDtoB.getId().equals(lastDtoC.getId()));
           
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
    }
    
    public void testTotals() {
        try {
            // get the latest process
            BillingProcessDTOEx lastDto = remoteBillingProcess.getDto(
                    remoteBillingProcess.getLast(entityId),
                    languageId);

            // get the grand total for this process
            BillingProcessRunDTOEx grandTotal = lastDto.getGrandTotal();
            
            // add up all the totals and compare to the grand total
            // go over all the runs
            float totalInvoicedSum = 0;
            for (int f = 0; f < lastDto.getRuns().size(); f++) {
                BillingProcessRunDTOEx runDto = (BillingProcessRunDTOEx) lastDto.
                        getRuns().get(f);
                // go over the totals
                for (int ff = 0; ff < runDto.getTotals().size(); ff++) {
                    BillingProcessRunTotalDTOEx totalDto = 
                            (BillingProcessRunTotalDTOEx) runDto.getTotals()
                                .get(ff);
                    if (totalDto.getCurrencyId().intValue() == 1) {
                        totalInvoicedSum += totalDto.getTotalInvoiced().
                            floatValue();
                    }
                }
            }
            
            // verify that my invoiced total is the same as the grandtotal's
            for (int f = 0; f < grandTotal.getTotals().size(); f++) {
                BillingProcessRunTotalDTOEx totalDto = 
                        (BillingProcessRunTotalDTOEx) grandTotal.getTotals()
                            .get(f);
                if (totalDto.getCurrencyId().intValue() == 1) {
                    assertEquals("Total invoiced sum", 
                            totalDto.getTotalInvoiced().floatValue(),
                            totalInvoicedSum, 0.005F);        
                    break;
                }
            }
            
            // now verify that the numbers of total/pm are consitent
            // get the first run of this process
            BillingProcessRunDTOEx runDto = (BillingProcessRunDTOEx) lastDto.
                    getRuns().get(0);
            // get the total for the currency 1
            BillingProcessRunTotalDTOEx totalDto = null;
            for (int f = 0; f < runDto.getTotals().size(); f++) {
                totalDto = (BillingProcessRunTotalDTOEx) runDto.getTotals()
                        .get(f);
                if (totalDto.getCurrencyId().intValue() == 1) {
                    break;
                }
            }
            
            assertEquals("Total numbers", totalDto.getTotalInvoiced().floatValue(),
                    totalDto.getTotalNotPaid().floatValue() +
                    totalDto.getTotalPaid().floatValue(), 0.01F);
            
            // get the pms for this total
            float totalpms = 0;
            for (Enumeration en = totalDto.getPmTotals().elements(); 
                    en.hasMoreElements();) {
                totalpms += ((Float) en.nextElement()).floatValue();
            }
            assertEquals("Pms total", totalDto.getTotalPaid().floatValue(),
                    totalpms, 0.01F);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
    }
    
    public void testAgeing() {
        try {
            Integer userId = new Integer(17);
            
            // the grace period should keep this user active
            cal.clear();
            cal.set(2003, GregorianCalendar.MAY, 6);
            remoteBillingProcess.reviewUsersStatus(cal.getTime());
            UserDTOEx user = remoteUser.getUserDTOEx(userId);
            assertEquals("Grace period", UserDTOEx.STATUS_ACTIVE,
                    user.getStatusId());
                    
            // when the grace over, she should be warned
            cal.set(2003, GregorianCalendar.MAY, 7);
            remoteBillingProcess.reviewUsersStatus(cal.getTime());
            user = remoteUser.getUserDTOEx(userId);
            assertEquals("to overdue", UserDTOEx.STATUS_ACTIVE.intValue() + 1,
                    user.getStatusId().intValue());

            // two day after, the status should be the same
            cal.set(2003, GregorianCalendar.MAY, 9);
            remoteBillingProcess.reviewUsersStatus(cal.getTime());
            user = remoteUser.getUserDTOEx(userId);
            assertEquals("still overdue", UserDTOEx.STATUS_ACTIVE.intValue() + 1,
                    user.getStatusId().intValue());

            // after three days of the warning, fire the next one
            cal.set(2003, GregorianCalendar.MAY, 10);
            remoteBillingProcess.reviewUsersStatus(cal.getTime());
            user = remoteUser.getUserDTOEx(userId);
            assertEquals("to overdue 2", UserDTOEx.STATUS_ACTIVE.intValue() + 2,
                    user.getStatusId().intValue());

            // the next day it goes to suspended
            cal.set(2003, GregorianCalendar.MAY, 11);
            remoteBillingProcess.reviewUsersStatus(cal.getTime());
            user = remoteUser.getUserDTOEx(userId);
            assertEquals("to suspended", UserDTOEx.STATUS_ACTIVE.intValue() + 4,
                    user.getStatusId().intValue());

            // two days for suspended 3
            cal.set(2003, GregorianCalendar.MAY, 13);
            remoteBillingProcess.reviewUsersStatus(cal.getTime());
            user = remoteUser.getUserDTOEx(userId);
            assertEquals("to suspended 3", UserDTOEx.STATUS_ACTIVE.intValue() + 6,
                    user.getStatusId().intValue());

            // two days for suspended 3
            cal.add(GregorianCalendar.DATE, 30);
            remoteBillingProcess.reviewUsersStatus(cal.getTime());
            user = remoteUser.getUserDTOEx(userId);
            assertEquals("deleted", new Integer(1), user.getDeleted());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception:" + e);
        }
    }

    public static Date parseDate(String str) throws Exception{
        if (str == null ) {
            return null;
        }
        
        if ( str.length() != 10 || str.charAt(4) != '-' || str.charAt(7) != '-') {
            throw new Exception("Can't parse " + str);
           
        }
        
        try {
            int year = Integer.valueOf(str.substring(0,4)).intValue();
            int month = Integer.valueOf(str.substring(5,7)).intValue();
            int day = Integer.valueOf(str.substring(8,10)).intValue();
        
            GregorianCalendar cal = new GregorianCalendar(year, month - 1, day);
        
            return cal.getTime();
        } catch (Exception e) {
            throw new Exception("Can't parse " + str);
        }
    }
*/    
    public void testQuicky() {
        try {
            cal.clear();
            cal.set(2006, GregorianCalendar.JULY, 6); 
            remoteBillingProcess.trigger(cal.getTime());
            //remoteBillingProcess.reviewUsersStatus(cal.getTime());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
