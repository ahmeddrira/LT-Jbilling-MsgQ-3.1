package com.sapienter.jbilling.server.mediation.db;

import java.util.List;

import org.hibernate.Query;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class MediationProcessDAS extends AbstractDAS<MediationProcess> {

    // QUERIES
    private static final String findAllByEntitySQL =
        "SELECT b " +
        "  FROM MediationProcess b " + 
        " WHERE b.configuration.entityId = :entity " +
        " ORDER BY id DESC";

    public List<MediationProcess> findAllByEntity(Integer entityId) {
        Query query = getSession().createQuery(findAllByEntitySQL);
        query.setParameter("entity", entityId);
        //return query.getResultList();
        return query.list();
    }

}
