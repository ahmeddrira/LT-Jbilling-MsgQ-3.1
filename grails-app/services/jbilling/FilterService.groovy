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

    private static final String SESSION_CURRENT_FILTER_TYPE = "current_filter_type";

    /**
     * Fetches the filters for the given type and sets the filter values from the UI
     * input fields if the "applyFilter" request parameter is present.
     *
     * Filters are available in the session (and can also be set elsewhere in the session)
     * using the key FilterType.name() + "_FILTERS".
     *
     * @param type filter type
     * @param params request parameters from controller action
     * @return filters for the given filter type
     */
    def Object getFilters(FilterType type, GrailsParameterMap params) {
        def session = getSession()
        def key = getSessionKey(type)

        /*
            Fetch filters for the given filter type. If the filters are already
            in the session, use the existing filters instead of fetching new ones.
         */
        def filters = [];
        if (!session[key]) {
            filters = Filter.withCriteria {
                or {
                    eq("type", FilterType.ALL)
                    eq("type", type)
                }
            }
        } else {
            filters = session[key]
        }

        // update filters with values from request parameters
        if (params?.boolean("applyFilter")) {
            filters.each {
                it.stringValue = params["filters.${it.id}.stringValue"]
                it.integerValue = params["filters.${it.id}.integerValue"] ? Integer.valueOf((String) params["filters.${it.id}.integerValue"]) : null
                it.startDateValue = params["filters.${it.id}.startDateValue"] ? DATE_FORMAT.parse((String) params["filters.${it.id}.startDateValue"]) : null
                it.endDateValue = params["filters.${it.id}.endDateValue"] ? DATE_FORMAT.parse((String) params["filters.${it.id}.endDateValue"]) : null
            }
        }

        // store filters in session for next request
        session[SESSION_CURRENT_FILTER_TYPE] = type;
        session[key] = filters
        return filters
    }
        
    /**
     * Changes the visibility of a filter so that it appears in the filter pane.
     *
     * @param id filter id to show
     * @return updated filter list
     */
    def Object showFilter(Integer id) {
        def type = (FilterType) session[SESSION_CURRENT_FILTER_TYPE]
        def filters = getFilters(type, null)

        filters?.each{
            if (it.id == id)
                it.visible = true 
        }

        session[getSessionKey(type)] = filters
        return filters
    }

    /**
     * Changes the visibility of the filter so that it is removed from the filter pane. This
     * method also clears the filter's set value so that it's effect on the entity criteria
     * will be removed.
     *
     * @param id filter id to remove
     * @return updated filter list
     */
    def Object removeFilter(Integer id) {
        def type = (FilterType) session[SESSION_CURRENT_FILTER_TYPE]
        def filters = getFilters(type, null)

        filters?.each{
            if (it.id == id) {
                it.visible = false
                it.clear()
            }
        }

        session[getSessionKey(type)] = filters
        return filters
    }

    /**
     * Returns the HTTP session
     *
     * @return http session
     */
    def HttpSession getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    /**
     * Returns the session attribute key for the given set of filters.
     *
     * @param type filter type
     * @return session attribute key
     */
    def String getSessionKey(FilterType type) {
        return "${type.name()}_FILTERS"
    }
}
