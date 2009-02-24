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
package com.sapienter.jbilling.server.util.db;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.util.db.generated.AgeingEntityStep;

/**
 * 
 * @author abimael
 *
 */
public class InternationalDescriptionDAS extends AbstractDAS<InternationalDescriptionDTO> {

    private final String QUERY = "SELECT a " +
            "FROM description a, jbilling_table b " +
            "WHERE a.tableId = b.id " +
            "AND b.name = :table " +
            "AND a.foreignId = :foreing ";

    public InternationalDescriptionDTO findIt(String table,
            Integer foreignId, String column, Integer language) {

        InternationalDescriptionId idi =
                new InternationalDescriptionId(Util.getTableId(table), (foreignId == null) ? 0 : foreignId, column, (language == null) ? 0 : language);

        Criteria crit = getSession().createCriteria(InternationalDescriptionDTO.class);
        crit.add(Restrictions.eq("id", idi));

        return (InternationalDescriptionDTO) crit.uniqueResult();
    }

    public InternationalDescriptionDTO create(String table, Integer foreignId, String column, Integer language, String message) {

        InternationalDescriptionId idi = new InternationalDescriptionId(Util.getTableId(table), foreignId, column, language);

        InternationalDescriptionDTO inter = new InternationalDescriptionDTO();
        inter.setId(idi);
        inter.setContent(message);

        return save(inter);

    }

    public Collection<InternationalDescriptionDTO> findByTable_Row(String table, Integer foreignId) {

        Query query = getSession().createQuery(QUERY);
        query.setParameter("table", table);
        query.setParameter("foreing", foreignId);
        return query.list();
    }
}
