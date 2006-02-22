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
 * Created on Nov 19, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.pluggableTask;

import com.sapienter.jbilling.server.entity.PluggableTaskParameterDTO;

/**
 * @author Emil
 */
public class PluggableTaskParameterDTOEx extends PluggableTaskParameterDTO {

    private Integer type = null; // this indicates the data type of the value
    private String value = null;
    
    public static final int INT = 1;
    public static final int STR = 2;
    public static final int FLO = 3;
    
    /**
     * 
     */
    public PluggableTaskParameterDTOEx() {
        super();
    }

    /**
     * @param id
     * @param name
     * @param intValue
     * @param strValue
     * @param floatValue
     */
    public PluggableTaskParameterDTOEx(
        Integer id,
        String name,
        Integer intValue,
        String strValue,
        Float floatValue) {
        super(id, name, intValue, strValue, floatValue);
    }
    
    public void populateValue() {
        if (getIntValue() != null) {
            type = new Integer(INT);
            value = String.valueOf(getIntValue());
        } else if (getStrValue() != null) {
            type = new Integer(STR);
            value = getStrValue();
        } else if (getFloatValue() != null) {
            type = new Integer(FLO);
            value = String.valueOf(getFloatValue());
        } else {
        	// the value of this parameer is null
        	// we default the type to String
        	type = new Integer(STR);
        }
    }
    
    public void expandValue() 
            throws NumberFormatException {
        switch(type.intValue()) {
        case INT:
            setIntValue(Integer.valueOf(value));
            setStrValue(null);
            setFloatValue(null);  
        break;
        case STR:
            setIntValue(null);
            setStrValue(value);
            setFloatValue(null);  
        break;
        case FLO:
            setIntValue(null);
            setStrValue(null);
            setFloatValue(Float.valueOf(value));  
        break;
        }
    }

    /**
     * @param otherValue
     */
    public PluggableTaskParameterDTOEx(PluggableTaskParameterDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public Integer getType() {
        return type;
    }


    /**
     * @return
     */
    public String getValue() {
        return value;
    }


    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

}
