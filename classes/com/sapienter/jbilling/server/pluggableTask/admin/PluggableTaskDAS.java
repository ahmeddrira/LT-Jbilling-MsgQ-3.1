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
package com.sapienter.jbilling.server.pluggableTask.admin;

import java.util.List;

import javax.persistence.Query;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class PluggableTaskDAS extends AbstractDAS<PluggableTaskDTO> {

    // QUERIES
    private static final String findAllByEntitySQL =
        "SELECT b " +
        "  FROM PluggableTaskDTO b " + 
        " WHERE b.entityId = :entity";
    
    private static final String findByEntityTypeSQL =
        "SELECT b " +
        "  FROM PluggableTaskDTO b " + 
        " WHERE b.entityId = :entity " +
        "   AND b.type.id = :type";

    private static final String findByEntityCategorySQL =
        "SELECT b " +
        "  FROM PluggableTaskDTO b " + 
        " WHERE b.entityId = :entity " +
        "   AND b.type.category.id = :category" +
        " ORDER BY b.processingOrder";

    // END OF QUERIES
   
    public PluggableTaskDAS() {
        super();
    }
    
    public PluggableTaskDTO find(Integer id) {
        PluggableTaskDTO entity = em.find(PluggableTaskDTO.class, id);
        //PluggableTaskDTO entity = new PluggableTaskDTO();
        //em.load(entity, id);

        return entity;
    }
    
    public List<PluggableTaskDTO> findAllByEntity(Integer entityId) {
        Query query = em.createQuery(findAllByEntitySQL);
        query.setParameter("entity", entityId);
        return query.getResultList();
        //return query.list();
    }
    
    public PluggableTaskDTO findByEntityType(Integer entityId, Integer typeId) {
        Query query = em.createQuery(findByEntityTypeSQL);
        query.setParameter("entity", entityId);
        query.setParameter("type", typeId);
        return (PluggableTaskDTO) query.getSingleResult();
        //return (PluggableTaskDTO) query.uniqueResult();
    }

    public List<PluggableTaskDTO> findByEntityCategory(Integer entityId, Integer categoryId) {
        Query query = em.createQuery(findByEntityCategorySQL);
        query.setParameter("entity", entityId);
        query.setParameter("category", categoryId);
        return query.getResultList();
        //return query.list();
    }

}
