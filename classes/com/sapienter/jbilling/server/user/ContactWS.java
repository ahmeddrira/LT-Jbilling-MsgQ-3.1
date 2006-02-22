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
