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

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.BillingProcessConfigurationEntityLocal;
import com.sapienter.jbilling.interfaces.BillingProcessConfigurationEntityLocalHome;
import com.sapienter.jbilling.server.entity.BillingProcessConfigurationDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.EventLogger;

public class ConfigurationBL {
    private JNDILookup EJBFactory = null;
    private BillingProcessConfigurationEntityLocalHome configurationHome = null;
    private BillingProcessConfigurationEntityLocal configuration = null;
    private Logger log = null;
    private EventLogger eLogger = null;
    
    public ConfigurationBL(Integer entityId) 
            throws NamingException, FinderException {
        init();
        set(entityId);
    }
    
    public ConfigurationBL() throws NamingException {
        init();
    }
    
    public ConfigurationBL(BillingProcessConfigurationEntityLocal cfg) 
            throws NamingException {
        init();
        configuration = cfg;
    }
    
    private void init() throws NamingException {
        log = Logger.getLogger(ConfigurationBL.class);     
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        configurationHome = (BillingProcessConfigurationEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                BillingProcessConfigurationEntityLocalHome.class,
                BillingProcessConfigurationEntityLocalHome.JNDI_NAME);

    }

    public BillingProcessConfigurationEntityLocal getEntity() {
        return configuration;
    }
    
    public void set(Integer id) throws FinderException {
        configuration = configurationHome.findByEntity(id);
    }

    
    public Integer createUpdate(Integer executorId, 
            BillingProcessConfigurationDTO dto) 
            throws CreateException {
        try {
            configuration = configurationHome.findByEntity(dto.getEntityId());
            if (!configuration.getGenerateReport().equals(
                    dto.getGenerateReport())) {
                eLogger.audit(executorId, 
                        Constants.TABLE_BILLING_PROCESS_CONFIGURATION, 
                        configuration.getId(), EventLogger.MODULE_BILLING_PROCESS,
                        EventLogger.ROW_UPDATED, configuration.getGenerateReport(), 
                        null, null);
                configuration.setGenerateReport(dto.getGenerateReport());
                configuration.setReviewStatus(
                        dto.getGenerateReport().intValue() == 1 
                            ? Constants.REVIEW_STATUS_GENERATED
                            : Constants.REVIEW_STATUS_APPROVED);
            } else {
                eLogger.audit(executorId, 
                        Constants.TABLE_BILLING_PROCESS_CONFIGURATION, 
                        configuration.getId(), EventLogger.MODULE_BILLING_PROCESS,
                        EventLogger.ROW_UPDATED, null, null, null);
            }
            configuration.setNextRunDate(dto.getNextRunDate());
        } catch (FinderException e) {
            configuration = configurationHome.create(dto.getEntityId(), 
                    dto.getNextRunDate(), dto.getGenerateReport());
        }
        configuration.setDaysForReport(dto.getDaysForReport());
        configuration.setDaysForRetry(dto.getDaysForRetry());
        configuration.setRetries(dto.getRetries());
        configuration.setPeriodUnitId(dto.getPeriodUnitId());
        configuration.setPeriodValue(dto.getPeriodValue());
        configuration.setDueDateUnitId(dto.getDueDateUnitId());
        configuration.setDueDateValue(dto.getDueDateValue());
        configuration.setDfFm(dto.getDfFm());
        configuration.setOnlyRecurring(dto.getOnlyRecurring());
        configuration.setInvoiceDateProcess(dto.getInvoiceDateProcess());
        configuration.setAutoPayment(dto.getAutoPayment());
        configuration.setAutoPaymentApplication(dto.getAutoPaymentApplication());
        configuration.setMaximumPeriods(dto.getMaximumPeriods());
        
        return configuration.getId();
    }
    
    public BillingProcessConfigurationDTO getDTO() {
        BillingProcessConfigurationDTO dto = new BillingProcessConfigurationDTO();
        
        dto.setDaysForReport(configuration.getDaysForReport());
        dto.setDaysForRetry(configuration.getDaysForRetry());
        dto.setEntityId(configuration.getEntityId());
        dto.setGenerateReport(configuration.getGenerateReport());
        dto.setId(configuration.getId());
        dto.setNextRunDate(configuration.getNextRunDate());
        dto.setRetries(configuration.getRetries());
        dto.setPeriodUnitId(configuration.getPeriodUnitId());
        dto.setPeriodValue(configuration.getPeriodValue());
        dto.setReviewStatus(configuration.getReviewStatus());
        dto.setDueDateUnitId(configuration.getDueDateUnitId());
        dto.setDueDateValue(configuration.getDueDateValue());
        dto.setDfFm(configuration.getDfFm());
        dto.setOnlyRecurring(configuration.getOnlyRecurring());
        dto.setInvoiceDateProcess(configuration.getInvoiceDateProcess());
        dto.setAutoPayment(configuration.getAutoPayment());
        dto.setMaximumPeriods(configuration.getMaximumPeriods());
        dto.setAutoPaymentApplication(configuration.getAutoPaymentApplication());
        
        return dto;
    }

    public void setReviewApproval(Integer executorId, boolean flag) 
            throws CreateException {

        eLogger.audit(executorId, 
                Constants.TABLE_BILLING_PROCESS_CONFIGURATION, 
                configuration.getId(), EventLogger.MODULE_BILLING_PROCESS,
                EventLogger.ROW_UPDATED, configuration.getReviewStatus(), 
                null, null);
        configuration.setReviewStatus(flag ? Constants.REVIEW_STATUS_APPROVED 
                : Constants.REVIEW_STATUS_DISAPPROVED);

    }
    
}
