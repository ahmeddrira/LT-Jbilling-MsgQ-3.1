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

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class OrderDAS extends AbstractDAS<OrderDTO> {
	public OrderProcessDTO findProcessByEndDate(Integer id, Date myDate) {
		return (OrderProcessDTO) getSession().createFilter(find(id).getOrderProcesses(), 
				"where this.periodEnd = :endDate").setDate("endDate", 
						Util.truncateDate(myDate)).uniqueResult();
		
	}

	/**
	 * Finds active recurring orders for a given user
	 * @param userId
	 * @return
	 */
	public List<OrderDTO> findByUserSubscriptions(Integer userId) {
		// I need to access an association, so I can't use the parent helper class
		Criteria criteria = getSession().createCriteria(OrderDTO.class)
				.createAlias("orderStatus", "s")
					.add(Restrictions.eq("s.id", Constants.ORDER_STATUS_ACTIVE))
				.add(Restrictions.eq("deleted", 0))
				.createAlias("baseUserByUserId", "u")
					.add(Restrictions.eq("u.id", userId))
				.createAlias("orderPeriod", "p")
					.add(Restrictions.ne("p.id", Constants.ORDER_PERIOD_ONCE));
		
		return criteria.list();
	}
	
	public List<OrderDTO> findByUser_Status(Integer userId,Integer statusId) {
		// I need to access an association, so I can't use the parent helper class
		Criteria criteria = getSession().createCriteria(OrderDTO.class)
				.add(Restrictions.eq("deleted", 0))
				.createAlias("baseUserByUserId", "u")
					.add(Restrictions.eq("u.id", userId))
				.createAlias("orderStatus", "s")
					.add(Restrictions.eq("s.id", statusId));
		
		return criteria.list();
	}

    // used for the web services call to get the latest X orders
    public List<Integer> findIdsByUserLatestFirst(Integer userId, int maxResults) {
        Criteria criteria = getSession().createCriteria(OrderDTO.class)
                .add(Restrictions.eq("deleted", 0))
                .createAlias("baseUserByUserId", "u")
                    .add(Restrictions.eq("u.id", userId))
                .setProjection(Projections.id())
                .addOrder(Order.desc("id"))
                .setMaxResults(maxResults);
        return criteria.list();
    }

    // used for the web services call to get the latest X orders that contain an item of a type id
    @SuppressWarnings("unchecked")
    public List<Integer> findIdsByUserAndItemTypeLatestFirst(Integer userId, Integer itemTypeId, int maxResults) {
        // I'm a HQL guy, not Criteria
        String hql = 
            "select distinct(orderObj.id)" +
            " from OrderDTO orderObj" +
            " inner join orderObj.lines line" +
            " inner join line.item.itemTypes itemType" +
            " where itemType.id = :typeId" +
            "   and orderObj.baseUserByUserId.id = :userId" +
            "   and orderObj.deleted = 0" +
            " order by orderObj.id desc";
        List<Integer> data = getSession()
                                .createQuery(hql)
                                .setParameter("userId", userId)
                                .setParameter("typeId", itemTypeId)
                                .setMaxResults(maxResults)
                                .list();
        return data;
    }

	/**
	 * @author othman
	 * @return list of active orders
	 */
	public List<OrderDTO> findToActivateOrders() {
		Date today = Util.truncateDate(new Date());
		Criteria criteria = getSession().createCriteria(OrderDTO.class);

		criteria.add(Restrictions.eq("deleted", 0));
		criteria.add(Restrictions.or(Expression.le("activeSince", today),
				Expression.isNull("activeSince")));
		criteria.add(Restrictions.or(Expression.gt("activeUntil", today),
				Expression.isNull("activeUntil")));

		return criteria.list();
	}

	/**
	 * @author othman
	 * @return list of inactive orders
	 */
	public List<OrderDTO> findToDeActiveOrders() {
		Date today = Util.truncateDate(new Date());
		Criteria criteria = getSession().createCriteria(OrderDTO.class);

		criteria.add(Restrictions.eq("deleted", 0));
		criteria.add(Restrictions.or(Expression.gt("activeSince", today),
				Expression.le("activeUntil", today)));

		return criteria.list();
	}
}
