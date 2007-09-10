/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
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
