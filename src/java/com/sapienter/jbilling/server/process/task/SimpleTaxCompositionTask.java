/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.process.task;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.invoice.NewInvoiceDTO;
import com.sapienter.jbilling.server.invoice.db.InvoiceLineDTO;
import com.sapienter.jbilling.server.item.db.ItemDAS;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.ItemTypeDTO;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.ParameterDescription;
import com.sapienter.jbilling.server.user.contact.db.ContactDAS;
import com.sapienter.jbilling.server.user.contact.db.ContactDTO;
import com.sapienter.jbilling.server.user.contact.db.ContactFieldDTO;
import com.sapienter.jbilling.server.util.Constants;

/**
 * This plug-in calculates taxes for invoice.
 *
 * Plug-in parameters:
 *
 *      custom_contact_field_id     (optional) The id of CCF that if its value is 'true' or 'yes' for a customer,
 *                                  then the customer is considered exempt. Exempt customers do not get the tax
 *                                  added to their invoices.
 *      item_exempt_category_id     (optional) The id of an item category that, if the item belongs to, it is
 *                                  exempt from taxes
 *
 * @author Alexander Aksenov, Vikas Bodani
 * @since 30.04.11
 */
public class SimpleTaxCompositionTask extends AbstractChargeTask {

    private static final Logger LOG = Logger.getLogger(SimpleTaxCompositionTask.class);
    
    // optional, may be empty
    protected final static ParameterDescription PARAM_CUSTOM_CONTACT_FIELD_ID = new ParameterDescription("custom_contact_field_id", false, ParameterDescription.Type.STR);
    protected final static ParameterDescription PARAM_ITEM_EXEMPT_CATEGORY_ID = new ParameterDescription("item_exempt_category_id", false, ParameterDescription.Type.STR);

    protected Integer itemExemptCategoryId = null;
    protected Integer customContactFieldId = null;
    
    //initializer for pluggable params
    {
        descriptions.add(PARAM_CUSTOM_CONTACT_FIELD_ID);
        descriptions.add(PARAM_ITEM_EXEMPT_CATEGORY_ID);
    }
    
    /**
     * 
     * @param invoice
     * @param userId
     */
    protected BigDecimal calculateAndApplyTax(NewInvoiceDTO invoice, Integer userId) { 
        
        LOG.debug("calculateAndApplyTax");
        
        BigDecimal invoiceAmountSum= super.calculateAndApplyTax(invoice, userId);
        
        if (taxItem.getPercentage() != null) {
            
            LOG.debug("Exempt Category " + itemExemptCategoryId);
            if (itemExemptCategoryId != null) {
                // find exemp items and subtract price
                for (int i = 0; i < invoice.getResultLines().size(); i++) {
                    InvoiceLineDTO invoiceLine = (InvoiceLineDTO) invoice.getResultLines().get(i);
                    ItemDTO item = invoiceLine.getItem();
                    
                    if (item != null) {
                        Set<ItemTypeDTO> itemTypes = new ItemDAS().find(item.getId()).getItemTypes();
                        for (ItemTypeDTO itemType : itemTypes) {
                            if (itemType.getId() == itemExemptCategoryId) {
                                LOG.debug("Item " + item.getDescription() + " is Exempt. Category " + itemType.getId());
                                invoiceAmountSum = invoiceAmountSum.subtract(invoiceLine.getAmount());
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        this.invoiceLineTypeId= Constants.INVOICE_LINE_TYPE_TAX;
        
        return invoiceAmountSum;
    }
    
    /**
     * Custom logic to determine if the tax should be applied to this user's invoice
     * @param userId The user_id of the Invoice
     * @return
     */
    @Override
    protected boolean isTaxCalculationNeeded(NewInvoiceDTO invoice, Integer userId) {
        LOG.debug("isTaxCalculationNeeded()");
        if (customContactFieldId == null) {
            return true;
        }
        ContactDTO contactDto = new ContactDAS().findPrimaryContact(userId);
        if (contactDto == null) {
            return true;
        }
    
        for (ContactFieldDTO contactField : contactDto.getFields()) {
            if (contactField.getType().getId() == customContactFieldId) {
                String value = contactField.getContent();
                if ("yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value)) {
                    return false;
                }
            }
        }
    
        return true;
    }
    
    /**
     * Set the current plugin params
     */
    @Override
    protected void setPluginParameters()  throws TaskException {
        LOG.debug("setPluginParameters()");
        super.setPluginParameters();
        try {
            String paramValue = getParameter(PARAM_ITEM_EXEMPT_CATEGORY_ID.getName(), "");
            if (paramValue != null && !"".equals(paramValue.trim())) {
                itemExemptCategoryId = new Integer(paramValue);
                LOG.debug("itemExemptCategoryId set.");
            }
            paramValue = getParameter(PARAM_CUSTOM_CONTACT_FIELD_ID.getName(), "");
            if (paramValue != null && !"".equals(paramValue.trim())) {
                customContactFieldId = new Integer(paramValue);
                LOG.debug("customContactFieldId set.");
            }
        } catch (NumberFormatException e) {
            LOG.error("Incorrect plugin configuration", e);
            throw new TaskException(e);
        }
    }

}
