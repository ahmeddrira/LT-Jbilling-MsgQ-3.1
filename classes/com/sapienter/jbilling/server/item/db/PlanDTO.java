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

package com.sapienter.jbilling.server.item.db;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @author Brian Cowdery
 * @since 26-08-2010
 */
@Entity
@Table(name = "plan")
@TableGenerator(
        name = "plan_GEN",
        table = "jbilling_seqs",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue = "plan",
        allocationSize = 100
)
@NamedQueries({
        @NamedQuery(name = "PlanDTO.findByPlanItem",
                    query = "select plan from PlanDTO plan where plan.item.id = :plan_item_id"),

        @NamedQuery(name = "CustomerDTO.findCustomersByPlan",
                    query = "select user.customer"
                            + " from OrderLineDTO line "
                            + " inner join line.item.plans as plan "
                            + " inner join line.purchaseOrder.baseUserByUserId as user"
                            + " where plan.id = :plan_id"
                            + " and line.purchaseOrder.orderPeriod.id != 1 " // Constants.ORDER_PERIOD_ONCE
                            + " and line.purchaseOrder.orderStatus.id = 1 "  // Constants.ORDER_STATUS_ACTIVE
                            + " and line.purchaseOrder.deleted = 0"),

        @NamedQuery(name = "PlanDTO.isSubscribed",
                    query = "select line.id"
                            + " from OrderLineDTO line "
                            + " inner join line.item.plans as plan "
                            + " inner join line.purchaseOrder.baseUserByUserId as user "
                            + " where plan.id = :plan_id "
                            + " and user.id = :user_id "
                            + " and line.purchaseOrder.orderPeriod.id != 1 " // Constants.ORDER_PERIOD_ONCE
                            + " and line.purchaseOrder.orderStatus.id = 1 "  // Constants.ORDER_STATUS_ACTIVE
                            + " and line.purchaseOrder.deleted = 0"),

        // todo: include bundled items as "affected"
        @NamedQuery(name = "PlanDTO.findByAffectedItem",
                    query = "select plan "
                            + " from PlanDTO plan "
                            + " inner join plan.planItems planItems "
                            + " where planItems.item.id = :affected_item_id")
})
// todo: cache config
public class PlanDTO implements Serializable {

    private Integer id;
    private ItemDTO item;
    private String description;
    private List<PlanItemDTO> planItems;

    public PlanDTO() {
    }

    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "plan_GEN")
    @Column(name = "id", nullable = false, unique = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    /**
     * Returns the plan subscription item.
     * Syntax sugar, alias for {@link #getItem()}
     * @return plan subscription item
     */
    @Transient
    public ItemDTO getPlanSubscriptionItem() {
        return getItem();
    }

    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plan")
    public List<PlanItemDTO> getPlanItems() {
        return planItems;
    }

    public void setPlanItems(List<PlanItemDTO> planItems) {
        this.planItems = planItems;
    }

    public void addPlanItem(PlanItemDTO planItem) {
        this.planItems.add(planItem);
    }

    @Override
    public String toString() {
        return "PlanDTO{"
               + "id=" + id
               + ", item=" + item
               + ", description='" + description + '\''
               + ", planItems=" + planItems
               + '}';
    }
}
