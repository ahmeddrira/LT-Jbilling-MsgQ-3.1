/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.mediation.cache;

import com.sapienter.jbilling.server.util.Context;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * NANPACallIdentificationFinder to query call destination data based on the called international
 * code, area code and sub-area code.
 *
 * Rules:
 * <code>
 *  rule 'Resolve Call Destination'
 *  no-loop
 *  dialect 'java'
 *  when
 *      $result : MediationResult( step == MediationResult.STEP_4_RESOLVE_ITEM,
 *                                  description != null,
 *                                  lines.empty == false )
 *
 *      PricingField( name == "dst", $dst : strValue != null, resultId == $result.id )
 *
 *      # Only for international calls
 *      OrderLineDTO( itemId == 40 ) from $result.lines
 *
 *  then
 *      NANPACallIdentificationFinder finder = NANPACallIdentificationFinder.getInstance();
 *      String destination = finder.findCallDescription($dst);
 *
 *      $result.setDescription($result.getDescription() + " " + destination);
 *
 *      LOG.debug("Found call destination '" + destination + "', for: " + $dst);
 *  end
 * </code>
 *
 * See descriptors/spring/jbilling-caching.xml for configuration
 *
 * @author Brian Cowdery
 * @since 29-11-2010
 */
public class NANPACallIdentificationFinder extends AbstractFinder {

    private static final Logger LOG = Logger.getLogger(NANPACallIdentificationFinder.class);
    
    private static final String INTERNATIONAL_PREFIX_REGEX = "^\\+?011";
    private static final String BLANK = "";
    private static final String DEFAULT_COUNTRY_CODE = "1";
    private static final String DEFAULT_DESCRIPTION = "Unknown";

    private static final Integer MAX_COUNTRY_CODE_LENGTH = 3;
    private static final Integer MAX_AREA_CODE_LENGTH = 4;
    private static final Integer MAX_SUBAREA_CODE_LENGTH = 3;

    public static NANPACallIdentificationFinder getInstance() {
        Object bean = Context.getBean(Context.Name.NANPA_CALL_IDENTIFICATION_FINDER);
        return (NANPACallIdentificationFinder) bean;
    }

    public NANPACallIdentificationFinder(JdbcTemplate template, ILoader loader) {
        super(template, loader);
    }

    public void init() {
        // noop
    }

    public String findCallDescription(String number) {
        LOG.debug("Identifying call '" + number + "'");

        number = number.replaceFirst(INTERNATIONAL_PREFIX_REGEX, BLANK);
        String countryCode = getCountryCode(number);
        String areaCode = getAreaCode(countryCode, number);
        String subareaCode = getSubAreaCode(countryCode, areaCode, number);

        LOG.debug("Country code: '" + countryCode
                  + "', Area code: '" + areaCode
                  + "', Subarea code: '" + subareaCode + "'");

        return (String) jdbcTemplate.query(
                "select description "
                + " from " + loader.getTableName()
                + " where intl_code = ? "
                + " and area_code = ? "
                + " and subarea_code = ?",
                new Object[] {
                        countryCode,
                        areaCode,
                        subareaCode
                },
                new ResultSetExtractor() {
                    public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                        if (rs.next()) {
                            return rs.getString("description");
                        }
                        LOG.debug("No call identification data found.");
                        return DEFAULT_DESCRIPTION;
                    }
                });
        
    }

    /**
     * Searches for the country code of the given phone number.
     *
     * @param number phone number
     * @return found country code
     */
    public String getCountryCode(String number) {
        LOG.debug("Getting country code from " + number);

        int length = MAX_COUNTRY_CODE_LENGTH;
        number = getDigits(number, length);

        while (length >= 0) {
            LOG.debug("Searching for country code: " + number);

            SqlRowSet rs = jdbcTemplate.queryForRowSet(
                    "select distinct(intl_code) "
                    + " from " + loader.getTableName()
                    + " where intl_code = ?",
                    new Object[] {
                            number
                    }
            );

            if (rs.next()) {
                return rs.getString("intl_code");
            } else {
                length--;
                number = getDigits(number, length);
            }
        }
        
        // default country code
        return DEFAULT_COUNTRY_CODE;
    }

    /**
     * Searches for the area code of the given phone number and country code.
     *
     * @param countryCode country code
     * @param number phone number
     * @return found area code
     */
    public String getAreaCode(String countryCode, String number) {
        LOG.debug("Getting area code for " + countryCode + " from " + number);

        if (countryCode != null)
            number = number.replaceFirst(countryCode, BLANK);

        int length = MAX_AREA_CODE_LENGTH;
        number = getDigits(number, length);

        while (length >= 0) {
            LOG.debug("Searching for area code: " + number);

            SqlRowSet rs = jdbcTemplate.queryForRowSet(
                    "select distinct(area_code) "
                    + " from " + loader.getTableName()
                    + " where intl_code = ? "
                    + " and area_code = ?",
                    new Object[] {
                            countryCode,
                            number
                    }
            );

            if (rs.next()) {
                return rs.getString("area_code");
            } else {
                length--;
                number = getDigits(number, length);
            }
        }

        return null;
    }

    /**
     * Searches for the sub-area code of the given phone number, area code, and country code.
     *
     * @param countryCode country code
     * @param areaCode phone number
     * @param number phone number
     * @return found sub-area code
     */
    public String getSubAreaCode(String countryCode, String areaCode, String number) {
        LOG.debug("Getting area code for " + countryCode + " " + areaCode + " from " + number);

        if (countryCode != null)
            number = number.replaceFirst(countryCode, BLANK);

        if (areaCode != null)
            number = number.replaceFirst(areaCode, BLANK);

        int length = MAX_SUBAREA_CODE_LENGTH;
        number = getDigits(number, length);

        while (length >= 0) {
            LOG.debug("Searching for sub-area code: " + number);

            SqlRowSet rs = jdbcTemplate.queryForRowSet(
                    "select distinct(subarea_code) "
                    + " from " + loader.getTableName()
                    + " where intl_code = ? "
                    + " and area_code = ? " 
                    + " and subarea_code = ?",
                    new Object[] {
                            countryCode,
                            areaCode,
                            number
                    }
            );

            if (rs.next()) {
                return rs.getString("subarea_code");
            } else {
                length--;
                number = getDigits(number, length);
            }
        }

        return null;
    }

    /**
     * Shortens a given number string down to the given length by removing the
     * trailing numbers. If the length is zero or less-than zero, the string "0"
     * will be returned as a possible wildcard value for lookup.
     *
     * @param length desired length
     * @param number to shorten
     * @return country code shortened to the desired length
     */
    public String getDigits(String number, int length) {
        if (length <= 0) return "0";
        return number.length() > length ? number.substring(0, length) : number;
    }
}
