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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import com.sapienter.jbilling.server.item.db.ItemDTO;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@TableGenerator(
        name = "invoice_line_GEN", 
        table = "jbilling_table", 
        pkColumnName = "name", 
        valueColumnName = "next_id", 
        pkColumnValue = "invoice_line", 
        allocationSize = 100)
@Table(name = "invoice_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvoiceLineDTO implements Serializable {

    private int id;
    private InvoiceLineTypeDTO invoiceLineType;
    private ItemDTO item;
    private InvoiceDTO invoice;
    private Float amount;
    private Double quantity;
    private Float price;
    private Integer deleted;
    private String description;
    private Integer sourceUserId;
    private Integer isPercentage;
    private int versionNum;

    public InvoiceLineDTO() {
    }

    public InvoiceLineDTO(int id, Float amount, Integer deleted,
            Integer isPercentage) {
        this.id = id;
        this.amount = amount;
        this.deleted = deleted;
        this.isPercentage = isPercentage;
    }
    
    public InvoiceLineDTO(Integer id, String description, Float amount, Float price, Double quantity,
            Integer typeId, Integer deleted, Integer itemId, Integer sourceUserId, Integer isPercentage) {
        setId(id == null ? 0 : id);
        setDescription(description);
        setAmount(amount);
        setPrice(price);
        setQuantity(quantity);
        setDeleted(deleted);
        setItem(itemId == null ? null : new ItemDTO(itemId));
        setSourceUserId(sourceUserId);
        setIsPercentage(isPercentage);
        setInvoiceLineType(new InvoiceLineTypeDTO(typeId));
        
    }

    public InvoiceLineDTO(int id, InvoiceLineTypeDTO invoiceLineType,
            ItemDTO item, InvoiceDTO invoice, Float amount, Double quantity,
            Float price, Integer deleted, String description,
            Integer sourceUserId, Integer isPercentage) {
        this.id = id;
        this.invoiceLineType = invoiceLineType;
        this.item = item;
        this.invoice = invoice;
        this.amount = amount;
        this.quantity = quantity;
        this.price = price;
        this.deleted = deleted;
        this.description = description;
        this.sourceUserId = sourceUserId;
        this.isPercentage = isPercentage;
    }

    public InvoiceLineDTO(int id2, String description2, Float amount,
            Float price, Double quantity2, Integer deleted, ItemDTO item,
            Integer sourceUserId2, Integer isPercentage) {
        this.id = id2;
        this.description = description2;
        this.amount = amount;
        this.price = price;
        this.quantity = quantity2;
        this.deleted = deleted;
        this.item = item;
        this.sourceUserId = sourceUserId2;
        this.isPercentage = isPercentage;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "invoice_line_GEN")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    public InvoiceLineTypeDTO getInvoiceLineType() {
        return this.invoiceLineType;
    }

    public void setInvoiceLineType(InvoiceLineTypeDTO invoiceLineType) {
        this.invoiceLineType = invoiceLineType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    public ItemDTO getItem() {
        return this.item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    public InvoiceDTO getInvoice() {
        return this.invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    @Column(name = "amount", nullable = false, precision = 17, scale = 17)
    public Float getAmount() {
        return this.amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Column(name = "quantity")
    public Double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setQuantity(Integer quantity) {
        setQuantity(new Double(quantity));
    }

    @Column(name = "price", precision = 17, scale = 17)
    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Column(name = "deleted", nullable = false)
    public Integer getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Column(name = "description", length = 1000)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "source_user_id")
    public Integer getSourceUserId() {
        return this.sourceUserId;
    }

    public void setSourceUserId(Integer sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    @Column(name = "is_percentage", nullable = false)
    public Integer getIsPercentage() {
        return this.isPercentage;
    }

    public void setIsPercentage(Integer isPercentage) {
        this.isPercentage = isPercentage;
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
    public int getOrderPosition() {
        return getInvoiceLineType().getOrderPosition();
    }
    
    @Transient
    public int getTypeId() {
        return getInvoiceLineType().getId();
    }
}