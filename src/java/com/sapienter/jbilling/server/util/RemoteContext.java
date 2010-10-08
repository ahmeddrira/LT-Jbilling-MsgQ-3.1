/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sapienter.jbilling.server.util;

import java.util.EnumMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RemoteContext {

    private static final ApplicationContext spring = 
            new ClassPathXmlApplicationContext( new String[] {
            "/jbilling-remote-beans.xml" });
    
    public enum Name {
        API_CLIENT,
        API_CLIENT_2,
        API_CLIENT_3,
        SPRING_SECURITY_SERVICE
    }
    
    private static final Map<Name, String> springBeans = new EnumMap<Name, String>(Name.class);
    
    // all the managed beans
    static {
        // remote session beans
        springBeans.put(Name.API_CLIENT, "apiClient");
        springBeans.put(Name.API_CLIENT_2, "apiClient2");
        springBeans.put(Name.API_CLIENT_3, "apiClient3");
        springBeans.put(Name.SPRING_SECURITY_SERVICE, "springSecurityService");
    }
    
    // should not be instantiated
    private RemoteContext() {
    }
    
    public static Object getBean(Name bean) {
        return spring.getBean(springBeans.get(bean));
    }
}
