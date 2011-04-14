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

package jbilling

import com.sapienter.jbilling.server.payment.blacklist.db.BlacklistDTO

class BlacklistConfigController {

    def index = {
        redirect action: list, params: params
    }

    def list = {
        def blacklist = BlacklistDTO.createCriteria().list() {
            eq('company.id', session['company_id'])
            order('id', 'asc')
        }

        def selected = params.id ? BlacklistDTO.get(params.int('id')) : null

        [ blacklist: blacklist, selected: selected ]
    }

    def show = {
        def entry = BlacklistDTO.get(params.int('id'))

        render template: 'show', model: [ selected: entry ]
    }
}
