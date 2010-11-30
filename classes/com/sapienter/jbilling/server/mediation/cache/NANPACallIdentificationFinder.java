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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * NANPACallIdentificationFinder to query call destination data based on the called international
 * code, area code and sub-area code.
 *
 * Rules:
 * <code>
 *  function String getAreaCode(String number) {
 *      return number.length() >= 10 ? number.substring(number.length() - 10, number.length() - 7) : null;
 *  }
 *
 *  function String getSubareaCode(String number) {
 *      return number.length() >= 7 ? number.substring(number.length() - 7, number.length() - 4) : null;
 *  }
 *
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
 *
 *      String areaCode = getAreaCode($dst);
 *      String subareaCode = getSubareaCode($dst);
 *      String destination = finder.findCallDescription("1", areaCode, subareaCode);
 *
 *      $result.setDescription($result.getDescription() + " " + destination);
 *
 *      LOG.debug("Found call destination '" + destination + "', for area: " + areaCode
 *                + " sub-area: " + subareaCode);
 *
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

    public String findCallDescription(String internationalCode, String areaCode, String subareaCode) {       
        return (String) jdbcTemplate.query(
                "select description "
                + " from " + loader.getTableName()
                + " where intl_code = ? "
                + " and area_code = ? "
                + " and subarea_code = ?",
                new Object[] {
                        internationalCode,
                        areaCode,
                        subareaCode
                },
                new ResultSetExtractor() {
                    public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                        if (rs.next()) {
                            return rs.getString("description");
                        }
                        LOG.debug("No call identification data found.");
                        return null;
                    }
                });
        
    }
}
