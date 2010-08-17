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

package com.sapienter.jbilling.server.pricing.db;

import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.pricing.PriceModelWS;
import com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy;
import com.sapienter.jbilling.server.pricing.strategy.PricingStrategy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.MapKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Cowdery
 * @since 30-07-2010
 */
@Entity
@Table(name = "price_model")
@TableGenerator(
        name = "price_model_GEN",
        table = "jbilling_seqs",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue = "price_model",
        allocationSize = 100
)
@NamedQueries({
        @NamedQuery(name = "PriceModelDTO.findDefaultPricing",
                    query = "select price from PriceModelDTO price where price.defaultPricing = true"),

        @NamedQuery(name = "PriceModelDTO.findByType",
                    query = "select price from PriceModelDTO price where price.type = :type"),

        @NamedQuery(name = "PriceModelDTO.findByPlanItemId",
                    query = "select price from PriceModelDTO price where price.planItem.id = :plan_item_id"),

        @NamedQuery(name = "PriceModelDTO.findbyPlanItemIds",
                    query = "select price from PriceModelDTO price " +
                            "where price.planItem.id in (:plan_item_ids) " +
                            "or price.defaultPricing = true")

})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PriceModelDTO implements Serializable {

    public static final ItemDTO DEFAULT_PLAN_ITEM = null; // default pricing doesn't have an item
    public static final Integer DEFAULT_PRECEDENCE = -1;
    public static final String ATTRIBUTE_WILDCARD = "*";

    private Integer id;
    private PriceModelStrategy type;
    private Map<String, String> attributes = new HashMap<String, String>();
    private ItemDTO planItem;
    private Integer planItemId; // read only field, doesn't use lazy-loaded planItem
    private Integer precedence;
    private BigDecimal rate;
    private BigDecimal includedQuantity;
    private boolean defaultPricing = false;

    public PriceModelDTO() {
    }

    public PriceModelDTO(PriceModelWS ws, ItemDTO planItem) {
        setId(ws.getId());
        setType(PriceModelStrategy.valueOf(ws.getType()));
        setAttributes(new HashMap<String,String>(ws.getAttributes()));
        setPlanItem(planItem);
        setPrecedence(ws.getPrecedence());
        setRate(ws.getRate());
        setIncludedQuantity(ws.getIncludedQuantity());
        setDefaultPricing(ws.isDefaultPricing());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "price_model_GEN")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "strategy_type", nullable = false, length = 25)
    public PriceModelStrategy getType() {
        return type;
    }

    public void setType(PriceModelStrategy type) {
        this.type = type;        
    }

    @Transient
    public PricingStrategy getStrategy() {
        return getType() != null ? getType().getStrategy() : null;
    }

    @CollectionOfElements(fetch = FetchType.EAGER)
    @JoinTable(name = "price_model_attribute", joinColumns = @JoinColumn(name = "price_model_id"))
    @MapKey(columns = @Column(name = "attribute_name", nullable = true, length = 255))
    @Column(name = "attribute_value", nullable = true, length = 255)
    @Fetch(FetchMode.SELECT)
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        setAttributeWildcards();
    }

    /**
     * Sets the given attribute. If the attribute is null, it will be persisted as a wildcard "*".
     *
     * @param name attribute name
     * @param value attribute value
     */
    public void addAttribute(String name, String value) {
        this.attributes.put(name, (value != null ? value : ATTRIBUTE_WILDCARD));
    }

    /**
     * Replaces null values in the attribute list with a wildcard character. Null values cannot be
     * persisted using the @CollectionOfElements, and make for uglier 'optional' attribute queries.
     */
    public void setAttributeWildcards() {
        if (getAttributes() != null && !getAttributes().isEmpty()) {
            for (Map.Entry<String, String> entry : getAttributes().entrySet())
                if (entry.getValue() == null)
                    entry.setValue(ATTRIBUTE_WILDCARD);
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_item_id")
    public ItemDTO getPlanItem() {
        return planItem;
    }

    public void setPlanItem(ItemDTO planItem) {
        this.planItem = planItem;
    }

    /**
     * Returns the plan item id. This is a read-only field cannot be updated.
     * Use {@link #getPlanItem()} and {@link #setPlanItem(com.sapienter.jbilling.server.item.db.ItemDTO)}
     * to update.
     *
     * Use to retrieve the plan item id without lazy-loading the ItemDTO association.
     *
     * @return plan item id.
     */
    @Column(name = "plan_item_id", updatable = false, insertable = false)
    public Integer getPlanItemId() {
        return planItemId;
    }

    public void setPlanItemId(Integer planItemId) {
        this.planItemId = planItemId;
    }

    @Column(name = "precedence", nullable = false, length = 1)
    public Integer getPrecedence() {
        return precedence;
    }

    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    /**
     * Returns the pricing rate. If the strategy type defines an overriding rate, the
     * strategy rate will be returned.
     *
     * @see com.sapienter.jbilling.server.pricing.strategy.PricingStrategy#hasRate()
     * @see com.sapienter.jbilling.server.pricing.strategy.PricingStrategy#getRate()
     *
     * @return pricing rate.
     */
    @Column(name = "rate", nullable = false, precision = 10, scale = 22)
    public BigDecimal getRate() {
        return getStrategy() != null && getStrategy().hasRate() ? getStrategy().getRate() : rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * Returns the pricing included quantity if using a graduated strategy type, if the set
     * pricing strategy is not a graduated strategy, this method always returns zero.
     *
     * @see com.sapienter.jbilling.server.pricing.strategy.PricingStrategy#isGraduated()
     *
     * @return included quantity if strategy is graduated, zero if not.
     */
    @Column(name = "included_quantity", precision = 10, scale = 22)
    public BigDecimal getIncludedQuantity() {        
        return getStrategy() != null && getStrategy().isGraduated() ? includedQuantity : BigDecimal.ZERO;
    }

    public void setIncludedQuantity(BigDecimal includedQuantity) {
        this.includedQuantity = includedQuantity;
    }

    /**
     * Returns true if this is a default pricing model.
     *
     * @return true if default pricing model.
     */
    @Column(name = "is_default", nullable = false)
    public boolean isDefaultPricing() {
        return defaultPricing;
    }

    /**
     * Set this price to be the default for this type of purchase.
     *
     * Default pricing will be used when the customer is not subscribed
     * to any other plan. Default pricing has a precedence of zero and no
     * plan item.
     *
     * @param defaultPricing true if this plan price should be the default
     */
    public void setDefaultPricing(boolean defaultPricing) {
        this.defaultPricing = defaultPricing;
        if (defaultPricing) {
            setPlanItem(DEFAULT_PLAN_ITEM);
            setPrecedence(DEFAULT_PRECEDENCE);
        }
    }

    /**
     * Applies this pricing to the given PricingResult. This method is intended to be invoked
     * by the rules engine to apply pricing.
     *
     * @see com.sapienter.jbilling.server.pricing.strategy.PricingStrategy
     *
     * @param result pricing result to apply pricing to
     * @param quantity quantity of item being priced
     * @param usage total item usage for this billing period
     */
    @Transient
    public void applyTo(PricingResult result, BigDecimal quantity, BigDecimal usage) {
        getType().getStrategy().applyTo(result, this, quantity, usage);
    }

    @Override
    public String toString() {
        return "PriceModelDTO{"
                + "id=" + id
                + ", type=" + type
                + ", attributes=" + attributes
                + ", planItemId=" + planItemId
                + ", precedence=" + precedence
                + ", rate=" + rate
                + ", includedQuantity=" + includedQuantity
                + ", defaultPricing=" + defaultPricing
                + '}';
    }
}
