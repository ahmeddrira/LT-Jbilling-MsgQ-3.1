/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package jbilling

import com.sapienter.jbilling.server.metafields.MetaFieldBL
import com.sapienter.jbilling.server.metafields.db.EntityType
import com.sapienter.jbilling.server.metafields.db.MetaField

/**
 * @author Alexander Aksenov
 * @since 20.10.11
 */

class MetaFieldsController {

    def breadcrumbService

    def index = {
        redirect(action: 'listCategories')
    }

    def listCategories = {
        List categorylist = Arrays.asList(EntityType.values());
        breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
        [lst: categorylist]
    }

    def list = {

        log.debug "METHOD: list\nId=${params.id} selectedId= ${params.selectedId}"
        EntityType entityType = EntityType.valueOf(params.get('id').toString())
        def lstByCateg = MetaField.findAllByEntityType(entityType);
        log.debug "size of metafields=" + lstByCateg.size() + " of total " + MetaField.list()?.size()

        if (params.template)
            render template: 'list', model: [lstByCategory: lstByCateg, selected: new MetaField()]
        else {
            MetaField selectedField = null;
            if (params.selectedId) {
                selectedField = MetaField.findById(params.int("selectedId"))
            }
            render(view: 'listCategories', model: [selectedCategory: entityType.name(), lst: Arrays.asList(EntityType.values()), lstByCategory: lstByCateg, selected: selectedField])
        }
    }

    def show = {
        def metaField = MetaField.get(params.int('id'))

        breadcrumbService.addBreadcrumb(controllerName, 'list', null, metaField.id, metaField.name)

        render template: 'show', model: [selected: metaField]
    }

    def edit = {
        def metaField = params.id ? MetaField.get(params.int('id')) : new MetaField()

        if (params.id) {
            def crumbName = 'update'
            def crumbDescription = metaField.getName()
            breadcrumbService.addBreadcrumb(controllerName, actionName, crumbName, params.int('id'), crumbDescription)
        }

        [metaField: metaField]
    }

    def save = {
        def metaField = new MetaField()
        EntityType entityType = EntityType.valueOf(params.get('entityType').toString())
        bindData(metaField, params, 'metaField')
        metaField.setEntityType(entityType)

        def defaultValue = null;
        if (params.defaultValue) {
            defaultValue = metaField.createValue()
            bindData(defaultValue, ['value': params.get("defaultValue")])
        };
        metaField.setDefaultValue(defaultValue)


        def metaFieldsService = new MetaFieldBL();

        // save or update
        if (!metaField.id || metaField.id == 0) {
            log.debug("saving new metaField ${metaField}")
            metaField.id = metaFieldsService.create(metaField).id

            flash.message = 'metaField.created'
            flash.args = [metaField.id]

        } else {
            log.debug("updating meta field ${metaField.id}")

            metaFieldsService.update(metaField)

            flash.message = 'metaField.updated'
            flash.args = [metaField.id]
        }

        redirect(action: "list", id: entityType.name(), params: ["selectedId": metaField.id])
    }

    def delete = {
        EntityType type = null;
        if (params.id) {
            type = MetaField.findById(params.int("id")).entityType
            new MetaFieldBL().delete(params.int('id'))
            log.debug("Deleted meta field ${params.id}.")
        }

        flash.message = 'metaField.deleted'
        flash.args = [params.id]

        if (type != null) {
            params.template = true
            params.id = type.name()
            list()
        } else {
            listCategories();
        }
    }
}
