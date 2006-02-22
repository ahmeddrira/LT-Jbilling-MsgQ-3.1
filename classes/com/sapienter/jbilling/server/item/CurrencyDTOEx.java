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
