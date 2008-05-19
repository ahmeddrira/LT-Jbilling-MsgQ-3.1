/*
 jbilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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

/*
 * Created on Dec 30, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.order;

import java.io.Serializable;
import java.util.Date;

import com.sapienter.jbilling.server.item.ItemDTOEx;

/**
 * @author Emil
 * @jboss-net.xml-schema urn="sapienter:OrderLineWS"
 */
public class OrderLineWS implements Serializable {

	private int id;

	private Integer orderId;

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

	private ItemDTOEx itemDto = null;

	private Integer typeId = null;

	private Boolean useItem = null;

	private Integer itemId = null;

	/**
	 * 
	 */
	public OrderLineWS() {
	}

	/**
	 * @param id
	 * @param itemId
	 * @param description
	 * @param amount
	 * @param quantity
	 * @param price
	 * @param itemPrice
	 * @param createDate
	 * @param deleted
	 */
	public OrderLineWS(Integer id, Integer itemId, String description,
			Float amount, Integer quantity, Float price, Integer itemPrice,
			Date create, Integer deleted, Integer newTypeId, Boolean editable,
			Integer orderId, Boolean useItem, Integer version) {
		setId(id);
		setItemId(itemId);
		setDescription(description);
		setAmount(amount);
		setQuantity(quantity);
		setPrice(price);
		setItemPrice(itemPrice);
		setCreateDatetime(create);
		setDeleted(deleted);
		setTypeId(newTypeId);
		setEditable(editable);
		setOrderId(orderId);
		setUseItem(useItem);
		setVersionNum(version);
	}

	/**
	 * @return
	 */
	public Integer getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 */
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Boolean getUseItem() {
		return useItem == null ? new Boolean(false) : useItem;
	}

	public void setUseItem(Boolean useItem) {
		this.useItem = useItem;
	}

	public String toString() {
		return " typeId = " + typeId + " useItem = "
				+ useItem + "itemId = " + itemId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ItemDTOEx getItemDto() {
		return itemDto;
	}

	public void setItemDto(ItemDTOEx itemDto) {
		this.itemDto = itemDto;
	}

	public Integer getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(Integer versionNum) {
		this.versionNum = versionNum;
	}
}
