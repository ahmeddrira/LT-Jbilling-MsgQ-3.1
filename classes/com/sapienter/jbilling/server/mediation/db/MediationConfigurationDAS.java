package com.sapienter.jbilling.server.mediation.db;

import java.util.List;

import javax.persistence.Query;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class MediationConfigurationDAS extends AbstractDAS<MediationConfiguration> {

    // QUERIES
    private static final String findAllByEntitySQL =
        "SELECT b " +
        "  FROM MediationConfiguration b " + 
        " WHERE b.entityId = :entity " +
        " ORDER BY orderValue";

    @Override
    public MediationConfiguration find(Integer id) {
        return em.find(MediationConfiguration.class, id);
    }

    public List<MediationConfiguration> findAllByEntity(Integer entityId) {
        Query query = em.createQuery(findAllByEntitySQL);
        query.setParameter("entity", entityId);
        return query.getResultList();
        //return query.list();
    }

}
