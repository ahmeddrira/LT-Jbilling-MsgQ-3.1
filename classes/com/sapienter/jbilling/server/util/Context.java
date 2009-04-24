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
package com.sapienter.jbilling.server.util;

import java.util.EnumMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Context {

    private static final ApplicationContext spring = 
            new ClassPathXmlApplicationContext( new String[] {"/jbilling-beans.xml", 
            "/jbilling-database.xml", "/jbilling-provisioning.xml"});
    
    public enum Name {
        ITEM_SESSION, 
        NOTIFICATION_SESSION,
        CUSTOMER_SESSION,
        LIST_SESSION,
        USER_SESSION,
        INVOICE_SESSION,
        ORDER_SESSION,
        PLUGGABLE_TASK_SESSION,
        PAYMENT_SESSION,
        MEDIATION_SESSION,
        REPORT_SESSION,
        PROVISIONING,
        VELOCITY,
        DATA_SOURCE,
        HIBERNATE_TEMPLATE,
        DESCRIPTION_DAS,
        JBILLING_TABLE_DAS,
        PLUGGABLE_TASK_DAS,
        CACHE,
        CAI
    }
    
    private static final Map<Name, String> springBeans = new EnumMap<Name, String>(Name.class);
    
    // all the managed beans
    static {
        // those that act as session facade, mostly for transaction demarcation
        springBeans.put(Name.ITEM_SESSION, "itemSession");
        springBeans.put(Name.NOTIFICATION_SESSION, "notificationSession");
        springBeans.put(Name.CUSTOMER_SESSION, "customerSession");
        springBeans.put(Name.LIST_SESSION, "listSession");
        springBeans.put(Name.USER_SESSION, "userSession");
        springBeans.put(Name.INVOICE_SESSION, "invoiceSession");
        springBeans.put(Name.ORDER_SESSION, "orderSession");
        springBeans.put(Name.PLUGGABLE_TASK_SESSION, "pluggableTaskSession");
        springBeans.put(Name.PAYMENT_SESSION, "paymentSession");
        springBeans.put(Name.MEDIATION_SESSION, "mediationSession");
        springBeans.put(Name.REPORT_SESSION, "reportSession");

        // data access service
        springBeans.put(Name.DESCRIPTION_DAS, "internationalDescriptionDAS");
        springBeans.put(Name.JBILLING_TABLE_DAS, "jbillingTableDAS");
        springBeans.put(Name.PLUGGABLE_TASK_DAS, "pluggableTaskDAS");
        
        // other simple beans
        springBeans.put(Name.PROVISIONING, "provisioning");
        springBeans.put(Name.VELOCITY, "velocityEngine");
        springBeans.put(Name.DATA_SOURCE, "dataSource");
        springBeans.put(Name.HIBERNATE_TEMPLATE, "hibernateTemplate");
        springBeans.put(Name.CACHE, "cacheProviderFacade");
        springBeans.put(Name.CAI, "cai");
    };
    
    // should not be instantiated
    private Context() {
    }
    
    public static Object getBean(Name bean) {
        return spring.getBean(springBeans.get(bean));
    }
}
