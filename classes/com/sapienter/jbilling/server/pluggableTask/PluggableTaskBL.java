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

import java.util.Iterator;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.PluggableTaskEntityLocal;
import com.sapienter.jbilling.interfaces.PluggableTaskEntityLocalHome;
import com.sapienter.jbilling.interfaces.PluggableTaskParameterEntityLocal;
import com.sapienter.jbilling.interfaces.PluggableTaskParameterEntityLocalHome;
import com.sapienter.jbilling.server.util.EventLogger;

public class PluggableTaskBL {
    private JNDILookup EJBFactory = null;
    private PluggableTaskEntityLocalHome pluggableTaskHome = null;
    private PluggableTaskParameterEntityLocalHome pluggableTaskParameterHome = null;
    private PluggableTaskEntityLocal pluggableTask = null;
    private Logger log = null;
    private EventLogger eLogger = null;
    
    public PluggableTaskBL(Integer pluggableTaskId) 
            throws NamingException, FinderException {
        init();
        set(pluggableTaskId);
    }
    
    public PluggableTaskBL() throws NamingException {
        init();
    }
    
    private void init() throws NamingException {
        log = Logger.getLogger(PluggableTaskBL.class);     
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        pluggableTaskHome = (PluggableTaskEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                PluggableTaskEntityLocalHome.class,
                PluggableTaskEntityLocalHome.JNDI_NAME);
        pluggableTaskParameterHome = (PluggableTaskParameterEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                PluggableTaskParameterEntityLocalHome.class,
                PluggableTaskParameterEntityLocalHome.JNDI_NAME);
    }

    public PluggableTaskEntityLocal getEntity() {
        return pluggableTask;
    }

    public PluggableTaskEntityLocalHome getHome() {
        return pluggableTaskHome;
    }
    
    public void set(Integer id) throws FinderException {
        pluggableTask = pluggableTaskHome.findByPrimaryKey(id);
    }
    
    public void set(Integer entityId, Integer typeId) 
            throws FinderException {
        pluggableTask = pluggableTaskHome.findByEntityType(entityId, typeId);
        
    }
    
    public PluggableTaskDTOEx getDTO() {
        PluggableTaskDTOEx retValue = new PluggableTaskDTOEx();
        
        retValue.setEntityId(pluggableTask.getEntityId());
        retValue.setId(pluggableTask.getId());
        retValue.setProcessingOrder(pluggableTask.getProcessingOrder());
        
        for (Iterator it = pluggableTask.getParameters().iterator();
                it.hasNext(); ) {
            retValue.getParameters().add(getParameterDTO(
                    (PluggableTaskParameterEntityLocal) it.next()));
                
        }
        
        return retValue;
    }
    
    public void updateParameters(PluggableTaskDTOEx dto) 
            throws FinderException {

        // update the parameters from the dto
        for (int f = 0; f < dto.getParameters().size(); f++) {
            PluggableTaskParameterDTOEx parameter = 
                    (PluggableTaskParameterDTOEx) dto.getParameters().get(f);
            
            updateParameter(parameter); 
        }
    }
    
    public PluggableTaskParameterDTOEx getParameterDTO(
            PluggableTaskParameterEntityLocal parameter) {
        PluggableTaskParameterDTOEx retValue = new 
                PluggableTaskParameterDTOEx();
        
        retValue.setFloatValue(parameter.getFloatValue());
        retValue.setId(parameter.getId());
        retValue.setIntValue(parameter.getIntValue());
        retValue.setName(parameter.getName());
        retValue.setStrValue(parameter.getStrValue());
        retValue.populateValue();
        
        return retValue;
    }        
    
    private void updateParameter(PluggableTaskParameterDTOEx dto) 
            throws FinderException {
        PluggableTaskParameterEntityLocal parameter = 
                pluggableTaskParameterHome.findByPrimaryKey(dto.getId());
    
        dto.expandValue();
        
        parameter.setFloatValue(dto.getFloatValue());
        parameter.setIntValue(dto.getIntValue());
        parameter.setName(dto.getName());
        parameter.setStrValue(dto.getStrValue());
    }     
}
