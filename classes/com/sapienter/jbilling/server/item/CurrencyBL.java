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

/*
 * Created on Mar 10, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.item;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.CommonConstants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.CurrencyEntityLocal;
import com.sapienter.jbilling.interfaces.CurrencyEntityLocalHome;
import com.sapienter.jbilling.interfaces.CurrencyExchangeEntityLocal;
import com.sapienter.jbilling.interfaces.CurrencyExchangeEntityLocalHome;
import com.sapienter.jbilling.interfaces.EntityEntityLocal;
import com.sapienter.jbilling.interfaces.EntityEntityLocalHome;
import com.sapienter.jbilling.server.entity.CurrencyDTO;
import com.sapienter.jbilling.server.user.EntityBL;
import com.sapienter.jbilling.server.util.Util;

/**
 * @author Emil
 */
public class CurrencyBL {
    private JNDILookup EJBFactory = null;
    private CurrencyEntityLocalHome currencyHome = null;
    private CurrencyExchangeEntityLocalHome currencyExchangeHome = null;
    private CurrencyEntityLocal currency = null;
    EntityEntityLocalHome entityHome = null;
    private Logger log = null;
    
    public CurrencyBL(Integer currencyId) 
            throws NamingException, FinderException {
        init();
        set(currencyId);
    }
    
    public void set(Integer id) 
            throws FinderException {
        currency = currencyHome.findByPrimaryKey(id);
    }
    
    public CurrencyBL() throws NamingException {
        init();
    }
    
    private void init() throws NamingException {
        log = Logger.getLogger(CurrencyBL.class);     
        EJBFactory = JNDILookup.getFactory(false);
        currencyHome = (CurrencyEntityLocalHome) EJBFactory.lookUpLocalHome(
                CurrencyEntityLocalHome.class,
                CurrencyEntityLocalHome.JNDI_NAME);

        currencyExchangeHome = (CurrencyExchangeEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                CurrencyExchangeEntityLocalHome.class,
                CurrencyExchangeEntityLocalHome.JNDI_NAME);
        
        entityHome = (EntityEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                EntityEntityLocalHome.class,
                EntityEntityLocalHome.JNDI_NAME);

    }
    
    public CurrencyEntityLocal getEntity() {
        return currency;
    }

    public Float convert(Integer fromCurrencyId, Integer toCurrencyId,
            Float amount, Integer entityId) 
            throws SessionInternalError {
        Float retValue = null;
        
        if (fromCurrencyId.equals(toCurrencyId)) {
            // mmm.. no conversion needed
            return amount;
        }
        
        // make the conversions
        retValue = convertPivotToCurrency(toCurrencyId, 
                convertToPivot(fromCurrencyId, amount, entityId), entityId);
        
        return retValue;         
    }
    
    public Float convertToPivot(Integer currencyId, Float amount, 
            Integer entityId) 
            throws SessionInternalError {
        Float retValue = null;
        CurrencyExchangeEntityLocal exchange = null;
        
        if (currencyId.intValue() == 1) {
            // this is already in the pivot
            return amount;
        }
        
        exchange = findExchange(entityId, currencyId);
        // make the conversion itself
        BigDecimal tmp = new BigDecimal(amount.toString());
        tmp = tmp.divide(new BigDecimal(exchange.getRate().toString()), CommonConstants.BIGDECIMAL_SCALE, CommonConstants.BIGDECIMAL_ROUND);
        retValue = new Float(tmp.floatValue());
        
        return retValue;
    }
    
    public Float convertPivotToCurrency(Integer currencyId, Float amount,
            Integer entityId) 
            throws SessionInternalError {
        Float retValue = null;
        CurrencyExchangeEntityLocal exchange = null;
        
        if (currencyId.intValue() == 1) {
            // this is already in the pivot
            return amount;
        }
        
        exchange = findExchange(entityId, currencyId);
        // make the conversion itself
        BigDecimal tmp = new BigDecimal(amount.toString());
        tmp.multiply(new BigDecimal(exchange.getRate().toString()));
        retValue = new Float(tmp.floatValue());
        
        return retValue;
    }
    
    public CurrencyExchangeEntityLocal findExchange(Integer entityId,
            Integer currencyId) 
            throws SessionInternalError {
        CurrencyExchangeEntityLocal exchange = null;
        
        try {
            exchange = currencyExchangeHome.find(entityId, currencyId);
        } catch (FinderException e) {
            // this entity doesn't have this exchange defined
            try {
                // 0 is the default, don't try to use null, it won't work
                exchange = currencyExchangeHome.find(new Integer(0), 
                        currencyId);
            } catch (FinderException e1) {
                throw new SessionInternalError("Currency " + currencyId +
                        " doesn't have a defualt exchange");
            }
        }
    
        return exchange;
    }
    
    /**
     * Returns all the currency symbols in the proper order, so
     * retValue[currencyId] would be the symbol of currencyId
     * @return
     * @throws NamingException
     * @throws SQLException
     */
    public CurrencyDTO[] getSymbols() 
            throws NamingException, SQLException {
        JNDILookup jndi = JNDILookup.getFactory();
        Connection conn = jndi.lookUpDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "select id, symbol, code " +
                "  from currency");
        ResultSet result = stmt.executeQuery();
        Vector results = new Vector();
        while (result.next()) {
            int currencyId = result.getInt(1);
            // ensure that the vector will have space for this currency
            if (results.size() < currencyId) {
                results.setSize(currencyId);
            }
            String symbol = result.getString(2);
            String code = result.getString(3);
            CurrencyDTO bean = new CurrencyDTO(null, code, symbol, null);
            results.add(result.getInt(1), bean);
        }
        result.close();
        stmt.close();
        conn.close();
        CurrencyDTO[] retValue = new CurrencyDTO[results.size()];
        
        return (CurrencyDTO []) results.toArray(retValue);
    }
    
    public CurrencyDTOEx[] getCurrencies(Integer languageId, 
            Integer entityId) 
            throws NamingException, SQLException, FinderException {
        Vector result = new Vector();
        
        CurrencyDTO[] all = getSymbols();
        for (int f = 1; f < all.length; f++) {
            Integer currencyId = new Integer(f);
            set(currencyId);
            CurrencyDTOEx newCurrency = new CurrencyDTOEx();
            newCurrency.setId(currencyId);
            newCurrency.setName(currency.getDescription(languageId));
            // find the system rate
            if (f == 1) {
                newCurrency.setSysRate(new Float(1));
            } else {
                newCurrency.setSysRate(currencyExchangeHome.find(new Integer(0),
                        currencyId).getRate());
            }
            // may be there's an entity rate
            try {
                EntityBL en = new EntityBL(entityId);
                newCurrency.setRate(Util.float2string(currencyExchangeHome.find(entityId,
                        currencyId).getRate(), en.getLocale()));
            } catch (FinderException e) {}
            // let's see if this currency is in use by this entity
            newCurrency.setInUse(new Boolean(entityHasCurrency(entityId, 
                    currencyId)));
            result.add(newCurrency);
        }
        CurrencyDTOEx[] retValue = new CurrencyDTOEx[result.size()];
        return (CurrencyDTOEx[]) result.toArray(retValue);
    }
    
    public void setCurrencies(Integer entityId, CurrencyDTOEx[] currencies) 
            throws NamingException, FinderException, RemoveException,
                    CreateException, ParseException {
        EntityBL entity = new EntityBL(entityId);

        // start by wiping out the existing data for this entity
        entity.getEntity().getCurrencies().clear();
        for (Iterator it = currencyExchangeHome.findByEntity(entityId).
                iterator(); it.hasNext(); ) {
            CurrencyExchangeEntityLocal exchange = 
                    (CurrencyExchangeEntityLocal) it.next();
            exchange.remove();
        }
        
        for (int f = 0; f < currencies.length; f++) {
            if (currencies[f].getInUse().booleanValue()) {
                set(currencies[f].getId());
                entity.getEntity().getCurrencies().add(currency);

                if (currencies[f].getRate() != null) {
                    currencyExchangeHome.create(entityId, currencies[f].getId(), 
                            Util.string2float(currencies[f].getRate(), 
                                    entity.getLocale()));
                }
            }
        }
    }
    
    public Integer getEntityCurrency(Integer entityId)
             throws FinderException {
        EntityEntityLocal entity = entityHome.findByPrimaryKey(entityId);
        return entity.getCurrencyId();
    }
    
    public void setEntityCurrency(Integer entityId, Integer currencyId) 
            throws FinderException {
        EntityEntityLocal entity = entityHome.findByPrimaryKey(entityId);
        entity.setCurrencyId(currencyId);
    }
    
    /**
     * Ok, this is cheating, but heck is easy and fast.
     * @param entityId
     * @param currencyId
     * @return
     * @throws SQLException
     * @throws NamingException
     */
    private boolean entityHasCurrency(Integer entityId, Integer currencyId) 
            throws SQLException, NamingException {
        boolean retValue = false;
        JNDILookup jndi = JNDILookup.getFactory();
        Connection conn = jndi.lookUpDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "select 1 " +
                "  from currency_entity_map " +
                " where currency_id = ? " +
                "   and entity_id = ?");
        stmt.setInt(1, currencyId.intValue());
        stmt.setInt(2, entityId.intValue());
        ResultSet result = stmt.executeQuery();
        if (result.next()) {
            retValue = true;
        }
        result.close();
        stmt.close();
        conn.close();
        
        return retValue;
    }
}
