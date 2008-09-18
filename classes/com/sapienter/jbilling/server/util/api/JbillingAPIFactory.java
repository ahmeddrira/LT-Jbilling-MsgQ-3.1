/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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
package com.sapienter.jbilling.server.util.api;

import java.io.IOException;
import java.util.Properties;

public final class JbillingAPIFactory {
    static private JbillingAPI api = null;
    static private Properties config = null;
    
    private JbillingAPIFactory() {}; // a factory should not be instantiated
    
    static public JbillingAPI getAPI() 
            throws JbillingAPIException, IOException {
    	if (api == null) {
            String propFile = System.getProperty("JBILLING_PROPERTIES_FILE", "/jbilling_api.properties");
            config = new Properties();
            config.load(JbillingAPI.class.getResourceAsStream(propFile));

            String default_api = config.getProperty("default", "axis");
            // check for AXIS
            if (default_api.compareToIgnoreCase("axis") == 0) {
                String userName = config.getProperty("user_name");
                String password = config.getProperty("password");
                String endPoint = config.getProperty("end_point");
                if (userName == null || password == null || endPoint == null) {
                    throw new JbillingAPIException("properties user_name " +
                            "password end_point are required for AXIS");
                }
                String timeOutStr = config.getProperty("timeout");
                int timeOut = 30;
                if (timeOutStr != null && timeOutStr.length() > 0) {
                    timeOut = Integer.valueOf(timeOutStr);
                }
                timeOut *= 1000; // convert to mills
                api =  new AxisAPI(userName,password,endPoint,timeOut );
            } else if (default_api.compareToIgnoreCase("ejb") == 0) {
                String userName = config.getProperty("user_name");
                String password = config.getProperty("password");
                String provider_url = config.getProperty("provider_url");
                if (userName == null || password == null || provider_url == null) {
                    throw new JbillingAPIException("properties user_name [" + userName +
                            "] password [" + password + 
                            "] provider_url [" + provider_url + 
                            "] are required for EJB");
                }
                api =  new EJBAPI(provider_url, userName, password);
            } else {
                throw new JbillingAPIException("api [" + default_api + "] is not supported");
            }
        }
    	return api;
    }
}
