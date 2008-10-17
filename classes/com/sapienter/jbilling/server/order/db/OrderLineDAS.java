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
package com.sapienter.jbilling.server.order.db;

import org.hibernate.Query;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class OrderLineDAS extends AbstractDAS<OrderLineDTO> {
    public Long findLinesWithDecimals(Integer itemId) {

        final String hql =
            "select count(*)" + 
            "  from OrderLineDTO ol " +
            " where ol.deleted = 0 " + 
            "   and ol.item.id= :item and (ol.quantity - cast(ol.quantity as integer)) <> 0";

        Query query = getSession().createQuery(hql);
        query.setParameter("item", itemId);

        return (Long) query.uniqueResult();
    }

}
