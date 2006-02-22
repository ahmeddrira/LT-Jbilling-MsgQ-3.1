/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/

package com.sapienter.jbilling.server.item;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.ItemTypeEntityLocal;
import com.sapienter.jbilling.interfaces.ItemTypeEntityLocalHome;
import com.sapienter.jbilling.server.user.UserBL;

/**
 *
 * This is the session facade for the Item. All interaction from the client
 * to the server is made through calls to the methods of this class. This 
 * class uses helper classes (Business Logic -> BL) for the real logic.
 *
 * @author emilc
 * @ejb:bean name="com/sapienter/jbilling/server/item/ItemSession"
 *           display-name="The item session facade"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="both"
 *           jndi-name="com/sapienter/jbilling/server/item/ItemSession"
 * 
 * @ejb.resource-ref res-ref-name="jdbc/ApplicationDS"
 *                   res-type="javax.sql.DataSource"
 * 					 res-auth="Container"
 * 
 * @jboss.resource-ref res-ref-name="jdbc/ApplicationDS"
 *                     jndi-name="java:/ApplicationDS"
 * 
 */

public class ItemSessionBean implements SessionBean {

    private Logger log = null;
    private SessionContext mContext;
    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------  

    /**
    * @ejb:interface-method view-type="remote"
    */
    public Integer create(ItemDTOEx dto, Integer languageId) 
            throws SessionInternalError {
        try {
            ItemBL bl = new ItemBL();
            return bl.create(dto, languageId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    

    /**
    * @ejb:interface-method view-type="remote"
    */
    public void update(Integer executorId, ItemDTOEx dto, Integer languageId) 
            throws SessionInternalError {
        try {
            ItemBL bl = new ItemBL(dto.getId());
            bl.update(executorId, dto, languageId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public ItemDTOEx get(Integer id, Integer languageId, Integer userId,
            Integer currencyId, Integer entityId) 
            throws SessionInternalError {
        try {
            ItemBL itemBL = new ItemBL(id);
            return itemBL.getDTO(languageId, userId, entityId, currencyId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    } 

    /**
    * @ejb:interface-method view-type="remote"
    */
    public void delete(Integer executorId, Integer id) 
            throws SessionInternalError {
        try {
            ItemBL bl = new ItemBL(id);
            bl.delete(executorId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
    } 

    /**
    * @ejb:interface-method view-type="remote"
    */
    public Integer createType(ItemTypeDTOEx dto) 
            throws SessionInternalError {
        try {
            ItemTypeBL bl = new ItemTypeBL();
            bl.create(dto);
            return bl.getEntity().getId();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    /**
    * @ejb:interface-method view-type="remote"
    */
    public ItemTypeDTOEx getType(Integer id) 
            throws SessionInternalError {
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ItemTypeEntityLocalHome itemTypeHome =
                   (ItemTypeEntityLocalHome) EJBFactory.lookUpLocalHome(
                    ItemTypeEntityLocalHome.class,
                    ItemTypeEntityLocalHome.JNDI_NAME);
            
            ItemTypeEntityLocal type = itemTypeHome.findByPrimaryKey(id);
            ItemTypeDTOEx dto = new ItemTypeDTOEx();
            dto.setId(type.getId());
            dto.setEntityId(type.getEntityId());
            dto.setDescription(type.getDescription());
            dto.setOrderLineTypeId(type.getOrderLineTypeId());

            return dto;        
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public void updateType(Integer executorId, ItemTypeDTOEx dto) 
            throws SessionInternalError {
        try {
            ItemTypeBL bl = new ItemTypeBL(dto.getId());
            bl.update(executorId, dto);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }

    }

    /**
     * For now, this will delete permanently
     * @ejb:interface-method view-type="remote"
     */
     public void deleteType(Integer executorId, Integer itemTypeId) 
             throws SessionInternalError {
         try {
             
             ItemTypeBL bl = new ItemTypeBL(itemTypeId);
             bl.delete(executorId);

         } catch (Exception e) {
             throw new SessionInternalError(e);
         }
     }


    /**
    * @ejb:interface-method view-type="remote"
    * @return the id if all good, null if the user/item combination already
    * exists.
    */
    public Integer createPrice(Integer executorId, ItemUserPriceDTOEx dto) 
            throws SessionInternalError {
        Integer retValue = null;
        ItemUserPriceBL bl;
        try {
            try {
                bl = new ItemUserPriceBL(dto.getUserId(),
                        dto.getItemId(), dto.getCurrencyId());
                bl.update(executorId, dto); 
            } catch (FinderException e1) {
                bl = new ItemUserPriceBL();
                retValue = bl.create(dto.getUserId(), dto.getItemId(), 
                        dto.getCurrencyId(), dto.getPrice());
            } 
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
        return retValue;
    }
    
    /**
    * @ejb:interface-method view-type="remote"
    */
    public ItemUserPriceDTOEx getPrice(Integer userId, Integer itemId)
            throws SessionInternalError {
        try {
            UserBL userBL = new UserBL(userId);
            ItemUserPriceBL bl = new ItemUserPriceBL(userId, itemId, 
                    userBL.getCurrencyId());
            return bl.getDTO();
        } catch (FinderException e) {
            // we let know the caller if the combination is not there
            return null;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public ItemUserPriceDTOEx getPrice(Integer priceId)
            throws SessionInternalError {
        try {
            ItemUserPriceBL bl = new ItemUserPriceBL(priceId);
            return bl.getDTO();
        } catch (FinderException e) {
            // we let know the caller if the combination is not there
            return null;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    /**
    * @ejb:interface-method view-type="remote"
    */
    public void updatePrice(Integer executorId, ItemUserPriceDTOEx dto) 
            throws SessionInternalError {
        try {
            ItemUserPriceBL bl = new ItemUserPriceBL(dto.getId());
            bl.update(executorId, dto);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }

    }

    /**
     * For now, this will delete permanently
     * @ejb:interface-method view-type="remote"
     */
     public void deletePrice(Integer executorId, Integer itemPriceId) 
             throws SessionInternalError {
         try {
             
             ItemUserPriceBL bl = new ItemUserPriceBL(itemPriceId);
             bl.delete(executorId);

         } catch (Exception e) {
             throw new SessionInternalError(e);
         }
     }

    /**
    * @ejb:interface-method view-type="remote"
    * @return the id if all good, null if the user/item combination already
    * exists.
    */
    public Integer createPromotion(Integer executorId, Integer entityId, 
            PromotionDTOEx dto) throws SessionInternalError {
        Integer retValue;
        try {
            PromotionBL bl = new PromotionBL();
            retValue = bl.create(entityId, dto);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
        return retValue;
    }
    
    /**
    * @ejb:interface-method view-type="remote"
    */
    public PromotionDTOEx getPromotion(Integer id) 
            throws SessionInternalError {
        try {
            PromotionBL bl = new PromotionBL(id);            
            return bl.getDTO();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public PromotionDTOEx getPromotion(Integer entityId, String code) 
            throws SessionInternalError {
        try {
            PromotionBL bl = new PromotionBL();
            if (bl.isPresent(entityId, code)) {            
                return bl.getDTO();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public Boolean promotionIsAvailable(Integer promotionId, Integer userId, 
            String code) throws SessionInternalError {
        try {
            PromotionBL bl = new PromotionBL(promotionId);
            return new Boolean(bl.isAvailable(userId, code));
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public void updatePromotion(Integer executorId, PromotionDTOEx dto) 
            throws SessionInternalError {
        try {
            PromotionBL bl = new PromotionBL(dto.getId());
            bl.update(executorId, dto);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }

    }

    /**
     * For now, this will delete permanently
     * @ejb:interface-method view-type="remote"
     */
     public void deletePromotion(Integer executorId, Integer itemPromotionId) 
             throws SessionInternalError {
         try {
             PromotionBL bl = new PromotionBL(itemPromotionId);
             bl.delete(executorId);
         } catch (Exception e) {
             throw new SessionInternalError(e);
         }
     }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public CurrencyDTOEx[] getCurrencies(Integer languageId, Integer entityId) 
            throws SessionInternalError {
        try {
            CurrencyBL bl = new CurrencyBL();
            return bl.getCurrencies(languageId, entityId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    /**
    * @ejb:interface-method view-type="remote"
    */
    public void setCurrencies(Integer entityId, CurrencyDTOEx[] currencies,
            Integer currencyId) 
            throws SessionInternalError {
        try {
            CurrencyBL bl = new CurrencyBL();
            bl.setCurrencies(entityId, currencies);
            bl.setEntityCurrency(entityId, currencyId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
    }   

    /**
    * @ejb:interface-method view-type="remote"
    */
    public Integer getEntityCurrency(Integer entityId) 
            throws SessionInternalError {
        try {
            CurrencyBL bl = new CurrencyBL();
            return bl.getEntityCurrency(entityId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
    }   
        
    /**
    * Create the Session Bean
    *
    * @throws CreateException 
    *
    * @ejb:create-method view-type="remote"
    */
    public void ejbCreate() throws CreateException {
        if (log == null) {
            log = Logger.getLogger(ItemSessionBean.class);
        }
    }

    /**
    * Describes the instance and its content for debugging purpose
    *
    * @return Debugging information about the instance and its content
    */
    public String toString() {
        return "ItemSessionBean [ " + " ]";
    }

    // -------------------------------------------------------------------------
    // Framework Callbacks
    // -------------------------------------------------------------------------  

    public void setSessionContext(SessionContext aContext)
            throws EJBException {
        log = Logger.getLogger(ItemSessionBean.class);
        mContext = aContext;
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbRemove() throws EJBException {
    }
}
