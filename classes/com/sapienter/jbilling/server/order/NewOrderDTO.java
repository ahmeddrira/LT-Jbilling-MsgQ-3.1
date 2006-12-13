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

package com.sapienter.jbilling.server.order;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.entity.OrderDTO;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @author emilc
 *
 */
public class NewOrderDTO extends OrderDTO {
    protected Hashtable orderLines = null;
    private Vector rawOrderLines = null;
    private Float orderTotal = null;
    private Integer period = null;
    private Integer userId = null; // who is buying ?
    private String promoCode = null;
    // these are necessary for the view page, to show the description
    // instead of the ids
    private String periodStr = null;
    private String billingTypeStr = null;
    
    private String currencySymbol = null;

    public NewOrderDTO() {
        orderLines = new Hashtable();
        orderTotal = new Float(0.0F);
        period = Constants.ORDER_PERIOD_ONCE;
        rawOrderLines = null;
    }
    
    public NewOrderDTO(OrderWS order) 
        throws SessionInternalError {
        super(order);
        try {
            userId = order.getUserId();
            period = order.getPeriod();
            periodStr = order.getPeriodStr();
            billingTypeStr = order.getBillingTypeStr();
            orderLines = new Hashtable();
            rawOrderLines = new Vector();
            
            for (int f=0; f < order.getOrderLines().length; f++) {
                Logger.getLogger(NewOrderDTO.class).debug("line " + order.getOrderLines()[f]);
                OrderLineDTOEx line = new OrderLineDTOEx(order.getOrderLines()[f]);
                rawOrderLines.add(line); 
                // the next line is to be able to call BasicLineTotalTaks
                orderLines.put(line.getItemId(), line);
            }
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    /**
     * Returns the orderLines.
     * This has to be in the form of a collection, because if it is a Hashtable
     * the iterate method of the sturts logic tag doesn't know about the methods
     * of the beans inside the collection. With a collection, direct calls to
     * these methods can be done. This is posible to do because we are puting
     * only one type of class in the hashtable.
     * @return Enumeration
     */
    public Enumeration getOrderLines() {
        return orderLines.elements();
    }

    // useful for testcases
    public Hashtable getOrderLinesMap() {
        return orderLines;
    }

	// probably totaly useless
    public void setOrderLinesMap(Hashtable newOrderLines) {
        orderLines = newOrderLines;
    }
    
    // makes the client simpler and less cupled with me
    public boolean isEmpty() {
        return orderLines.isEmpty();
    }

    public Object getOrderLine(String itemId) {
        return orderLines.get(Integer.valueOf(itemId));
    }

    public void setOrderLine(Integer itemId, OrderLineDTOEx line) {
        orderLines.put(itemId, line);
    }

    /**
     * Returns the orderTotal.
     * @return Float
     */
    public Float getOrderTotal() {
        return orderTotal;
    }
    
    public void setOrderTotal(Float newTotal) {
        orderTotal = newTotal;
    }

    public Integer getNumberOfLines() {
        return new Integer(orderLines.size());
    }

    public String toString() {
        return "NewOrderDTO = total ["
            + orderTotal
            + "] items["
            + orderLines
            + "]";
    }
    /**
     * Returns the period.
     * @return Integer
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * Sets the period.
     * @param period The period to set
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * Returns the user id of the customer that is buying.
     * @return Integer
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the user id of the customer that is buying.
     * @param userId The userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    /**
     * @return
     */
    public String getPromoCode() {
        return promoCode;
    }

    /**
     * @param string
     */
    public void setPromoCode(String string) {
        promoCode = string;
    }

    /**
     * @return
     */
    public String getBillingTypeStr() {
        return billingTypeStr;
    }

    /**
     * @return
     */
    public String getPeriodStr() {
        return periodStr;
    }

    /**
     * @param string
     */
    public void setBillingTypeStr(String string) {
        billingTypeStr = string;
    }

    /**
     * @param string
     */
    public void setPeriodStr(String string) {
        periodStr = string;
    }

    /**
     * @return
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * @param currencySymbol
     */
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Vector getRawOrderLines() {
        return rawOrderLines;
    }
    public void setRawOrderLines(Vector rawOrderLines) {
        this.rawOrderLines = rawOrderLines;
    }
}
