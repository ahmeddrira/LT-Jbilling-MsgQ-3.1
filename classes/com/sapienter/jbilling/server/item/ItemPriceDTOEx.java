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

public class ItemPriceDTOEx
   extends java.lang.Object
   implements java.io.Serializable 
{
    // ItemPriceDTO
    private java.lang.Integer id;
    private java.lang.Float price;
    private java.lang.Integer currencyId;

    // ItemPriceDTOEx
    private String name = null;
    // this is useful for the form, exposing a Float is trouble
    private String priceForm = null;


   public ItemPriceDTOEx()
   {
   }

   public ItemPriceDTOEx( java.lang.Integer id,java.lang.Float price,java.lang.Integer currencyId )
   {
	  this.id = id;
	  this.price = price;
	  this.currencyId = currencyId;
   }

   //TODO Cloneable is better than this !
   public ItemPriceDTOEx( ItemPriceDTOEx otherValue )
   {
	  this.id = otherValue.id;
	  this.price = otherValue.price;
	  this.currencyId = otherValue.currencyId;
   }

   public java.lang.Integer getId()
   {
	  return this.id;
   }

   public void setId( java.lang.Integer id )
   {
	  this.id = id;
   }

   public java.lang.Float getPrice()
   {
	  return this.price;
   }

   public void setPrice( java.lang.Float price )
   {
	  this.price = price;
   }

   public java.lang.Integer getCurrencyId()
   {
	  return this.currencyId;
   }

   public void setCurrencyId( java.lang.Integer currencyId )
   {
	  this.currencyId = currencyId;
   }

    // ItemPriceDTOEx
    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return "name = " + name + " priceForm = " + priceForm + itemPriceDtoToString();
    }

    /**
     * @return
     */
    public String getPriceForm() {
        return priceForm;
    }

    /**
     * @param priceForm
     */
    public void setPriceForm(String priceForm) {
        this.priceForm = priceForm;
    }

   public String itemPriceDtoToString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("id=" + getId() + " " + "price=" + getPrice() + " " + "currencyId=" + getCurrencyId());
	  str.append('}');

	  return(str.toString());
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if (other instanceof ItemPriceDTOEx)
	  {
		 ItemPriceDTOEx that = (ItemPriceDTOEx) other;
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
	  if (other instanceof ItemPriceDTOEx)
	  {
		 ItemPriceDTOEx that = (ItemPriceDTOEx) other;
		 boolean lEquals = true;
		 if( this.price == null )
		 {
			lEquals = lEquals && ( that.price == null );
		 }
		 else
		 {
			lEquals = lEquals && this.price.equals( that.price );
		 }
		 if( this.currencyId == null )
		 {
			lEquals = lEquals && ( that.currencyId == null );
		 }
		 else
		 {
			lEquals = lEquals && this.currencyId.equals( that.currencyId );
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

      result = 37*result + ((this.price != null) ? this.price.hashCode() : 0);

      result = 37*result + ((this.currencyId != null) ? this.currencyId.hashCode() : 0);

	  return result;
   }

}
