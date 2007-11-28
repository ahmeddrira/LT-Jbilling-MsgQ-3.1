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
 * Created on Mar 20, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.item;

import com.sapienter.jbilling.server.entity.CurrencyDTO;

/**
 * This is mostly for the screen of currency edition :)
 * @author Emil
 */
public class CurrencyDTOEx extends CurrencyDTO {

    private String name = null;
    private Boolean inUse = null;
    private String rate = null; // will be converted to float
    private Float sysRate = null;
    /**
     * 
     */
    public CurrencyDTOEx() {
        super();
    }

    /**
     * @param id
     * @param code
     * @param symbol
     * @param countryCode
     */
    public CurrencyDTOEx(Integer id, String code, String symbol,
            String countryCode) {
        super(id, code, symbol, countryCode);
    }

    /**
     * @param otherValue
     */
    public CurrencyDTOEx(CurrencyDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public Boolean getInUse() {
        return inUse;
    }

    /**
     * @param isInUse
     */
    public void setInUse(Boolean isInUse) {
        this.inUse = isInUse;
    }

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

    /**
     * @return
     */
    public String getRate() {
        return rate;
    }

    /**
     * @param rate
     */
    public void setRate(String rate) {
        this.rate = rate;
    }

    /**
     * @return
     */
    public Float getSysRate() {
        return sysRate;
    }

    /**
     * @param sysRate
     */
    public void setSysRate(Float sysRate) {
        this.sysRate = sysRate;
    }

}
