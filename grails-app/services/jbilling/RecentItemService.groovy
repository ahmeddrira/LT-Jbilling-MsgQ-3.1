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

import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder

/**
 * RecentItemService
 
 * @author Brian Cowdery
 * @since  07-12-2010
 */
class RecentItemService {

    public static final Integer MAX_ITEMS = 5

    def Object getRecentItems() {
        return RecentItem.findAllByUserId(session['user_id'])
    }

    def void addRecentItem(Integer objectId, RecentItemType type) {
        addRecentItem(new RecentItem(objectId: objectId, type: type))
    }

    def void addRecentItem(RecentItem item) {
        def items = getRecentItems()

        item.userId = session['user_id']
        item.save()
        
        items << item
        if (items.size() > MAX_ITEMS)
            items.remove(0).delete()
    }

    /**
     * Returns the HTTP session
     *
     * @return http session
     */
    def HttpSession getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

}
