package com.sapienter.jbilling.server.list.db;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class ListDAS extends AbstractDAS<ListDTO> {

    public ListDTO findByName(String legacyName) {

        Criteria criteria = getSession().createCriteria(ListDTO.class).add(
                Restrictions.eq("legacyName", legacyName));

        return (ListDTO) criteria.uniqueResult();
    }
}
