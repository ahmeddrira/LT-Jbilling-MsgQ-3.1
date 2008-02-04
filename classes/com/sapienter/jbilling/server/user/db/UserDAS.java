/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
package com.sapienter.jbilling.server.user.db;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.common.CommonConstants;
import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class UserDAS extends AbstractDAS<BaseUser> {
	public BaseUser findRoot(String username) {
		// I need to access an association, so I can't use the parent helper class
		Criteria criteria = getSession().createCriteria(BaseUser.class)
			.add(Restrictions.eq("userName", username))
			.add(Restrictions.eq("deleted", 0))
			.createAlias("roles", "r")
				.add(Restrictions.eq("r.id", CommonConstants.TYPE_ROOT));
		
		criteria.setCacheable(true); // it will be called over an over again
		
		return (BaseUser) criteria.uniqueResult();
	}

	public BaseUser findByUserName(String username, Integer entityId) {
		// I need to access an association, so I can't use the parent helper class
		Criteria criteria = getSession().createCriteria(BaseUser.class)
				.add(Restrictions.eq("userName", username))
				.add(Restrictions.eq("deleted", 0))
				.createAlias("company", "e")
					.add(Restrictions.eq("e.id", entityId));
		
		return (BaseUser) criteria.uniqueResult();

	}
}
