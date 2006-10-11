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

package com.sapienter.jbilling.client.process;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.client.item.CurrencyArrayWrap;
import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.server.list.ListSession;
import com.sapienter.jbilling.server.list.ListSessionHome;


/**
 * 
 * @author Justin
 *
 */
public class TriggerHelperServlet extends HttpServlet {
	
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
        
        Logger log = Logger.getLogger(TriggerHelperServlet.class);
		
        // this initializes the cron service, that takes care of 
        // periodically run the billing and other batch process
		Trigger.Initialize();
        
        // initialize the currencies, which are in application scope
        ServletContext context = config.getServletContext();
        log.debug("Loadding application currency symbols");
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
        
    }
}
