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

package com.sapienter.jbilling.server.util;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocalHome;

public class DescriptionBL {
    JNDILookup EJBFactory = null;
    DescriptionEntityLocalHome descriptionHome;
    
    public DescriptionBL() throws NamingException {
        init(); 
    }
    
    void init() throws NamingException {
        EJBFactory = JNDILookup.getFactory(false); 
        descriptionHome =
                (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                DescriptionEntityLocalHome.class,
                DescriptionEntityLocalHome.JNDI_NAME);
        
    }
    
    public void delete(String table, Integer foreignId) 
            throws FinderException {
        Collection toDelete = descriptionHome.findByTable_Row(table, 
                foreignId);
                
        toDelete.clear(); // this would be cool if it works.
    }
}
