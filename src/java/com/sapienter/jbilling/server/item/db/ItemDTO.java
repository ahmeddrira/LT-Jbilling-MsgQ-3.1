/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

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

import com.sapienter.jbilling.server.invoice.db.InvoiceLineDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.csv.Exportable;
import com.sapienter.jbilling.server.util.db.AbstractDescription;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@TableGenerator(
        name = "item_GEN",
        table = "jbilling_seqs",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue = "item",
        allocationSize = 100
)
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ItemDTO extends AbstractDescription implements Exportable {

    private int id;
    private CompanyDTO entity;
    private String internalNumber;
    private PriceModelDTO defaultPrice;
    private BigDecimal percentage;
    private Integer deleted;
    private Integer hasDecimals;
    private Set<OrderLineDTO> orderLineDTOs = new HashSet<OrderLineDTO>(0);
    private Set<ItemTypeDTO> itemTypes = new HashSet<ItemTypeDTO>(0);
    private Set<InvoiceLineDTO> invoiceLines = new HashSet<InvoiceLineDTO>(0);
    private Set<PlanDTO> plans = new HashSet<PlanDTO>(0);

    private int versionNum;

    // transient
    private Integer[] types = null;
    private Collection<String> strTypes = null; // for rules 'contains' operator
    private String promoCode = null;
    private Integer currencyId = null;
    private BigDecimal price = null;
    private Integer orderLineTypeId = null;

    public ItemDTO() {
    }

    public ItemDTO(int id) {
        this.id = id;
    }

    public ItemDTO(int id, String internalNumber, BigDecimal percentage,
                   Integer hasDecimals, Integer deleted, CompanyDTO entity) {
        this.id = id;
        this.internalNumber = internalNumber;
        this.percentage = percentage;
        this.hasDecimals = hasDecimals;
        this.deleted = deleted;
        this.entity = entity;
    }

    public ItemDTO(int id, Integer deleted, Integer hasDecimals) {
        this.id = id;
        this.deleted = deleted;
        this.hasDecimals = hasDecimals;
    }

    public ItemDTO(int id, CompanyDTO entity, String internalNumber, BigDecimal percentage,
                   Integer deleted, Integer hasDecimals, Set<OrderLineDTO> orderLineDTOs, Set<ItemTypeDTO> itemTypes,
                   Set<InvoiceLineDTO> invoiceLines) {
        this.id = id;
        this.entity = entity;
        this.internalNumber = internalNumber;
        this.percentage = percentage;
        this.deleted = deleted;
        this.hasDecimals = hasDecimals;
        this.orderLineDTOs = orderLineDTOs;
        this.itemTypes = itemTypes;
        this.invoiceLines = invoiceLines;
    }

    // ItemDTOEx
    public ItemDTO(int id, String number, CompanyDTO entity, String description, Integer deleted,
                   Integer currencyId, BigDecimal price, BigDecimal percentage, Integer orderLineTypeId,
                   Integer hasDecimals) {
        
        this(id, number, percentage, hasDecimals, deleted, entity);
        setDescription(description);
        setCurrencyId(currencyId);
        setOrderLineTypeId(orderLineTypeId);
    }


    @Transient
    protected String getTable() {
        return Constants.TABLE_ITEM;
    }

    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "item_GEN")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    public CompanyDTO getEntity() {
        return this.entity;
    }

    public void setEntity(CompanyDTO entity) {
        this.entity = entity;
    }

    @Column(name = "internal_number", length = 50)
    public String getInternalNumber() {
        return this.internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "price_model_id", nullable = true)
    public PriceModelDTO getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(PriceModelDTO defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    @Column(name = "percentage")
    public BigDecimal getPercentage() {
        return this.percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Column(name = "deleted", nullable = false)
    public Integer getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Column(name = "has_decimals", nullable = false)
    public Integer getHasDecimals() {
        return this.hasDecimals;
    }

    public void setHasDecimals(Integer hasDecimals) {
        this.hasDecimals = hasDecimals;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "item")
    public Set<OrderLineDTO> getOrderLines() {
        return this.orderLineDTOs;
    }

    public void setOrderLines(Set<OrderLineDTO> orderLineDTOs) {
        this.orderLineDTOs = orderLineDTOs;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "item_type_map",
               joinColumns = {@JoinColumn(name = "item_id", updatable = false)},
               inverseJoinColumns = {@JoinColumn(name = "type_id", updatable = false)}
    )
    public Set<ItemTypeDTO> getItemTypes() {
        return this.itemTypes;
    }

    public void setItemTypes(Set<ItemTypeDTO> itemTypes) {
        this.itemTypes = itemTypes;
    }

    /**
     * Strips the given prefix off of item categories and returns the resulting code. This method allows categories to
     * be used to hold identifiers and other meta-data.
     * <p/>
     * Example: item = ItemDTO{ type : ["JB_123"] } item.getCategoryCode("JB") -> "123"
     *
     * @param prefix prefix of the category code to retrieve
     * @return code minus the given prefix
     */
    public String getCategoryCode(String prefix) {
        for (ItemTypeDTO type : getItemTypes())
            if (type.getDescription().startsWith(prefix))
                return type.getDescription().replaceAll(prefix, "");
        return null;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "item")
    public Set<InvoiceLineDTO> getInvoiceLines() {
        return this.invoiceLines;
    }

    public void setInvoiceLines(Set<InvoiceLineDTO> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    /**
     * List of all plans that use this item as the "plan subscription" item.
     *
     * @return plans
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "item")
    public Set<PlanDTO> getPlans() {
        return plans;
    }

    public void setPlans(Set<PlanDTO> plans) {
        this.plans = plans;
    }

    @Version
    @Column(name = "OPTLOCK")
    public int getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(int versionNum) {
        this.versionNum = versionNum;
    }

    @Transient
    public String getNumber() {
        return getInternalNumber();
    }

    @Transient
    public void setNumber(String number) {
        setInternalNumber(number);
    }

    /*
        Transient fields
     */

    @Transient
    public Integer[] getTypes() {
        if (this.types == null && itemTypes != null) {
            Integer[] types = new Integer[itemTypes.size()];
            int i = 0;
            for (ItemTypeDTO type : itemTypes) {
                types[i++] = type.getId();
            }
            setTypes(types);
        }
        return types;
    }

    @Transient
    public void setTypes(Integer[] types) {
        this.types = types;

        strTypes = new ArrayList<String>(types.length);
        for (Integer i : types) {
            strTypes.add(i.toString());
        }
    }

    public boolean hasType(Integer typeId) {
        return Arrays.asList(getTypes()).contains(typeId);
    }

    /**
     * Rules 'contains' operator only works on a collections of strings
     * @return collection of ItemTypeDTO ID's as strings.
     */
    @Transient
    public Collection<String> getStrTypes() {
        if (strTypes == null && itemTypes != null) {
            strTypes = new ArrayList<String>(itemTypes.size());
            for (ItemTypeDTO type : itemTypes)
                strTypes.add(String.valueOf(type.getId()));
        }

        return strTypes;
    }

    @Transient
    public String getPromoCode() {
        return promoCode;
    }


    @Transient
    public void setPromoCode(String string) {
        promoCode = string;
    }

    @Transient
    public Integer getEntityId() {
        return getEntity().getId();
    }

    @Transient
    public Integer getOrderLineTypeId() {
        return orderLineTypeId;
    }

    @Transient
    public void setOrderLineTypeId(Integer typeId) {
        orderLineTypeId = typeId;
    }

    @Transient
    public Integer getCurrencyId() {
        return currencyId;
    }

    @Transient
    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    @Transient
    public BigDecimal getPrice() {
        return price;
    }

    @Transient
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ItemDTO: id=" + getId();
    }

    @Transient
    public String[] getFieldNames() {
        return new String[] {
                "id",
                "productCode",
                "itemTypes",
                "hasDecimals",
                "percentage",
                "priceStrategy",
                "currency",
                "rate",
                "attributes"
        };
    }

    @Transient
    public Object[] getFieldValues() {
        StringBuilder itemTypes = new StringBuilder();
        for (ItemTypeDTO type : this.itemTypes) {
            itemTypes.append(type.getDescription()).append(" ");
        }

        return new Object[] {
                id,
                internalNumber,
                itemTypes.toString(),
                hasDecimals,
                percentage,
                (defaultPrice != null ? defaultPrice.getType().name() : null),

                (defaultPrice != null && defaultPrice.getCurrency() != null
                    ? defaultPrice.getCurrency().getDescription()
                    : null),

                (defaultPrice != null ? defaultPrice.getRate() : null),
                (defaultPrice != null ? defaultPrice.getAttributes() : null),
        };
    }
}


