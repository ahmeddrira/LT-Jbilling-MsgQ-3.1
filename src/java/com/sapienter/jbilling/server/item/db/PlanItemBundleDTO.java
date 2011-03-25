/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.item.db;

import com.sapienter.jbilling.server.item.PlanItemBundleWS;
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * BundledItemDTO
 *
 *      How many items to add
 *      Order period for bundled items
 *      Account target for where the bundled items are to be added (self, parent)
 *      Add if exists.
 *
 *
 *
 * @author Brian Cowdery
 * @since 24/03/11
 */
@Entity
@Table(name = "plan_item_bundle")
@TableGenerator(
        name = "plan_item_bundle_GEN",
        table = "jbilling_seqs",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue = "plan_item_bundle",
        allocationSize = 100
)
// todo: cache config
public class PlanItemBundleDTO implements Serializable {

    public static enum Customer {
        /** Add bundled items to the customer subscribing to the plan. */
        SELF,
        /** Add bundled items to the billable account, can be a parent account. */
        BILLABLE,
    }

    private Integer id;
    private BigDecimal quantity;
    private OrderPeriodDTO period;
    private Customer targetCustomer = Customer.SELF;
    private boolean addIfExists = true;

    public PlanItemBundleDTO() {
    }

    public PlanItemBundleDTO(PlanItemBundleWS ws, OrderPeriodDTO period) {
        this.id = ws.getId();
        this.quantity = ws.getQuantityAsDecimal();
        this.period = period;
        this.addIfExists = ws.addIfExists();

        if (ws.getTargetCustomer() != null) this.targetCustomer = Customer.valueOf(ws.getTargetCustomer());
    }

    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "plan_item_bundle_GEN")
    @Column(name = "id", nullable = false, unique = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "quantity", nullable = false, scale = 22, precision = 10)
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * The period to use for the order containing the bundled quantity. If the period is different than
     * the order used to subscribe the customer to the plan, then a new order will be created to contain
     * the bundled items.
     *
     * @return order period for the bundled quantity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    public OrderPeriodDTO getPeriod() {
        return period;
    }

    public void setPeriod(OrderPeriodDTO period) {
        this.period = period;
    }

    /**
     * The target customer account to add the bundled items.
     *
     * @return target customer account for bundled items.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "target_customer", nullable = false)
    public Customer getTargetCustomer() {
        return targetCustomer;
    }

    public void setTargetCustomer(Customer targetCustomer) {
        this.targetCustomer = targetCustomer;
    }

    /**
     * If true, the bundled item will be added regardless of whether the customer already
     * has an order containing the item to be added.
     *
     * If false, the bundled item will only ever be added if the customer does not have
     * an order containing the item.
     *
     * @return true to always add item, false to add only if item does not already exist.
     */
    @Column(name = "add_if_exists", nullable = false)
    public boolean getAddIfExists() {
        return addIfExists;
    }

    public void setAddIfExists(boolean addIfExists) {
        this.addIfExists = addIfExists;
    }

    public boolean addIfExists() {
        return addIfExists;
    }

    @Override
    public String toString() {
        return "PlanItemBundleDTO{"
               + "id=" + id
               + ", quantity=" + quantity
               + ", periodId=" + (period != null ? period.getId() : null)
               + ", targetCustomer=" + targetCustomer
               + ", addIfExists=" + addIfExists
               + '}';
    }
}
