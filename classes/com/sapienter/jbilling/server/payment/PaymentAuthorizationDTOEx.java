package com.sapienter.jbilling.server.payment;

import java.io.Serializable;

import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;

/**
 * @author Emil
 * @jboss-net.xml-schema urn="sapienter:PaymentAuthorizationDTOEx"
 */
public class PaymentAuthorizationDTOEx extends PaymentAuthorizationDTO implements Serializable {
    private Boolean result;
    
    public PaymentAuthorizationDTOEx() {
        super();
    }
    
    public PaymentAuthorizationDTOEx(PaymentAuthorizationDTO dto) {
        super(dto);
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
    
}
