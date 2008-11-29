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

package com.sapienter.jbilling.server.item;

import java.util.Collection;
import java.util.Vector;

/**
 * @jboss-net.xml-schema urn="sapienter:ItemDTOEx"
 */
public class ItemDTOEx
   extends java.lang.Object
   implements java.io.Serializable 
{
    // ItemDTO
   private java.lang.Integer id;
   private java.lang.String number;
   private java.lang.Float percentage;
   private java.lang.Integer priceManual;
   private java.lang.Integer hasDecimals;
   private java.lang.Integer deleted;
   private java.lang.Integer entityId;

    // *** ItemDTOEx ***
    private String description = null;
    private Integer[] types = null;
    private String promoCode = null;
    private Integer currencyId = null;
    private Float price = null;
    private Integer orderLineTypeId = null;
    // all the prices.ItemPriceDTOEx  
    private Vector prices = null;
    
    public ItemDTOEx(Integer id,String number, Integer entity, 
            String description,
            Integer manualPrice, Integer deleted, Integer currencyId,
            Float price, Float percentage, Integer orderLineTypeId,
            Integer hasDecimals ) {
        this(id, number, percentage, manualPrice, hasDecimals, deleted, entity);

        setDescription(description);
        setCurrencyId(currencyId);
        setPrice(price);
        setOrderLineTypeId(orderLineTypeId);
        //types = new Vector();
    }

    public ItemDTOEx() {
        //types = new Vector();
    }

   public ItemDTOEx( java.lang.Integer id,java.lang.String number,java.lang.Float percentage,java.lang.Integer priceManual,java.lang.Integer hasDecimals,java.lang.Integer deleted,java.lang.Integer entityId )
   {
	  this.id = id;
	  this.number = number;
	  this.percentage = percentage;
	  this.priceManual = priceManual;
	  this.hasDecimals = hasDecimals;
	  this.deleted = deleted;
	  this.entityId = entityId;
   }

   //TODO Cloneable is better than this !
   public ItemDTOEx( ItemDTOEx otherValue )
   {
	  this.id = otherValue.id;
	  this.number = otherValue.number;
	  this.percentage = otherValue.percentage;
	  this.priceManual = otherValue.priceManual;
	  this.hasDecimals = otherValue.hasDecimals;
	  this.deleted = otherValue.deleted;
	  this.entityId = otherValue.entityId;
   }

   public java.lang.Integer getId()
   {
	  return this.id;
   }

   public void setId( java.lang.Integer id )
   {
	  this.id = id;
   }

   public java.lang.String getNumber()
   {
	  return this.number;
   }

   public void setNumber( java.lang.String number )
   {
	  this.number = number;
   }

   public java.lang.Float getPercentage()
   {
	  return this.percentage;
   }

   public void setPercentage( java.lang.Float percentage )
   {
	  this.percentage = percentage;
   }

   public java.lang.Integer getPriceManual()
   {
	  return this.priceManual;
   }

   public void setPriceManual( java.lang.Integer priceManual )
   {
	  this.priceManual = priceManual;
   }

   public java.lang.Integer getHasDecimals()
   {
	  return this.hasDecimals;
   }

   public void setHasDecimals( java.lang.Integer hasDecimals )
   {
	  this.hasDecimals = hasDecimals;
   }

   public java.lang.Integer getDeleted()
   {
	  return this.deleted;
   }

   public void setDeleted( java.lang.Integer deleted )
   {
	  this.deleted = deleted;
   }

   public java.lang.Integer getEntityId()
   {
	  return this.entityId;
   }

   public void setEntityId( java.lang.Integer entityId )
   {
	  this.entityId = entityId;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("id=" + getId() + " " + "number=" + getNumber() + " " + "percentage=" + getPercentage() + " " + "priceManual=" + getPriceManual() + " " + "hasDecimals=" + getHasDecimals() + " " + "deleted=" + getDeleted() + " " + "entityId=" + getEntityId());
	  str.append('}');

	  return(str.toString());
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if (other instanceof ItemDTOEx)
	  {
		 ItemDTOEx that = (ItemDTOEx) other;
		 boolean lEquals = true;
		 if( this.id == null )
		 {
			lEquals = lEquals && ( that.id == null );
		 }
		 else
		 {
			lEquals = lEquals && this.id.equals( that.id );
		 }

		 lEquals = lEquals && isIdentical(that);

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public boolean isIdentical(Object other)
   {
	  if (other instanceof ItemDTOEx)
	  {
		 ItemDTOEx that = (ItemDTOEx) other;
		 boolean lEquals = true;
		 if( this.number == null )
		 {
			lEquals = lEquals && ( that.number == null );
		 }
		 else
		 {
			lEquals = lEquals && this.number.equals( that.number );
		 }
		 if( this.percentage == null )
		 {
			lEquals = lEquals && ( that.percentage == null );
		 }
		 else
		 {
			lEquals = lEquals && this.percentage.equals( that.percentage );
		 }
		 if( this.priceManual == null )
		 {
			lEquals = lEquals && ( that.priceManual == null );
		 }
		 else
		 {
			lEquals = lEquals && this.priceManual.equals( that.priceManual );
		 }
		 if( this.hasDecimals == null )
		 {
			lEquals = lEquals && ( that.hasDecimals == null );
		 }
		 else
		 {
			lEquals = lEquals && this.hasDecimals.equals( that.hasDecimals );
		 }
		 if( this.deleted == null )
		 {
			lEquals = lEquals && ( that.deleted == null );
		 }
		 else
		 {
			lEquals = lEquals && this.deleted.equals( that.deleted );
		 }
		 if( this.entityId == null )
		 {
			lEquals = lEquals && ( that.entityId == null );
		 }
		 else
		 {
			lEquals = lEquals && this.entityId.equals( that.entityId );
		 }

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public int hashCode(){
	  int result = 17;
      result = 37*result + ((this.id != null) ? this.id.hashCode() : 0);

      result = 37*result + ((this.number != null) ? this.number.hashCode() : 0);

      result = 37*result + ((this.percentage != null) ? this.percentage.hashCode() : 0);

      result = 37*result + ((this.priceManual != null) ? this.priceManual.hashCode() : 0);

      result = 37*result + ((this.hasDecimals != null) ? this.hasDecimals.hashCode() : 0);

      result = 37*result + ((this.deleted != null) ? this.deleted.hashCode() : 0);

      result = 37*result + ((this.entityId != null) ? this.entityId.hashCode() : 0);

	  return result;
   }

    // *** ItemDTOEx ***

    /**
     * Returns the description.
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return
     */
    public Integer[] getTypes() {
        return types;
    }

    /*
     * Rules only work on collections of strings (oparator contains)
     */
    public Collection<String> getStrTypes() {
        Vector<String> retValue = new Vector<String>();
        for (Integer i: types) {
            retValue.add(i.toString());
        }
        return retValue;
    }

    /**
     * @param vector
     */
    public void setTypes(Integer[] vector) {
        types = vector;
    }

    /**
     * @return
     */
    public String getPromoCode() {
        return promoCode;
    }

    /**
     * @param string
     */
    public void setPromoCode(String string) {
        promoCode = string;
    }

    /**
     * @return
     */
    public Integer getOrderLineTypeId() {
        return orderLineTypeId;
    }

    /**
     * @param integer
     */
    public void setOrderLineTypeId(Integer typeId) {
        orderLineTypeId = typeId;
    }

    /**
     * @return
     */
    public Integer getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId
     */
    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return
     */
    public Float getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(Float price) {
        this.price = price;
    }
    /**
     * @return
     */
    public Vector getPrices() {
        return prices;
    }

    /**
     * @param prices
     */
    public void setPrices(Vector prices) {
        this.prices = prices;
    }
}
