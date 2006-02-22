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
 * Created on Mar 30, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.process;

import com.sapienter.jbilling.server.entity.AgeingEntityStepDTO;

/**
 * @author Emil
 */
public class AgeingDTOEx extends AgeingEntityStepDTO {
    private Integer statusId = null;
    private String statusStr = null;
    private String welcomeMessage = null;
    private String failedLoginMessage = null;
    private Boolean inUse = null;
    private Integer canLogin = null;
    
    /**
     * @return
     */
    public String getFailedLoginMessage() {
        return failedLoginMessage;
    }

    /**
     * @param failedLoginMessage
     */
    public void setFailedLoginMessage(String failedLoginMessage) {
        this.failedLoginMessage = failedLoginMessage;
    }

    /**
     * @return
     */
    public Integer getStatusId() {
        return statusId;
    }

    /**
     * @param statusId
     */
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    /**
     * @return
     */
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    /**
     * @param welcomeMessage
     */
    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    /**
     * @return
     */
    public Boolean getInUse() {
        return inUse;
    }

    /**
     * @param inUse
     */
    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    /**
     * @return
     */
    public String getStatusStr() {
        return statusStr;
    }

    /**
     * @param statusStr
     */
    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    /**
     * @return
     */
    public Integer getCanLogin() {
        return canLogin;
    }

    /**
     * @param canLogin
     */
    public void setCanLogin(Integer canLogin) {
        this.canLogin = canLogin;
    }

}
