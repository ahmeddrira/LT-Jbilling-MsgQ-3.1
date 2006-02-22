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
 * Created on 19-Feb-2003
 *
 */
package com.sapienter.jbilling.client.user;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author Emil
 */
public final class UserLoginForm
    extends ValidatorForm
    implements Serializable {

    /**
     * The username.
     */
    private String sUserName = null;
    
    private String password = null;
    
    private String entityId = null;

    /**
     * Return the username.
     */
    public String getUserName() {
        return (this.sUserName);
    }

    /**
     * Set the username.
     *
     * @param username The new username
     */
    public void setUserName(String username) {
        this.sUserName = username;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.sUserName = null;
        this.password = null;
    }

    /**
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

}
