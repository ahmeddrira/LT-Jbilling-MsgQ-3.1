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
 * Created on Aug 11, 2004
 *
 */
package com.sapienter.jbilling.client.invoice;

import java.rmi.RemoteException;
import java.util.HashMap;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.UpdateOnlyCrudActionBase;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;

public class NumberingAction extends UpdateOnlyCrudActionBase<NumberingActionContext> {
    private static final String FORM_NUMBERING = "invoiceNumbering";
    private static final String FORWARD_EDIT = "invoiceNumbering_edit";
    
    private static final String FIELD_PREFIX = "prefix";
    private static final String FIELD_NUMBER = "number";
    
    private static final String MESSAGE_SUCCESSFULLY_UPDATED = "invoice.numbering.updated";
    
    private final UserSession myUserSession;
    
    public NumberingAction(){
        super(FORM_NUMBERING, "invoice numbering", FORWARD_EDIT);
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            UserSessionHome userHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
        
            myUserSession = userHome.create();
        } catch (Exception e) {
            throw new SessionInternalError(
                    "Initializing invoice numbering "
                            + " CRUD action: " + e.getMessage());
        }
    }
    
    @Override
    protected NumberingActionContext doEditFormToDTO() throws RemoteException {
        String prefix = (String) myForm.get(FIELD_PREFIX);
        String number = (String) myForm.get(FIELD_NUMBER);
        return new NumberingActionContext(prefix, number);
    }
    
    @Override
    protected ForwardAndMessage doUpdate(NumberingActionContext dto) throws RemoteException {
        HashMap<Integer, String> params = new HashMap<Integer, String>();
        params.put(Constants.PREFERENCE_INVOICE_PREFIX, dto.getPrefix());
        params.put(Constants.PREFERENCE_INVOICE_NUMBER, dto.getNumber()); 
        myUserSession.setEntityParameters(entityId, params);
        return getForwardEdit(MESSAGE_SUCCESSFULLY_UPDATED);
    }
    
    @Override
    protected ForwardAndMessage doSetup() throws RemoteException {
        Integer[] preferenceIds = new Integer[] {
                Constants.PREFERENCE_INVOICE_PREFIX, 
                Constants.PREFERENCE_INVOICE_NUMBER,
        };
        HashMap<?, ?> result = myUserSession.getEntityParameters(entityId, preferenceIds);
        
        String prefix = (String) result.get(Constants.PREFERENCE_INVOICE_PREFIX); 
        String number = (String) result.get(Constants.PREFERENCE_INVOICE_NUMBER); 
        myForm.set(FIELD_NUMBER, notNull(number));
        myForm.set(FIELD_PREFIX, notNull(prefix));
        
        return getForwardEdit();
    }
    

    @Override
    protected void preEdit() {
        super.preEdit();
        setForward(FORWARD_EDIT);
    }

    private static String notNull(String text){
        return text == null ? "" : text;
    }
    
}
