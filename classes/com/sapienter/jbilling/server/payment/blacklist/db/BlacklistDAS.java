/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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
package com.sapienter.jbilling.server.payment.blacklist.db;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class BlacklistDAS extends AbstractDAS<BlacklistDTO> {
    
    public List<BlacklistDTO> findByEntity(Integer entityId) {
        // I need to access an association, so I can't use the parent helper class
        Criteria criteria = getSession().createCriteria(BlacklistDTO.class)
                .createAlias("company", "c")
                    .add(Restrictions.eq("c.id", entityId));

        return criteria.list();
    }

    public List<BlacklistDTO> findByEntityType(Integer entityId, Integer type) {
        Criteria criteria = getSession().createCriteria(BlacklistDTO.class)
                .createAlias("company", "c")
                    .add(Restrictions.eq("c.id", entityId))
                .add(Restrictions.eq("type", type));

        return criteria.list();    
    }

    public List<BlacklistDTO> findByEntitySource(Integer entityId, Integer source) {
        Criteria criteria = getSession().createCriteria(BlacklistDTO.class)
                .createAlias("company", "c")
                    .add(Restrictions.eq("c.id", entityId))
                .add(Restrictions.eq("source", source));

        return criteria.list();
    }

    public List<BlacklistDTO> findByUser(Integer userId) {
        Criteria criteria = getSession().createCriteria(BlacklistDTO.class)
                .createAlias("user", "u")
                    .add(Restrictions.eq("u.id", userId));

        return criteria.list();
    }

    public List<BlacklistDTO> findByUserType(Integer userId, Integer type) {
        Criteria criteria = getSession().createCriteria(BlacklistDTO.class)
                .createAlias("user", "u")
                    .add(Restrictions.eq("u.id", userId))
                .add(Restrictions.eq("type", type));

        return criteria.list();
    }    
}
