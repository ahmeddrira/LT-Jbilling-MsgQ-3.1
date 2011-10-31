/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.user;

import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.process.db.PeriodUnitDTO;
import com.sapienter.jbilling.server.user.partner.PartnerWS;
import com.sapienter.jbilling.server.user.partner.db.Partner;
import com.sapienter.jbilling.server.util.RemoteContext;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;
import com.sapienter.jbilling.server.util.api.SpringAPI;
import junit.framework.TestCase;
import org.joda.time.DateMidnight;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Test of Partner web-service API
 *
 * @author Brian Cowdery
 * @since 31-Oct-2011
 */
public class PartnerWSTest extends TestCase {

    private static final Integer ADMIN_USER_ID = 2;
    private static final Integer PARTNER_1_USER_ID = 10;
    private static final Integer PARTNER_2_USER_ID = 11;
    private static final Integer PARTNER_3_USER_ID = 12;

    private static final Integer PARTNER_ROLE_ID = 4;

    public PartnerWSTest() {
        super();
    }

    public PartnerWSTest(String name) {
        super(name);
    }

    public void testCreatePartner() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        // new partner
        UserWS user = new UserWS();
        user.setUserName("partner-01-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(PARTNER_ROLE_ID);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Partner Test");
        contact.setLastName("create new");
        user.setContact(contact);

        PartnerWS partner = new PartnerWS();
        partner.setRelatedClerkUserId(ADMIN_USER_ID);
        partner.setBalance("0.00");
        partner.setPercentageRate("5.00"); // 5%
        partner.setReferralFee("10.00");   // $10.00 per referral
        partner.setFeeCurrencyId(1);
        partner.setOneTime(false);         // referral fee is recurring (not one time)
        partner.setPeriodUnitId(PeriodUnitDTO.MONTH); // payout once monthly
        partner.setPeriodValue(1);
        partner.setNextPayoutDate(new DateMidnight(2011, 1, 1).toDate()); // January 1, 2011
        partner.setAutomaticProcess(true);


        // create partner
        Integer partnerId = api.createPartner(user, partner);
        partner = api.getPartner(partnerId);

        assertNotNull("partner created", partner);
        assertNotNull("partner has an id", partner.getId());

        // validate partner attributes to make sure it saved correctly
        assertEquals(BigDecimal.ZERO, partner.getBalanceAsDecimal());
        assertEquals(new BigDecimal("10.00"), partner.getReferralFeeAsDecimal());
        assertEquals(1, partner.getFeeCurrencyId().intValue());
        assertEquals(PeriodUnitDTO.MONTH, partner.getPeriodUnitId().intValue());
        assertEquals(1, partner.getPeriodValue().intValue());
        assertEquals(new DateMidnight(2011, 1, 1).toDate(), partner.getNextPayoutDate());
        assertFalse(partner.getOneTime());
        assertTrue(partner.getAutomaticProcess());


        // cleanup
        api.deletePartner(partnerId);
    }

    public void testUpdatePartner() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        // new partner
        UserWS user = new UserWS();
        user.setUserName("partner-02-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(PARTNER_ROLE_ID);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Partner Test");
        contact.setLastName("update");
        user.setContact(contact);

        PartnerWS partner = new PartnerWS();
        partner.setRelatedClerkUserId(ADMIN_USER_ID);
        partner.setBalance("0.00");
        partner.setPercentageRate("5.00"); // 5%
        partner.setReferralFee("10.00");   // $10.00 per referral
        partner.setFeeCurrencyId(1);
        partner.setOneTime(false);         // referral fee is recurring (not one time)
        partner.setPeriodUnitId(PeriodUnitDTO.MONTH); // payout once monthly
        partner.setPeriodValue(1);
        partner.setNextPayoutDate(new DateMidnight(2011, 1, 1).toDate()); // January 1, 2011
        partner.setAutomaticProcess(true);


        // create partner
        Integer partnerId = api.createPartner(user, partner);
        partner = api.getPartner(partnerId);

        assertNotNull("partner created", partner);


        // update some attributes and save
        partner.setBalance("10.00");
        partner.setReferralFee("99.00");
        partner.setOneTime(true);
        partner.setNextPayoutDate(new DateMidnight(2011, 11, 1).toDate()); // November 1, 2011

        // just save changes to partner, nothing changes on the base user
        api.updatePartner(null, partner);
        partner = api.getPartner(partnerId);


        // validate partner attributes to make sure it saved correctly
        assertEquals(new BigDecimal("10.00"), partner.getBalanceAsDecimal());
        assertEquals(new BigDecimal("99.00"), partner.getReferralFeeAsDecimal());
        assertEquals(new DateMidnight(2011, 11, 1).toDate(), partner.getNextPayoutDate());
        assertTrue(partner.getOneTime());


        // cleanup
        api.deletePartner(partnerId);
    }

    public void testDeletePartner() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        // new partner
        UserWS user = new UserWS();
        user.setUserName("partner-02-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(PARTNER_ROLE_ID);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Partner Test");
        contact.setLastName("delete");
        user.setContact(contact);

        PartnerWS partner = new PartnerWS();
        partner.setRelatedClerkUserId(ADMIN_USER_ID);
        partner.setBalance("0.00");
        partner.setPercentageRate("5.00"); // 5%
        partner.setReferralFee("10.00");   // $10.00 per referral
        partner.setFeeCurrencyId(1);
        partner.setOneTime(false);         // referral fee is recurring (not one time)
        partner.setPeriodUnitId(PeriodUnitDTO.MONTH); // payout once monthly
        partner.setPeriodValue(1);
        partner.setNextPayoutDate(new DateMidnight(2011, 1, 1).toDate()); // January 1, 2011
        partner.setAutomaticProcess(true);


        // create partner
        Integer partnerId = api.createPartner(user, partner);
        partner = api.getPartner(partnerId);

        assertNotNull("partner created", partner);


        // delete partner
        api.deletePartner(partner.getId());


        // verify that partner cannot be fetched after deleting
        try {
            api.getPartner(partner.getId());
            fail("deleted partner should throw exception");
        } catch (SessionInternalError e) {
            assertTrue(e.getMessage().contains("No row with the given identifier exists"));
        }

        // verify that the base user was deleted with the partner
        UserWS deletedUser = api.getUserWS(partner.getUserId());
        assertEquals(1, deletedUser.getDeleted());
    }

    public void testGetPartner() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        // partner that does not exist throws exception
        try {
            api.getPartner(999);
            fail("non-existent partner should throw exception");
        } catch (SessionInternalError e) {
            assertTrue(e.getMessage().contains("No row with the given identifier exists"));
        }

        // partner belonging to a different entity throws a security exception
        try {
            api.getPartner(20); // belongs to entity 2
            fail("partner does not belong to entity 1, should throw security exception.");
        } catch (SecurityException e) {
            assertTrue(e.getMessage().contains("Unauthorized access to entity 2"));
        }
    }

    public void testReferralFee() throws Exception {
        // partner is linked to a customer

        // partner receives a referral fee
    }

    public void testCommission() throws Exception {
        // linked customer creates an order

        // partner receives a commission as a percentage of the order total
    }



    /*
        Convenience assertions for BigDecimal comparison
     */

    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, BigDecimal expected, BigDecimal actual) {
        assertEquals(message,
                     (Object) (expected == null ? null : expected.setScale(2, RoundingMode.HALF_UP)),
                     (Object) (actual == null ? null : actual.setScale(2, RoundingMode.HALF_UP)));
    }
}
