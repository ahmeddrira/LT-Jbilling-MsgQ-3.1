package com.sapienter.jbilling.server.list.db;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.server.user.db.CompanyDAS;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class ListEntityDAS extends AbstractDAS<ListEntityDTO> {

    public ListEntityDTO create(ListDTO list, Integer entityId, Integer count) {

        CompanyDTO company = new CompanyDAS().find(entityId);

        ListEntityDTO entity = new ListEntityDTO();
        entity.setEntity(company);
        entity.setList(list);
        entity.setTotalRecords(count);

        return save(entity);
    }

    public ListEntityDTO findByEntity(Integer listId, Integer entityId) {

        Criteria criteria = getSession().createCriteria(ListEntityDTO.class);
        criteria.createAlias("list", "list").add(
                Restrictions.eq("list.id", listId.intValue()));
        criteria.createAlias("entity", "entity").add(
                Restrictions.eq("entity.id", entityId.intValue()));

        return (ListEntityDTO) criteria.uniqueResult();
    }
}
