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
package com.sapienter.jbilling.server.payment.blacklist;

import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import com.sapienter.jbilling.common.JBCrypto;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.payment.blacklist.db.BlacklistDAS;
import com.sapienter.jbilling.server.payment.blacklist.db.BlacklistDTO;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.util.Util;
import com.sapienter.jbilling.server.util.db.generated.CreditCard;

/**
 * Filters credit card numbers.
 */
public class CreditCardFilter implements BlacklistFilter {

    public Result checkPayment(PaymentDTOEx paymentInfo) {
        if (paymentInfo.getCreditCard() != null) {
            List<CreditCard> creditCards = new Vector<CreditCard>(1);
            // need to convert EJB 2 entity DTO type to Hibernate DTO type
            CreditCard creditCard = new CreditCard();
            // DB compares encrypted data
            creditCard.setCcNumber(JBCrypto.getCreditCardCrypto().encrypt(
                    paymentInfo.getCreditCard().getNumber()));
            creditCards.add(creditCard);

            return checkCreditCard(paymentInfo.getUserId(), 
                    creditCards);
        }
        // not paying by credit card, so accept?
        return new Result(false, null);
    }

    public Result checkUser(Integer userId) {
        UserDTO user = new UserDAS().find(userId);
        return checkCreditCard(userId, user.getCreditCards());
    }

    public Result checkCreditCard(Integer userId, Collection<CreditCard> creditCards) {
        if (creditCards.isEmpty()) {
            return new Result(false, null);
        }

        // create a list of credit card numbers
        List<String> ccNumbers = new Vector<String>(creditCards.size());
        for (CreditCard cc : creditCards) {
            ccNumbers.add(cc.getCcNumber());
        }

        Integer entityId = new UserDAS().find(userId).getCompany().getId();
        List<BlacklistDTO> blacklist = new BlacklistDAS().filterByCcNumbers(
                entityId, ccNumbers);

        if (!blacklist.isEmpty()) {
            ResourceBundle bundle = Util.getEntityNotificationsBundle(userId);
            return new Result(true, 
                    bundle.getString("payment.blacklist.cc_number_filter"));
        }

        return new Result(false, null);
    }

    public String getName() {
        return "Credit card number blacklist filter";
    }
}
