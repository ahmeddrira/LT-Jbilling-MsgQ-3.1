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
 * Created on Oct 7, 2004
 *
 */
package com.sapienter.jbilling.server.order;

import java.util.Calendar;

/**
 * @author Emil
 *
 */
public class TimePeriod {
    private Integer unitId = null;
    private Integer value = null;
    private Boolean df_fm = null;
    private Long own_invoice = null;
    
    public boolean equals(Object another) {
        boolean retValue = false;
        
        if (another != null) {
            TimePeriod other = (TimePeriod) another;
            if (unitId.equals(other.getUnitId()) &&
                    value.equals(other.getValue())) {
                if (df_fm == null && other.getDf_fm() == null) {
                    retValue = true;
                } else if (df_fm != null && other.getDf_fm() != null &&
                        df_fm.booleanValue() == 
                            other.getDf_fm().booleanValue()){
                    retValue = true;
                }
            }
            
            if (retValue) {
                retValue = own_invoice.equals(other.getOwn_invoice());
            }
        }
        return retValue;
    }
    
    /*
     * No need to add the own invoice here. You can return the same hash code
     * for two unequal objects.
     */
    public int hashCode() {
        int dfValue;
        if (df_fm == null) {
            dfValue = 0;
        } else if (df_fm.booleanValue()) {
            dfValue = 1;
        } else {
            dfValue = 2;
        }
        return unitId.intValue() * 100 + value.intValue() * 10 + dfValue;
    }
    
    public String toString() {
        return "Period unit " + unitId + " value " + value + " Df Fm " + df_fm;
    }
    public Integer getUnitId() {
        return unitId;
    }
    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
    public Boolean getDf_fm() {
        return df_fm;
    }
    public void setDf_fm(Boolean df_fm) {
        this.df_fm = df_fm;
    }
    public void setDf_fm(Integer df_fm) {
        if (df_fm == null) {
            this.df_fm = null;
        } else {
            this.df_fm = new Boolean(df_fm.intValue() == 1);
        }
    }

    public Long getOwn_invoice() {
        return own_invoice;
    }
    public void setOwn_invoice(Integer own_invoice) {
        if (own_invoice != null && own_invoice.intValue() == 1) {
            // give a unique number to it
            Calendar cal = Calendar.getInstance();
            this.own_invoice = new Long(cal.getTimeInMillis());
        } else {
            this.own_invoice = new Long(0);
        }
    }
}
