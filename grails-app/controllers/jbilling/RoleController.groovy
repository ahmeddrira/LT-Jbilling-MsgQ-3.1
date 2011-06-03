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

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import com.sapienter.jbilling.server.user.permisson.db.RoleDTO
import com.sapienter.jbilling.server.user.permisson.db.PermissionTypeDTO
import com.sapienter.jbilling.server.user.RoleBL
import com.sapienter.jbilling.server.user.permisson.db.PermissionDTO

/**
 * RoleController 
 *
 * @author Brian Cowdery
 * @since 02/06/11
 */
class RoleController {

    def breadcrumbService

    def index = {
        redirect action: list, params: params
    }

    def getList(GrailsParameterMap params) {
        return RoleDTO.createCriteria().list() {
            order('id', 'asc')
        }
    }

    def list = {
        def roles = getList(params)
        def selected = params.id ? RoleDTO.get(params.int('id')) : null

        breadcrumbService.addBreadcrumb(controllerName, 'list', null, selected?.id, selected?.getTitle(session['language_id']))

        if (params.applyFilter) {
            render template: 'roles', model: [ roles: roles, selected: selected ]
        } else {
            render view: 'list', model: [ roles: roles, selected: selected ]
        }
    }

    def show = {
        def role = RoleDTO.get(params.int('id'))

        breadcrumbService.addBreadcrumb(controllerName, 'list', null, role.id, role.getTitle(session['language_id']))

        render template: 'show', model: [ selected: role ]
    }

    def edit = {
        def role = params.id ? RoleDTO.get(params.int('id')) : new RoleDTO()
        def permissionTypes = PermissionTypeDTO.list(order: 'asc')

        def crumbName = params.id ? 'update' : 'create'
        def crumbDescription = params.id ? role.getTitle(session['language_id']) : null
        breadcrumbService.addBreadcrumb(controllerName, actionName, crumbName, params.int('id'), crumbDescription)

        [ role: role, permissionTypes: permissionTypes ]
    }

    def save = {
        def role = new RoleDTO();
        bindData(role, params, 'role')

        List<PermissionDTO> allPermissions = PermissionDTO.list()
        params.permission.each { id, granted ->
            if (granted) {
                role.permissions.add(allPermissions.find{ it.id == id as Integer })
            }
        }

        def roleService = new RoleBL();

        // save or update
        if (!role.id || role.id == 0) {
            log.debug("saving new role ${role}")
            roleService.create(role)

        } else {
            log.debug("updating role ${role.id}")

            roleService.set(role.id)
            roleService.update(role)
        }

        // set/update international descriptions
        roleService.setTitle(session['language_id'], params.role.title)
        roleService.setDescription(session['language_id'], params.role.description)

        chain action: 'list', params: [ id: role.id ]
    }
}
