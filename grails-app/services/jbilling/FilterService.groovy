package jbilling

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder
import java.text.SimpleDateFormat
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

/**
 * FilterService

 * @author Brian Cowdery
 * @since  30-11-2010
 */
class FilterService {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy")

    public static final String SESSION_FILTER_TYPE = "filter_type";
    public static final String SESSION_FILTERS = "filters";

    def HttpSession getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    def Object getFilters(FilterType type, GrailsParameterMap params) {
        def session = getSession();

        /*
            Fetch filters for the given filter type. If the filters are already
            in the session, use the existing filters instead of fetching new ones.
         */
        def filters = [];
        if (session[SESSION_FILTER_TYPE] != type) {
            filters = Filter.withCriteria {
                or {
                    eq("type", FilterType.ALL)
                    eq("type", type)
                }
            }

            session[SESSION_FILTER_TYPE] = type
            
        } else {
            filters = session[SESSION_FILTERS]
        }

        // update filters with values from request parameters
        if (params["applyFilter"]) {
            filters.each {
                it.stringValue = params["filters.${it.id}.stringValue"]
                it.integerValue = params["filters.${it.id}.integerValue"] ? Integer.valueOf((String) params["filters.${it.id}.integerValue"]) : null
                it.startDateValue = params["filters.${it.id}.startDateValue"] ? DATE_FORMAT.parse((String) params["filters.${it.id}.startDateValue"]) : null
                it.endDateValue = params["filters.${it.id}.endDateValue"] ? DATE_FORMAT.parse((String) params["filters.${it.id}.endDateValue"]) : null
            }
        }

        session[SESSION_FILTERS] = filters
        return filters
    }
}
