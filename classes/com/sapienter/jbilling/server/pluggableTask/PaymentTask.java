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

package com.sapienter.jbilling.server.pluggableTask;

import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;

/*
 * Consider a base class to facilitante notifiaction, suspension, and other 
 * actions for overdue users
 */
public interface PaymentTask {
    
    
    /**
     * Creation of a payment
     * @param paymentInfo This can be an extension of PaymentDTO, with the
     * additional information for this implementation. For example, a task
     * fro credit card processing would expect an extension of PaymentDTO with
     * the credit card information.
     * @return If the next pluggable task has to be called or not. True would 
     * be returned usually when the gatway is not available.
     */
    boolean process(PaymentDTOEx paymentInfo) throws PluggableTaskException;
    
    void failure(Integer userId, Integer retry);

    /**
     * Does the authorization, but not capture, of a payment. This means that
     * the amount is approved, but if this charge is not confirmed within X
     * number of days, the charge will be dropped and the credit card not charged.
     * The way to confirm the charge is by calling ConfirmPreAuth
     * @param paymentInfo 
     *   This object needs to have
     *   - currency
     *   - amount
     *   - credit card
     *   - the id of the existing payment row
     * @return If the next pluggable task has to be called or not. True would 
     * be returned usually when the gatway is not available.
     * @throws PluggableTaskException
     */
    boolean preAuth(PaymentDTOEx paymentInfo) 
            throws PluggableTaskException;
    
    /**
     * This will confirm a previously authorized charge, so it is 'captured'.
     * If this method is not called in a pre-auth, the charge will be dropped.
     * By calling this method, the end customer will see the charge in her 
     * credit card.
     * @param auth
     * @param amount
     * @param currencyId
     * @return If the next pluggable task has to be called or not. True would 
     * be returned usually when the gatway is not available.
     * @throws PluggableTaskException
     */
    boolean confirmPreAuth(PaymentAuthorizationDTO auth, 
            PaymentDTOEx paymentInfo) 
            throws PluggableTaskException;
    
}
