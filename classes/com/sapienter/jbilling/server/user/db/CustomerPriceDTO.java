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
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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
@Table(name = "customer_price")
@NamedQueries({
        @NamedQuery(name = "PlanItemDTO.findAllCustomerSpecificPrices",
                    query = "select price.id.planItem"
                            + " from CustomerPriceDTO price "
                            + " where price.id.planItem.plan is null "
                            + " and price.id.baseUser.id = :user_id"),

        @NamedQuery(name = "CustomerDTO.findCustomersByPlan",
                    query = "select user.customer"
                            + " from OrderLineDTO line "
                            + " inner join line.item.plans as plan "
                            + " inner join line.purchaseOrder.baseUserByUserId as user"
                            + " where plan.id = :plan_id"
                            + " and line.purchaseOrder.orderPeriod.id != 1 " // Constants.ORDER_PERIOD_ONCE
                            + " and line.purchaseOrder.orderStatus.id = 1 "  // Constants.ORDER_STATUS_ACTIVE
                            + " and line.purchaseOrder.deleted = 0"),

        @NamedQuery(name = "CustomerPriceDTO.deletePrice",
                    query = "delete CustomerPriceDTO "
                            + " where id.planItem.id = :plan_item_id "
                            + " and id.baseUser.id = :user_id"),

        @NamedQuery(name = "CustomerPriceDTO.deletePriceByPlan",
                    query = "delete CustomerPriceDTO "
                            + " where id.baseUser.id = :user_id"
                            + " and id.planItem.id in ("                // postgresql has a strange syntax for delete
                            + "     select planItem.id "                // with join that is not supported by hibernate.
                            + "     from PlanItemDTO planItem "         // delete where id in (...) as a workaround
                            + "     where planItem.plan.id = :plan_id"
                            + ")")
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
@SqlResultSetMapping(name = "PlanItemDTOResultSetMapping", entities = @EntityResult(entityClass = PlanItemDTO.class))
// todo: cache config
public class CustomerPriceDTO implements Serializable {

    private CustomerPricePK id = new CustomerPricePK();
    private Date createDatetime = new Date();

    public CustomerPriceDTO() {
    }

    public CustomerPriceDTO(CustomerPricePK id) {
        this.id = id;
    }

    @Id
    public CustomerPricePK getId() {
        return id;
    }

    public void setId(CustomerPricePK id) {
        this.id = id;
    }

    @Transient
    public PlanItemDTO getPlanItem() {
        return id.getPlanItem();
    }

    public void setPlanItem(PlanItemDTO planItem) {
        id.setPlanItem(planItem);
    }

    @Transient
    public UserDTO getBaseUser() {
        return id.getBaseUser();
    }

    public void setBaseUser(UserDTO user) {
        id.setBaseUser(user);
    }

    @Transient
    public CustomerDTO getCustomer() {
        return (id.getBaseUser() != null ? id.getBaseUser().getCustomer() : null);
    }

    public void setCustomer(CustomerDTO customer) {
        if (customer != null)
            id.setBaseUser(customer.getBaseUser());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_datetime", nullable = false)
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}
