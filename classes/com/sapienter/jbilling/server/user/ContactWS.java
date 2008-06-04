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
 * Created on Jan 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sapienter.jbilling.server.user;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

import com.sapienter.jbilling.server.entity.ContactDTO;

/**
 * @author Emil
 *
 * @jboss-net.xml-schema urn="sapienter:ContactWS"
 */
public class ContactWS extends ContactDTO implements Serializable {

    private String[] fieldNames = null;
    private String[] fieldValues = null;
    private Integer type = null; // the contact type
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    public void setFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }
    public String[] getFieldValues() {
        return fieldValues;
    }
    public void setFieldValues(String[] fieldValues) {
        this.fieldValues = fieldValues;
    }
    /**
     * 
     */
    public ContactWS() {
        super();
    }

    /**
     * @param id
     * @param organizationName
     * @param address1
     * @param address2
     * @param city
     * @param stateProvince
     * @param postalCode
     * @param countryCode
     * @param lastName
     * @param firstName
     * @param initial
     * @param title
     * @param phoneCountryCode
     * @param phoneAreaCode
     * @param phoneNumber
     * @param faxCountryCode
     * @param faxAreaCode
     * @param faxNumber
     * @param email
     * @param createDate
     * @param deleted
     * @param include
     */
    public ContactWS(Integer id, String organizationName, String address1,
            String address2, String city, String stateProvince,
            String postalCode, String countryCode, String lastName,
            String firstName, String initial, String title,
            Integer phoneCountryCode, Integer phoneAreaCode,
            String phoneNumber, Integer faxCountryCode, Integer faxAreaCode,
            String faxNumber, String email, Date createDate, Integer deleted,
            Integer include) {
        super(id, organizationName, address1, address2, city, stateProvince,
                postalCode, countryCode, lastName, firstName, initial, title,
                phoneCountryCode, phoneAreaCode, phoneNumber, faxCountryCode,
                faxAreaCode, faxNumber, email, createDate, deleted, include,
                null);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param otherValue
     */
    public ContactWS(ContactDTOEx otherValue) {
        super(otherValue);
        setType(otherValue.getType());
        fieldNames = new String[otherValue.getFields().size()];
        fieldValues = new String[otherValue.getFields().size()];
        int index = 0;
        for (Iterator it = otherValue.getFields().keySet().iterator();
                it.hasNext();) {
            fieldNames[index] = (String) it.next();
            ContactFieldDTOEx fieldDto = (ContactFieldDTOEx) otherValue.
                getFields().get(fieldNames[index]);
            fieldValues[index] = fieldDto.getContent();
            index++;
        }
    }

    public String toString() {
        String ret = super.toString();
        ret += " type=" + getType(); 
        if (fieldNames != null) {
            for (int f = 0; f < fieldNames.length; f++) {
                ret = ret + " " + fieldNames[f] + "=" + fieldValues[f];
            }
        }
        return ret;
    }
    public Integer getType() {
        return type;
}
    public void setType(Integer type) {
        this.type = type;
    }
}
