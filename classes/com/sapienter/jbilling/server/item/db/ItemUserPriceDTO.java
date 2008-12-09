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
package com.sapienter.jbilling.server.item.db;


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
import javax.persistence.Transient;
import javax.persistence.Version;

import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@TableGenerator(
        name="item_user_GEN",
        table="jbilling_table",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue="item_user_price",
        allocationSize=10
        )
@Table(name="item_user_price")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ItemUserPriceDTO  implements java.io.Serializable {


    private int id;
    private UserDTO baseUser;
    private CurrencyDTO currencyDTO;
    private ItemDTO item;
    private Float price;
    private int versionNum;

    public ItemUserPriceDTO() {
    }

    public ItemUserPriceDTO(int id, CurrencyDTO currencyDTO, Float price) {
        this.id = id;
        this.currencyDTO = currencyDTO;
        this.price = price;
    }

    public ItemUserPriceDTO(int id, UserDTO baseUser, CurrencyDTO currencyDTO, ItemDTO item, Float price) {
       this.id = id;
       this.baseUser = baseUser;
       this.currencyDTO = currencyDTO;
       this.item = item;
       this.price = price;
    }

    public ItemUserPriceDTO(UserDTO baseUser, ItemDTO item, Float price, CurrencyDTO currencyDTO) {
        this.baseUser = baseUser;
        this.currencyDTO = currencyDTO;
        this.item = item;
        this.price = price;
    }

    public ItemUserPriceDTO(ItemUserPriceDTO other) {
        id = other.id;
        baseUser = other.baseUser;
        currencyDTO = other.currencyDTO;
        item = other.item;
        price = other.price;
        versionNum = other.versionNum;
    }
   
    @Id @GeneratedValue(strategy=GenerationType.TABLE, generator="item_user_GEN")
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    public UserDTO getBaseUser() {
        return this.baseUser;
    }
    
    public void setBaseUser(UserDTO baseUser) {
        this.baseUser = baseUser;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="currency_id", nullable=false)
    public CurrencyDTO getCurrency() {
        return this.currencyDTO;
    }

    public void setCurrency(CurrencyDTO currencyDTO) {
        this.currencyDTO = currencyDTO;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    public ItemDTO getItem() {
        return this.item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    @Column(name="price", nullable=false, precision=17, scale=17)
    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Version
    @Column(name="OPTLOCK")
    public int getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(int versionNum) {
        this.versionNum = versionNum;
    }

    @Transient
    public Integer getCurrencyId() {
        return currencyDTO.getId();
    }

    @Transient
    public UserDTO getUser() {
        return getBaseUser();
    }

    @Transient
    public void setUser(UserDTO user) {
        setBaseUser(user);
    }

    @Transient
    public Integer getItemId() {
        return item.getId();
    }

    @Transient
    public Integer getUserId() {
        return baseUser.getId();
    }
}


