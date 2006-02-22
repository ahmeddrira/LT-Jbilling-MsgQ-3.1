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

package com.sapienter.jbilling.client.item;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sapienter.jbilling.client.util.GenericMaintainAction;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ItemSession;
import com.sapienter.jbilling.interfaces.ItemSessionHome;

public class PromotionMaintainAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Logger log = Logger.getLogger(PromotionMaintainAction.class);
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ItemSessionHome itemHome =
                    (ItemSessionHome) EJBFactory.lookUpHome(
                    ItemSessionHome.class,
                    ItemSessionHome.JNDI_NAME);
        
            ItemSession itemSession = itemHome.create();
            GenericMaintainAction gma = new GenericMaintainAction(mapping,
                    form, request, response, servlet, itemSession, 
                    "promotion");
                    
            return gma.process();            
        } catch (Exception e) {
            log.error("Exception ", e);
        }
        
        return mapping.findForward("error");
    }
  
}
