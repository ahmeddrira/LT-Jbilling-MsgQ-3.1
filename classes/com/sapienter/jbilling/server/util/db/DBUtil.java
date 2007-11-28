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
