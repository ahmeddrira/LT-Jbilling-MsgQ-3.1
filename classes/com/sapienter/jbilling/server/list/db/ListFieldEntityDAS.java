package com.sapienter.jbilling.server.list.db;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class ListFieldEntityDAS extends AbstractDAS<ListFieldEntityDTO> {

    public ListFieldEntityDTO findByFieldEntity(Integer fieldId,
            Integer listEntityId) {

        Criteria criteria = getSession().createCriteria(
                ListFieldEntityDTO.class);
        criteria.createAlias("listField", "list").add(
                Restrictions.eq("list.id", fieldId.intValue()));
        criteria.createAlias("listEntity", "entity").add(
                Restrictions.eq("entity.id", listEntityId.intValue()));

        return (ListFieldEntityDTO) criteria.uniqueResult();
    }

    public ListFieldEntityDTO create(Integer entity, Integer field) {
        ListEntityDTO listEntity = new ListEntityDAS().find(entity);
        ListFieldDTO listField = new ListFieldDAS().find(field);

        ListFieldEntityDTO newListField = new ListFieldEntityDTO();
        newListField.setListEntity(listEntity);
        newListField.setListField(listField);

        return save(newListField);

    }

    public ListFieldEntityDTO create(ListEntityDTO entity, ListFieldDTO field) {

        ListFieldEntityDTO newListField = new ListFieldEntityDTO();
        newListField.setListEntity(entity);
        newListField.setListField(field);

        return save(newListField);
    }
}
