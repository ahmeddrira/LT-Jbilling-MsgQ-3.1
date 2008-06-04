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


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.db.Item;
import com.sapienter.jbilling.server.item.db.ItemDAS;

@Entity
@TableGenerator(
        name="order_line_GEN",
        table="jbilling_table",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue="order_line",
        allocationSize=10
        )
@Table(name="order_line")
//No cache, mutable and critical
public class OrderLineDTO implements Serializable, Comparable {

	private static final Logger LOG =  Logger.getLogger(OrderLineDTO.class); 

     private int id;
     private OrderLineTypeDTO orderLineTypeDTO;
     private Item item;
     private OrderDTO orderDTO;
     private Float amount;
     private Integer quantity;
     private Float price;
     private Integer itemPrice;
     private Date createDatetime;
     private int deleted;
     private String description;
     private Integer versionNum;
     private Boolean editable = null;
     
     // other fields, non-persistent
     private String priceStr = null;
     private Boolean totalReadOnly = null;
     private ItemDTOEx itemDto = null; // only while item is not JPAed

     
    public OrderLineDTO() {
    }
    
    public OrderLineDTO(OrderLineDTO other) {
    	this.id = other.id;
    	this.orderLineTypeDTO = other.getOrderLineType();
    	this.item = other.getItem();
    	this.amount = other.getAmount();
    	this.quantity = other.getQuantity();
    	this.price = other.getPrice();
    	this.itemPrice = other.getItemPrice();
    	this.createDatetime = other.getCreateDatetime();
    	this.deleted = other.getDeleted();
    	this.description = other.getDescription();
    	this.orderDTO = other.getPurchaseOrder();
    	this.versionNum = other.getVersionNum();
    }
	
    public OrderLineDTO(int id, float amount, Date createDatetime, Integer deleted) {
        this.id = id;
        this.amount = amount;
        this.createDatetime = createDatetime;
        this.deleted = deleted;
    }
    public OrderLineDTO(int id, OrderLineTypeDTO orderLineTypeDTO, Item item, OrderDTO orderDTO, float amount, 
    		Integer quantity, Float price, Integer itemPrice, Date createDatetime, Integer deleted, String description) {
       this.id = id;
       this.orderLineTypeDTO = orderLineTypeDTO;
       this.item = item;
       this.orderDTO = orderDTO;
       this.amount = amount;
       this.quantity = quantity;
       this.price = price;
       this.itemPrice = itemPrice;
       this.createDatetime = createDatetime;
       this.deleted = deleted;
       this.description = description;
    }
   
    @Id @GeneratedValue(strategy=GenerationType.TABLE, generator="order_line_GEN")
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="type_id", nullable=false)
    public OrderLineTypeDTO getOrderLineType() {
        return this.orderLineTypeDTO;
    }
    
    public void setOrderLineType(OrderLineTypeDTO orderLineTypeDTO) {
        this.orderLineTypeDTO = orderLineTypeDTO;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    public Item getItem() {
        return this.item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id")
    public OrderDTO getPurchaseOrder() {
        return this.orderDTO;
    }
    
    public void setPurchaseOrder(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }
    
    @Column(name="amount", nullable=false, precision=17, scale=17)
    public Float getAmount() {
        return this.amount;
    }
    
    public void setAmount(Float amount) {
        this.amount = amount;
    }
    
    @Column(name="quantity")
    public Integer getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    @Column(name="price", precision=17, scale=17)
    public Float getPrice() {
        return this.price;
    }
    
    public void setPrice(Float price) {
        this.price = price;
    }
    
    @Column(name="item_price")
    public Integer getItemPrice() {
        return this.itemPrice;
    }
    
    public void setItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }
    @Column(name="create_datetime", nullable=false, length=29)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    
    @Column(name="deleted", nullable=false)
    public int getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
    
    @Column(name="description", length=1000)
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        if (description.length() > 1000) {
            description = description.substring(0, 1000);
            LOG.warn("Truncated an order line description to " + description);
        }

        this.description = description;
    }

    @Version
    @Column(name="OPTLOCK")
	public Integer getVersionNum() {
		return versionNum;
	}
	public void setVersionNum(Integer versionNum) {
		this.versionNum = versionNum;
	}

	/*
	 * Conveniant methods to ease migration from entity beans
	 */
    @Transient
    public Integer getItemId() {
    	return (getItem() == null) ? null : getItem().getId();
    }
    public void setItemId(Integer itemId) {
    	//Item item = new Item();
    	//item.setId(itemId);
    	ItemDAS das = new ItemDAS();
    	setItem(das.find(itemId));
    }

    @Transient
	public Boolean getEditable() {
    	if (editable == null) {
			editable = getOrderLineType().getEditable().intValue() == 1;
    	}
    	return editable;
	}
    public void setEditable(Boolean editable) {
    	this.editable = editable;
    }

    @Transient
	public String getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}

    @Transient
	public Boolean getTotalReadOnly() {
        if (totalReadOnly == null) {
            setTotalReadOnly(false);
        }
		return totalReadOnly;
	}
	public void setTotalReadOnly(Boolean totalReadOnly) {
		this.totalReadOnly = totalReadOnly;
	}

    @Transient
	public Integer getTypeId() {
		return getOrderLineType() == null ? null : getOrderLineType().getId();
	}
	public void setTypeId(Integer typeId) {
		OrderLineTypeDAS das = new OrderLineTypeDAS();
		setOrderLineType(das.find(typeId));
	}

    @Transient
	public ItemDTOEx getItemDto() {
		return itemDto;
	}
	public void setItemDto(ItemDTOEx itemDto) {
		this.itemDto = itemDto;
	}
	
	public void touch() {
		getCreateDatetime();
		getItem().getInternalNumber();
		getEditable();
	}
	
    @PrePersist
    private void setDefaults() {
    	setCreateDatetime(Calendar.getInstance().getTime());
    }
    
    // this helps to add lines to the treeSet
    public int compareTo(Object o) {
    	OrderLineDTO other = (OrderLineDTO) o;
    	if (other.getItem() == null || this.getItem() == null) {
    		return -1;
    	}
    	return new Integer(this.getItem().getId()).compareTo(new Integer(other.getItem().getId()));
    }
    
    public String toString() {
        return "OrderLine:[id=" + id +
        " orderLineType=" + ((orderLineTypeDTO == null) ? "null" : orderLineTypeDTO.getId()) +
        " item=" +  item.getId() +
        " order id=" + ((orderDTO == null) ? "null" : orderDTO.getId()) +
        " amount=" +  amount +
        " quantity=" +  quantity +
        " price=" +  price +
        " itemPrice=" +  itemPrice +
        " createDatetime=" +  createDatetime +
        " deleted=" + deleted  +
        " description=" + description + 
        " versionNum=" + versionNum  +
        " editable=" + editable + "]";

    }
}