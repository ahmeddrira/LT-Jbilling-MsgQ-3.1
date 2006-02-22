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
 * Created on Feb 4, 2005
 *
 */
package com.sapienter.jbilling.server.user;

import java.io.Serializable;

/**
 * @author Emil
 * @jboss-net.xml-schema urn="sapienter:CreateResponseWS"
 */
public class CreateResponseWS implements Serializable {
    private Integer userId = null;
    private Integer orderId = null;
    private Integer invoiceId = null;
    private Integer paymentId = null;
    private Integer paymentResult = null;
    
    public Integer getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
    public Integer getOrderId() {
        return orderId;
    }
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    public Integer getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
    public Integer getPaymentResult() {
        return paymentResult;
    }
    public void setPaymentResult(Integer paymentResult) {
        this.paymentResult = paymentResult;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String toString() {
        return "user id = " + userId + " invoice id = " + invoiceId +
                " order id = " + orderId + " paymentId = " + paymentId +
                " payment result = " + paymentResult;
    }
}
