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

package com.sapienter.jbilling.server.user.db;

import com.sapienter.jbilling.server.item.db.PlanItemDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Customer pricing mapping class. Provides a list of prices for each customer. New prices
 * are inserted when a customer subscribes to a configured plan.
 *
 * @author Brian Cowdery
 * @since 26-08-2010
 */
@Entity
@Table(name = "customer_price_dto")
@NamedQueries({
        @NamedQuery(name = "PlanItemDTO.findAllCustomerSpecificPrices",
                    query = "select price.planItem"
                            + " from CustomerPriceDTO price "
                            + " where price.planItem.plan is null "
                            + " and price.customer.baseUser.id = :user_id"),

        @NamedQuery(name = "CustomerDTO.findCustomersByPlan",
                    query = "select user.customer"
                            + " from OrderLineDTO line "
                            + " inner join line.item.plans as plan "
                            + " inner join line.purchaseOrder.user as user"
                            + " where plan.id = :plan_id"
                            + " and line.purchaseOrder.orderPeriod.id != 1 " // Constants.ORDER_PERIOD_ONCE
                            + " and line.purchaseOrder.orderStatus.id = 1 "  // Constants.ORDER_STATUS_ACTIVE
                            + " and line.purchaseOrder.deleted = 0"),

        @NamedQuery(name = "CustomerPriceDTO.deletePrice",
                    query = "delete price "
                            + " from CustomerPriceDTO price "
                            + " where price.planItem.id = :plan_item_id "
                            + " and price.customer.baseUser.id = :user_id"),

        @NamedQuery(name = "CustomerPriceDTO.deletePriceByPlan",
                    query = "delete price "
                            + " from CustomerPriceDTO price "
                            + " where price.planItem.plan.id = :plan_id"
                            + " and price.customer.baseUser.id = :user_id")
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "PlanItemDTO.findCustomerPrice",
                          query = "select p.*"
                                  + " from plan_item p "
                                  + " join customer_price cp on cp.plan_item_id = p.id "
                                  + " where p.item_id = :item_id "
                                  + " and cp.user_id = :user_id "
                                  + " order by p.precedence, cp.create_datetime desc",
                          resultSetMapping = "PlanItemDTOResultSetMapping")
})
@SqlResultSetMapping(name = "PlanItemDTOResultSetMapping",
                     entities = @EntityResult(entityClass = PlanItemDTO.class))
public class CustomerPriceDTO implements Serializable {

    private PlanItemDTO planItem;
    private CustomerDTO customer;
    private Date createDatetime = new Date();

    public CustomerPriceDTO() {
    }

    /**
     * Returns the affected item and price
     *
     * @return affected item and price
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_item_id")
    public PlanItemDTO getPlanItem() {
        return planItem;
    }

    public void setPlanItem(PlanItemDTO planItem) {
        this.planItem = planItem;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id") // join through user instead of customer id
    public CustomerDTO getCustomer() {                              // as all pricing is performed by user
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_datetime")
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}
