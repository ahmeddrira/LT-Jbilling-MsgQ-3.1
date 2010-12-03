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

package jbilling

/**
 * FilterController
 
 * @author Brian Cowdery
 * @since  03-12-2010
 */
class FilterController {

    def filterService

    def add = {
        def filters = filterService.showFilter(params.int("id"))
        render template: "filters", model:[filters: filters]
    }

    def remove = {
        def filters = filterService.removeFilter(params.int("id"))
        render template: "filters", model:[filters: filters]
    }
    
    def save = {

    }

    def load = {

    }
}
