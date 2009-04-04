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



import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.ItemTypeDAS;
import com.sapienter.jbilling.server.item.db.ItemTypeDTO;
import com.sapienter.jbilling.server.item.db.ItemUserPriceDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/*
 *
 * This is the session facade for the Item. All interaction from the client
 * to the server is made through calls to the methods of this class. This 
 * class uses helper classes (Business Logic -> BL) for the real logic.
 *
 * @author emilc
 * 
 */

@Transactional( propagation = Propagation.REQUIRED )
public class ItemSessionBean {

    //private static final Logger LOG = Logger.getLogger(ItemSessionBean.class);

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------  

    public Integer create(ItemDTO dto, Integer languageId) 
            throws SessionInternalError {
        try {
            ItemBL bl = new ItemBL();
            return bl.create(dto, languageId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    

    public void update(Integer executorId, ItemDTO dto, Integer languageId) 
            throws SessionInternalError {
        try {
            ItemBL bl = new ItemBL(dto.getId());
            bl.update(executorId, dto, languageId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    public boolean validateDecimals( Integer hasDecimals, Integer itemId ) {
    	if( itemId == null ) { return true; }
        ItemBL bl = new ItemBL(itemId);
        return bl.validateDecimals( hasDecimals );
    }

    public ItemDTO get(Integer id, Integer languageId, Integer userId,
            Integer currencyId, Integer entityId) 
            throws SessionInternalError {
        try {
            ItemBL itemBL = new ItemBL(id);
            return itemBL.getDTO(languageId, userId, entityId, currencyId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    } 

    public void delete(Integer executorId, Integer id) 
            throws SessionInternalError {
        try {
            ItemBL bl = new ItemBL(id);
            bl.delete(executorId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
    } 

    public Integer createType(ItemTypeDTO dto) 
            throws SessionInternalError {
        try {
            ItemTypeBL bl = new ItemTypeBL();
            bl.create(dto);
            return bl.getEntity().getId();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    public ItemTypeDTO getType(Integer id) 
            throws SessionInternalError {
        try {            
            ItemTypeDTO type = new ItemTypeDAS().find(id);
            ItemTypeDTO dto = new ItemTypeDTO();
            dto.setId(type.getId());
            dto.setEntity(type.getEntity());
            dto.setDescription(type.getDescription());
            dto.setOrderLineTypeId(type.getOrderLineTypeId());

            return dto;        
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    public void updateType(Integer executorId, ItemTypeDTO dto) 
            throws SessionInternalError {
        try {
            ItemTypeBL bl = new ItemTypeBL(dto.getId());
            bl.update(executorId, dto);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }

    }

    /*
     * For now, this will delete permanently
     *
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
    * @return the id if all good, null if the user/item combination already
    * exists.
    */
    public Integer createPrice(Integer executorId, ItemUserPriceDTO dto) 
            throws SessionInternalError {
        Integer retValue = null;
        ItemUserPriceBL bl;
        try {
            bl = new ItemUserPriceBL(dto.getUserId(),
                    dto.getItemId(), dto.getCurrencyId());
            boolean exists = bl.update(executorId, dto);
            if(!exists) { 
                bl = new ItemUserPriceBL();
                retValue = bl.create(dto.getUserId(), dto.getItemId(), 
                        dto.getCurrencyId(), dto.getPrice());
            } 
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
        return retValue;
    }
    
    public ItemUserPriceDTO getPrice(Integer userId, Integer itemId)
            throws SessionInternalError {
        try {
            UserBL userBL = new UserBL(userId);
            ItemUserPriceBL bl = new ItemUserPriceBL(userId, itemId, 
                    userBL.getCurrencyId());
            return bl.getDTO();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    public ItemUserPriceDTO getPrice(Integer priceId)
            throws SessionInternalError {
        try {
            ItemUserPriceBL bl = new ItemUserPriceBL(priceId);
            return bl.getDTO();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    public void updatePrice(Integer executorId, ItemUserPriceDTO dto) 
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


    public CurrencyDTO[] getCurrencies(Integer languageId, Integer entityId) 
            throws SessionInternalError {
        try {
            CurrencyBL bl = new CurrencyBL();
            return bl.getCurrencies(languageId, entityId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    public void setCurrencies(Integer entityId, CurrencyDTO[] currencies,
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

    public Integer getEntityCurrency(Integer entityId) 
            throws SessionInternalError {
        try {
            CurrencyBL bl = new CurrencyBL();
            return bl.getEntityCurrency(entityId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
    }   
           
}
