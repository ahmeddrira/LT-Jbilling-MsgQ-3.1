/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.order.db;

import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.util.Context;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import javax.persistence.ColumnResult;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import java.util.Date;

/**
 * @author Brian Cowdery
 * @since 16-08-2010
 */
public class UsageDAS extends HibernateDaoSupport {

    public UsageDAS() {
        setSessionFactory((SessionFactory) Context.getBean(Context.Name.HIBERNATE_SESSION));
    }

    private static final String USAGE_BY_ITEM_ID_SQL =
            "select "
            + " sum(amount) as amount, "
            + " sum(quantity) as quantity "
            + "from order_line "
            + "where "
            + " deleted = 0 "
            + " and item_id = :item_id "
            + " and create_datetime between :start_date and :end_date";

    public Usage findUsageByItem(Integer itemId, Date startDate, Date endDate) {
        Query query = getSession().createSQLQuery(USAGE_BY_ITEM_ID_SQL);
        query.setParameter("item_id", itemId);
        query.setParameter("start_date", startDate);
        query.setParameter("end_date", endDate);

        query.setResultTransformer(Transformers.aliasToBean(Usage.class));

        Usage usage = (Usage) query.uniqueResult();
        usage.setItemId(itemId);
        usage.setStartDate(startDate);
        usage.setEndDate(endDate);

        return usage;
    }

    private static final String USAGE_BY_ITEM_TYPE_SQL =
            "select "
            + " sum(ol.amount) as amount, "
            + " sum(ol.quantity) as quantity "
            + "from "
            + " order_line ol "
            + " join item_type_map tm on tm.item_id = ol.item_id "
            + "where "
            + " ol.deleted = 0 "
            + " and tm.type_id = :item_type_id"
            + " and create_datetime between :start_date and :end_date";

    public Usage findUsageByItemType(Integer itemTypeId, Date startDate, Date endDate) {
        Query query = getSession().createSQLQuery(USAGE_BY_ITEM_TYPE_SQL);
        query.setParameter("item_type_id", itemTypeId);
        query.setParameter("start_date", startDate);
        query.setParameter("end_date", endDate);

        query.setResultTransformer(Transformers.aliasToBean(Usage.class));

        Usage usage = (Usage) query.uniqueResult();
        usage.setItemId(itemTypeId);
        usage.setStartDate(startDate);
        usage.setEndDate(endDate);

        return usage;
    }
}
