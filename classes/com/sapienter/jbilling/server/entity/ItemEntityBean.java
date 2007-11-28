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

package com.sapienter.jbilling.server.entity;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocal;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocalHome;
import com.sapienter.jbilling.interfaces.EntityEntityLocal;
import com.sapienter.jbilling.interfaces.EntityEntityLocalHome;
import com.sapienter.jbilling.interfaces.PromotionEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="ItemEntity" 
 *          display-name="Object representation of the table ITEM"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ItemEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ItemEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="item"
 *
 * @ejb.value-object name="Item"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "item"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */

public abstract class ItemEntityBean implements EntityBean {
    private Logger log = null;
    private JNDILookup EJBFactory = null;
    private DescriptionEntityLocalHome descriptionHome = null;    


    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer entityId, Float percentage, 
            Integer priceManual, String description, 
            Integer languageId) 
            throws CreateException {
        Integer newId;

        try {
            EJBFactory = JNDILookup.getFactory(false); 
            descriptionHome =
                    (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                    DescriptionEntityLocalHome.class,
                    DescriptionEntityLocalHome.JNDI_NAME);
            

            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer( generator.getNextSequenceNumber(
                    Constants.TABLE_ITEM));
            
            if (description.length() > 1000) {
                description = description.substring(0, 1000);
                log.warn("Truncated an item description to " + description);
            }

            descriptionHome.create(
                    Constants.TABLE_ITEM, newId, "description", 
                    languageId, description);
        
        } catch (Exception e) {
            log.error("Error creting item row", e);
            throw new CreateException(e.getMessage());
        }
        setId(newId);
        setPercentage(percentage);
        setPriceManual(priceManual);
        setDeleted(new Integer(0));

        return newId;
    }
    
    public void ejbPostCreate(Integer entityId, Float percentage, 
            Integer priceManual, String description, 
            Integer languageId) {
                // set the entity
        JNDILookup EJBFactory = null;
        try {
            EJBFactory = JNDILookup.getFactory(false); 

            EntityEntityLocalHome entityHome =
                    (EntityEntityLocalHome) EJBFactory.lookUpLocalHome(
                    EntityEntityLocalHome.class,
                    EntityEntityLocalHome.JNDI_NAME);
            EntityEntityLocal entityRow = entityHome.findByPrimaryKey(
                    entityId);
            setEntity(entityRow);
            
        } catch (Exception e) {
            log.error(e);
        }
    }

    private DescriptionEntityLocal getDescriptionObject(Integer language) {
        try {
            EJBFactory = JNDILookup.getFactory(false);
            descriptionHome =
                    (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                    DescriptionEntityLocalHome.class,
                    DescriptionEntityLocalHome.JNDI_NAME);
            

            return descriptionHome.findIt(Constants.TABLE_ITEM, getId(),
                "description", language);
        } catch (FinderException e) {
            log.debug("Description not set for item " + getId() + " language" +
                    " " + language);
            return null;
        } catch (Exception e) {
            log.error("Exception while looking for an item description", e);
            return null;
        }
    }

    // CMP field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getId();
    public abstract void setId(Integer itemId);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="internal_number"
     * @jboss.method-attributes read-only="true"
      */
    public abstract String getNumber();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setNumber(String number);


    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="percentage"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getPercentage();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setPercentage(Float percentage);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="price_manual"
     * @jboss.method-attributes read-only="true"
      */
     public abstract Integer getPriceManual();
    /**
      * @ejb:interface-method view-type="local"
      */
     public abstract void setPriceManual(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="deleted"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDeleted();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setDeleted(Integer deleted);

    // CMR field accessors -----------------------------------------------------
    /**
      * @ejb:interface-method view-type="local"
      * @ejb.relation name="item-type"
      *               role-name="item-belongs-to-types"
      *               target-ejb="ItemTypeEntity"
      *               target-role-name="type-has-items"
      *               target-multiple="yes"
      * @jboss.relation related-pk-field="id"  
      *                 fk-column="type_id"
      * @jboss.target-relation related-pk-field = "id"
      *                        fk-column = "item_id" 
      * @jboss.relation-table table-name = "item_type_map"    
      *                       create-table = "false"       
      */
     public abstract Collection getTypes();
    /**
     * @ejb:interface-method view-type="local"
     */
     public abstract void setTypes(Collection types);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="item-user_prices"
     *               role-name="item-has-prices"
     */
    public abstract Collection getUserPrices();
    public abstract void setUserPrices(Collection userPrices);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="item-promotion"
     *               role-name="item-is-promotion"
     */
    public abstract PromotionEntityLocal getPromotion();
    public abstract void setPromotion(PromotionEntityLocal promotion);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-items"
     *               role-name="item-belongs-to-entity"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="entity_id"            
     */
    public abstract EntityEntityLocal getEntity();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setEntity(EntityEntityLocal entity);

    /**
     * One-to-many unidirectional
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="item-prices"
     *               role-name="item-has-prices"
     *               target-ejb="ItemPriceEntity"
     *               target-role-name="prices-belong-to-item"
     *               target-cascade-delete="yes"
     * @jboss.target-relation related-pk-field = "id"
     *                        fk-column = "item_id" 
     */
    public abstract Collection getPrices();
    public abstract void setPrices(Collection prices);
    // Custom field accessors --------------------------------------------------

    /**
     * @ejb:interface-method view-type="local"
     */
    public String getDescription(Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language);
        if (desc == null) {
            return "Description in the language not set for this item";
        } 

        return desc.getContent();
    }

    /**
      * @ejb:interface-method view-type="local"
      */
    public void setDescription(String description, Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language);
        if (desc == null) {
            try {
                descriptionHome =
                   (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                   DescriptionEntityLocalHome.class,
                   DescriptionEntityLocalHome.JNDI_NAME);
                descriptionHome.create(
                        Constants.TABLE_ITEM, getId(), "description", 
                        language, description);
            } catch (Exception e) {
                log.error("Exception creating an item description", e);
            }
        } else {
            desc.setContent(description);
        }
    }
    
    // Select methods
    /**
     * @ejb.select query="SELECT up.price
     *                      FROM item AS it, IN (it.userPrices) up  
     *                     WHERE up.user.userId = ?1
     *                       AND it.id = ?2"
     */
    public abstract float ejbSelectGetUserPrice(Integer userId, Integer itemId) 
            throws FinderException;
    

    // EJB callbacks -----------------------------------------------------------
    public void setEntityContext(EntityContext context) {
        log = Logger.getLogger(ItemEntityBean.class);
    }

    public void unsetEntityContext() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbStore() {
    }

    public void ejbLoad() {
    }
}
