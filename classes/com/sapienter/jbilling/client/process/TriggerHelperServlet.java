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

package com.sapienter.jbilling.client.process;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.client.item.CurrencyArrayWrap;
import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.list.ListSession;
import com.sapienter.jbilling.server.list.ListSessionHome;


/**
 * 
 * @author Justin
 *
 */
public class TriggerHelperServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(TriggerHelperServlet.class);

	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
        
        
        // validate that the java version is correct
        validateJava();
		
        // this initializes the cron service, that takes care of 
        // periodically run the billing and other batch process
		Trigger.Initialize();
        
        // initialize the currencies, which are in application scope
        ServletContext context = config.getServletContext();
        LOG.debug("Loadding application currency symbols");
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);            
            ListSessionHome listHome =
                    (ListSessionHome) EJBFactory.lookUpHome(
                    ListSessionHome.class,
                    ListSessionHome.JNDI_NAME);

            ListSession myRemoteSession = listHome.create();
            context.setAttribute(Constants.APP_CURRENCY_SYMBOLS, 
                    new CurrencyArrayWrap(
                            myRemoteSession.getCurrencySymbolsMap()));
        } catch (Exception e) {
            throw new ServletException(e);
        }
        
        // request a table to force the load
        Util.getTableId("entity");
    }
    
    private void validateJava() {
        Float version = Float.valueOf(System.getProperty("java.version").substring(0, 3));
        if (version < 1.6F) {
            // can't run!
           LOG.fatal("*********************************************************");
           LOG.fatal("You need Java version 1.6 or higher to run jbilling. " +
                "Your current version is " + version);
           LOG.fatal("*********************************************************"); 
           System.exit(1);
        }
        
        if (!System.getProperty("java.vendor").matches(".*Sun.*")) {
            LOG.warn("Your java vendor is not Sun. Results are unpredicatble");
        }
    }
}
