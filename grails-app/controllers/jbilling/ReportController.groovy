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

import com.sapienter.jbilling.server.report.db.ReportDTO
import grails.plugins.springsecurity.Secured

import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.report.db.ReportTypeDTO
import com.sapienter.jbilling.server.report.ReportBL

/**
 * ReportController 
 *
 * @author Brian Cowdery
 * @since 07/03/11
 */
@Secured(['isAuthenticated()'])
class ReportController {

    static pagination = [ max: 10, offset: 0 ]

    def viewUtils
    def filterService
    def recentItemService
    def breadcrumbService

    def index = {
        redirect action: list, params: params
    }

    def getReportTypes() {
        return ReportTypeDTO.list()
    }

    def getReports(Integer typeId) {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        return ReportDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            and {
                if (typeId) {
                    type {
                        eq('id', typeId)
                    }
                }
                eq('entity', new CompanyDTO(session['company_id']))
            }
            order('id', 'desc')
        }
    }

    def list = {
        def types = getReportTypes()
        def typeId = params.int ? params.int('id') : types?.first()?.id
        def reports = getReports(typeId)

        breadcrumbService.addBreadcrumb(controllerName, 'list', null, typeId)

        [ types: types, reports: reports, selectedTypeId: typeId ]
    }

    def reports = {
        def typeId = params.int('id')
        def reports = typeId ? getReports(typeId) : null

        render template: 'reports', model: [ reports: reports, selectedTypeId: typeId ]
    }

    def allReports = {
        def reports = getReports(null)
        render template: 'reports', model: [ reports: reports ]
    }

    def show = {
        ReportDTO report = ReportDTO.get(params.int('id'))
        breadcrumbService.addBreadcrumb(controllerName, actionName, null, report?.id)

        render template: 'show', model: [ selected: report ]
    }

    def run = {
        def report = ReportDTO.get(params.int('id'))

        params.parameters.each { name, value ->
            if (value instanceof Map) {
                bindData(report.getParameter(name), value)
            }
        }

        new ReportBL(report, session.locale).renderHtml(response)
    }
}
