
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

/**
 * SortableTagLib 
 *
 * @author Brian Cowdery
 * @since 08/06/11
 */
class SortableTagLib {

    def remoteSort = { attrs, body ->
        def sort = assertAttribute('sort', attrs, 'remoteSort') as String
        def order = params.sort == sort ? params.order == 'desc' ? 'asc' : 'desc' : null

        def action = assertAttribute('action', attrs, 'remoteSort') as String
        def controller = params.controller ?: controllerName

        def update = assertAttribute('update', attrs, 'remoteSort') as String

        out << render(template: "/sortable",
                      params: params,
                      model: [
                              sort: sort,
                              order: order,
                              action: action,
                              controller: controller,
                              update: update,
                              body: body()
                      ]
        )
    }

    protected assertAttribute(String name, attrs, String tag) {
        if (!attrs.containsKey(name)) {
            throwTagError "Tag [$tag] is missing required attribute [$name]"
        }
        attrs.remove name
    }
}