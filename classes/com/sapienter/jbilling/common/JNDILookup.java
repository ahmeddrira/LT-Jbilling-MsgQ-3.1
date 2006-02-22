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

package com.sapienter.jbilling.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * EJB Home Factory, maintains a simple hashmap cache of EJBHomes
 * For a production implementations, exceptions such as NamingException
 * can be wrapped with a factory exception to futher simplify
 * the client.
 */
public class JNDILookup {

    private Map ejbHomes;
    private static final String DATABASE_JNDI = "java:/ApplicationDS";
    // this is then custom treated for serialization
    private static Logger log = null;
    // this one is always checked for null
    private transient static JNDILookup aFactorySingleton = null;
    private transient Context ctx = null;

    /**
     * EJBHomeFactory private constructor.
     */
    private JNDILookup(boolean test) throws NamingException {
        log = Logger.getLogger(JNDILookup.class);
        if (test) {
            Hashtable env = new Hashtable();
            env.put(
                Context.INITIAL_CONTEXT_FACTORY,
                "org.jnp.interfaces.NamingContextFactory");
            env.put(
                Context.URL_PKG_PREFIXES,
                "org.jboss.naming:org.jnp.interfaces");
            env.put(Context.PROVIDER_URL, "localhost");
            ctx = new InitialContext(env);
            log.info("Context set with environment.");
        } else {
            ctx = new InitialContext();
            log.info("Default Context set");
        }
        this.ejbHomes = Collections.synchronizedMap(new HashMap());
    }
    
    public static JNDILookup getFactory(boolean test)
        throws NamingException {

        if (JNDILookup.aFactorySingleton == null) {
            JNDILookup.aFactorySingleton = new JNDILookup(test);
            log.info("New EJBFactory created.");
        }

        return JNDILookup.aFactorySingleton;
    }

    /*
     * Returns the singleton instance of the EJBHomeFactory
     * The sychronized keyword is intentionally left out the
     * as I don't think the potential to intialize the singleton
     * twice at startup time (which is not a destructive event)
     * is worth creating a sychronization bottleneck on this
     * VERY frequently used class, for the lifetime of the
     * client application.
     */
    public static JNDILookup getFactory() throws NamingException {
        return getFactory(false);
    }
    
   /**
     * Lookup and cache an EJBHome object.
     * This 'alternate' implementation delegates JNDI name knowledge
     * to the client. It is included here for example only.
     */
    public EJBHome lookUpHome(Class homeClass, String jndiName)
        throws ClassCastException, NamingException {

        EJBHome anEJBHome;

        anEJBHome = (EJBHome) this.ejbHomes.get(jndiName);

        if (anEJBHome == null) {
            //log.info("finding Home " + jndiName + " for first time");
            anEJBHome =
                (EJBHome) PortableRemoteObject.narrow(
                    ctx.lookup(jndiName),
                    homeClass);
            this.ejbHomes.put(jndiName, anEJBHome);
        }

        return anEJBHome;

    }

    public EJBLocalHome lookUpLocalHome(Class homeClass, String jndiName)
        throws ClassCastException, NamingException {

        EJBLocalHome anEJBLocalHome;

        anEJBLocalHome = (EJBLocalHome) this.ejbHomes.get(jndiName);

        if (anEJBLocalHome == null) {
            //log.info("finding Local Home " + jndiName + " for first time");
            anEJBLocalHome =
                (EJBLocalHome) PortableRemoteObject.narrow(
                    ctx.lookup(jndiName),
                    homeClass);
            this.ejbHomes.put(jndiName, anEJBLocalHome);
        }

        return anEJBLocalHome;

    }
    
    public DataSource lookUpDataSource() throws NamingException {
        DataSource jdbc;
        
        jdbc = (DataSource) ejbHomes.get(DATABASE_JNDI);
        if (jdbc == null) {
            log.info("Looking up the datasource for the first time." + DATABASE_JNDI);
            jdbc = (DataSource) PortableRemoteObject.narrow(
            		ctx.lookup(DATABASE_JNDI), DataSource.class);
            ejbHomes.put(DATABASE_JNDI, jdbc);
        }
        
        return jdbc;
    }

}
