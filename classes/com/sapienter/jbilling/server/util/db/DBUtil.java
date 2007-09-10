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
package com.sapienter.jbilling.server.util.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public final class DBUtil {
    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("jbilling"); // use "jbilling-jta" for JTA
    
    private static ThreadLocal<EntityManager> tlem = 
        new ThreadLocal<EntityManager>();

    public static EntityManager getEntityManager() {
        EntityManager retValue = tlem.get();
        if (retValue == null) {
            retValue = emf.createEntityManager();
            tlem.set(retValue);
        }
        
        return retValue;
    }
    
    public static void finishSession() {
       EntityManager myEM = tlem.get();
       if (myEM != null) {
           myEM.close();
           tlem.remove();
       }
    }
    
    public static EntityTransaction getTransaction() {
        return getEntityManager().getTransaction();
        //return new NoTransaction();
        //return new FlushOnlyTransaction();
    }
}
