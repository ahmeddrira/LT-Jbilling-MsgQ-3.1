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

import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.pricing.PriceModelWS;
import com.sapienter.jbilling.server.pricing.strategy.PricingStrategy;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.MapKey;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PriceModelDTO implements Serializable {

    public static final String ATTRIBUTE_WILDCARD = "*";

    private Integer id;
    private PriceModelStrategy type;
    private Map<String, String> attributes = new HashMap<String, String>();
    private BigDecimal rate;
    private CurrencyDTO currency;

    // price model chaining
    private PriceModelDTO next;

    public PriceModelDTO() {
    }

    public PriceModelDTO(PriceModelWS ws, CurrencyDTO currency) {
        setId(ws.getId());
        setType(PriceModelStrategy.valueOf(ws.getType()));
        setAttributes(new HashMap<String, String>(ws.getAttributes()));
        setRate(ws.getRateAsDecimal());
        setCurrency(currency);
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

    /**
     * Expected attributes.
     *
     * @return list of expected attributes
     */
    @Transient
    public List<AttributeDefinition> getAttributeDefinitions() {
        if (getStrategy() != null)
            return getStrategy().getAttributeDefinitions();
        return null;
    }

    /**
     * Allowed positions in a chain of price models.
     *
     * @return list of chain positions
     */
    @Transient
    public List<ChainPosition> getChainPositions() {
        if (getStrategy() != null)
            return getStrategy().getChainPositions();
        return null;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    public CurrencyDTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDTO currency) {
        this.currency = currency;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "next_model_id", nullable = true)
    public PriceModelDTO getNext() {
        return next;
    }

    public void setNext(PriceModelDTO next) {
        this.next = next;
    }

    /**
     * Applies this pricing to the given PricingResult.
     *
     * This method will automatically convert the calculated price to the currency of the given
     * PricingResult if the set currencies differ.
     *
     * @see com.sapienter.jbilling.server.pricing.strategy.PricingStrategy
     *
     * @param result pricing result to apply pricing to
     * @param quantity quantity of item being priced
     * @param usage total item usage for this billing period
     */
    @Transient
    public void applyTo(PricingResult result, List<PricingField> fields, BigDecimal quantity, Usage usage) {
        PriceModelDTO model = this;
        while (model != null) {
            // apply pricing
            model.getType().getStrategy().applyTo(result, fields, model, quantity, usage);

            // convert currency if necessary
            if (result.getUserId() != null
                && result.getCurrencyId() != null
                && model.getCurrency() != null
                && model.getCurrency().getId() != result.getCurrencyId()) {

                Integer entityId = new UserBL().getEntityId(result.getUserId());
                result.setPrice(new CurrencyBL().convert(model.getCurrency().getId(), result.getCurrencyId(), result.getPrice(), entityId));
            }

            // next price model in chain
            model = model.getNext();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceModelDTO that = (PriceModelDTO) o;

        if (attributes != null ? !attributes.equals(that.attributes) : that.attributes != null) return false;
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (rate != null ? !rate.equals(that.rate) : that.rate != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PriceModelDTO{"
               + "id=" + id
               + ", type=" + type
               + ", attributes=" + attributes
               + ", rate=" + rate
               + ", currencyId=" + (currency != null ? currency.getId() : null)
               + ", next=" + next
               + '}';
    }
}
