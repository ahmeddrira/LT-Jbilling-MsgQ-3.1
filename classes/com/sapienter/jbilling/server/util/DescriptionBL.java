/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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
