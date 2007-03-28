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

package com.sapienter.jbilling.server.pluggableTask;

import java.io.Serializable;

import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;

public class AuthorizeNetResponseDTO implements Serializable {
    private PaymentAuthorizationDTO dbRow = null;
    
    public AuthorizeNetResponseDTO(String rawResponse) {
        // wow, how easy this is ?!! :)
        String fields[] = rawResponse.split(",", -1);
        dbRow = new PaymentAuthorizationDTO();
        
        dbRow.setCode1(fields[0]); // code 
        dbRow.setCode2(fields[1]); // subcode
        dbRow.setCode3(fields[2]); // reason code
        dbRow.setResponseMessage(fields[3]); // a string with plain text with a reason for this result
        dbRow.setApprovalCode(fields[4]); 
        dbRow.setAVS(fields[5]);
        dbRow.setTransactionId(fields[6]);
        dbRow.setMD5(fields[37]);
        dbRow.setCardCode(fields[38]);        
    }
    
    public String toString() {
        return "[" +
            "code=" + dbRow.getCode1() + "," +
        "subCode=" + dbRow.getCode2() + "," +
        "reasonCode=" + dbRow.getCode3() + "," +
        "reasonText=" + dbRow.getResponseMessage() + "," +
        "approvalCode=" + dbRow.getApprovalCode() + "," +
        "AVSResultCode=" + dbRow.getAVS() + "," +
        "transactionId=" + dbRow.getTransactionId() + "," +
        "MD5Hash=" + dbRow.getMD5() + "," +
        "cardCode=" + dbRow.getCardCode() + 
        "]";
    }
    
    public PaymentAuthorizationDTO getPaymentAuthorizationDTO() {
        return dbRow;
    }

}
