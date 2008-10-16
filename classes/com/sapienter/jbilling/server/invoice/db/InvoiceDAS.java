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

package com.sapienter.jbilling.server.invoice.db;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class InvoiceDAS extends AbstractDAS<Invoice> {
    // used for the web services call to get the latest X 
    public List<Integer> findIdsByUserLatestFirst(Integer userId, int maxResults) {
        Criteria criteria = getSession().createCriteria(Invoice.class)
                .add(Restrictions.eq("deleted", 0))
                .createAlias("baseUser", "u")
                    .add(Restrictions.eq("u.id", userId))
                .setProjection(Projections.id())
                .addOrder(Order.desc("id"))
                .setMaxResults(maxResults);
        return criteria.list();
    }

    public Double findTotalForPeriod(Integer userId, Date start, Date end) {
        Criteria criteria = getSession().createCriteria(Invoice.class);
        addUserCriteria(criteria, userId);
        addPeriodCriteria(criteria, start, end);
        criteria.setProjection(Projections.sum("total"));
        return (Double) criteria.uniqueResult();
    }

    public Double findAmountForPeriodByItem(Integer userId, Integer itemId, Date start, Date end) {
        Criteria criteria = getSession().createCriteria(Invoice.class);
        addUserCriteria(criteria, userId);
        addPeriodCriteria(criteria, start, end);
        addItemCriteria(criteria, itemId);
        criteria.setProjection(Projections.sum("invoiceLines.amount"));
        return (Double) criteria.uniqueResult();
    }

    public Double findQuantityForPeriodByItem(Integer userId, Integer itemId, Date start, Date end) {
        Criteria criteria = getSession().createCriteria(Invoice.class);
        addUserCriteria(criteria, userId);
        addPeriodCriteria(criteria, start, end);
        addItemCriteria(criteria, itemId);
        criteria.setProjection(Projections.sum("invoiceLines.quantity"));
        return (Double) criteria.uniqueResult();
    }

    public Integer findLinesForPeriodByItem(Integer userId, Integer itemId, Date start, Date end) {
        Criteria criteria = getSession().createCriteria(Invoice.class);
        addUserCriteria(criteria, userId);
        addPeriodCriteria(criteria, start, end);
        addItemCriteria(criteria, itemId);
        criteria.setProjection(Projections.count("id"));
        return (Integer) criteria.uniqueResult();
    }

    public Double findAmountForPeriodByItemCategory(Integer userId, Integer categoryId, Date start, Date end) {
        Criteria criteria = getSession().createCriteria(Invoice.class);
        addUserCriteria(criteria, userId);
        addPeriodCriteria(criteria, start, end);
        addItemCategoryCriteria(criteria, categoryId);
        criteria.setProjection(Projections.sum("invoiceLines.amount"));
        return (Double) criteria.uniqueResult();
    }

    public Double findQuantityForPeriodByItemCategory(Integer userId, Integer categoryId, Date start, Date end) {
        Criteria criteria = getSession().createCriteria(Invoice.class);
        addUserCriteria(criteria, userId);
        addPeriodCriteria(criteria, start, end);
        addItemCategoryCriteria(criteria, categoryId);
        criteria.setProjection(Projections.sum("invoiceLines.quantity"));
        return (Double) criteria.uniqueResult();
    }

    public Integer findLinesForPeriodByItemCategory(Integer userId, Integer categoryId, Date start, Date end) {
        Criteria criteria = getSession().createCriteria(Invoice.class);
        addUserCriteria(criteria, userId);
        addPeriodCriteria(criteria, start, end);
        addItemCategoryCriteria(criteria, categoryId);
        criteria.setProjection(Projections.count("id"));
        return (Integer) criteria.uniqueResult();
    }

    private void addUserCriteria(Criteria criteria, Integer userId) {
        criteria
            .add(Restrictions.eq("deleted", 0))
            .createAlias("baseUser", "u")
            .add(Restrictions.eq("u.id", userId));
    }

    private void addPeriodCriteria(Criteria criteria, Date start, Date end) {
        criteria
            .add(Restrictions.ge("createDatetime", start))
            .add(Restrictions.lt("createDatetime", end));
    }

    private void addItemCriteria(Criteria criteria, Integer itemId) {
        criteria
            .createAlias("invoiceLines", "invoiceLines")
            .add(Restrictions.eq("invoiceLines.item.id", itemId));
    }

    private void addItemCategoryCriteria(Criteria criteria, Integer categoryId) {
        criteria
            .createAlias("invoiceLines", "invoiceLines")
            .createAlias("invoiceLines.item", "item")
            .createAlias("item.itemTypes", "itemTypes")
            .add(Restrictions.eq("itemTypes.id", categoryId));
    }
}
