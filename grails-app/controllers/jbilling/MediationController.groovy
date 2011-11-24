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

import com.sapienter.jbilling.client.util.SortableCriteria
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO
import com.sapienter.jbilling.server.mediation.db.MediationProcess
import com.sapienter.jbilling.server.mediation.db.MediationRecordDTO
import com.sapienter.jbilling.server.order.db.OrderDTO
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

/**
 * MediationController
 *
 * @author Vikas Bodani
 * @since 17/02/2011
 */
@Secured(["MENU_95"])
class MediationController {

    static pagination = [max: 1, offset: 0, sort: 'id', order: 'desc']

    def webServicesSession
    def recentItemService
    def breadcrumbService
    def filterService
    def mediationSession

    def index = {
        redirect action: list, params: params
    }

    def list = {
        def filters = filterService.getFilters(FilterType.MEDIATIONPROCESS, params)
        List<Integer> processValues = []
        List<MediationProcess> processes = []
        (processes, processValues) = getFilteredProcesses(filters, params)

        breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)

        if (params.applyFilter || params.partial) {
            render template: 'processes', model: [processes: processes, filters: filters, processValues: processValues]
        } else {
            render view: "list", model: [processes: processes, filters: filters, processValues: processValues]
        }
    }

    def getFilteredProcesses(filters, GrailsParameterMap params) {
        params.max = (params?.max?.toInteger()) ?: pagination.max
        params.offset = (params?.offset?.toInteger()) ?: pagination.offset
        params.sort = params?.sort ?: pagination.sort
        params.order = params?.order ?: pagination.order

//		def processes = new HashMap<MediationProcess, Integer>()
        //
        //        MediationProcess.createCriteria().list(
        //			max:    params.max,
        //			offset: params.offset
        //		) {
        //			and {
        //				filters.each { filter ->
        //					if (filter.value != null) {
        //						addToCriteria(filter.getRestrictions());
        //					}
        //				}
        //			}
        //
        //            configuration {
        //                eq("entityId", session['company_id'])
        //            }
        //
        //            // apply sorting
        //            SortableCriteria.sort(params, delegate)
        //
        //        }.each { process ->
        //            processes.put(process, getRecordCount(process))
        //        }

        List<MediationProcess> processes = []
        List<Integer> processValues = []

        processes = MediationProcess.createCriteria().list(
                max: params.max,
                offset: params.offset
        ) {
            and {
                filters.each { filter ->
                    if (filter.value != null) {
                        addToCriteria(filter.getRestrictions());
                    }
                }
            }

            configuration {
                eq("entityId", session['company_id'])
            }

            // apply sorting
            SortableCriteria.sort(params, delegate)

        }

        processes.eachWithIndex { process, idx ->
            processValues[idx] = getRecordCount(process)
        }

        return [processes, processValues]
    }

    def Integer getRecordCount(MediationProcess process) {
        return MediationRecordDTO.createCriteria().get() {
            eq('process.id', process.id)

            projections {
                rowCount()
            }
        }
    }

    def show = {
        def process = MediationProcess.get(params.int('id'))
        def recordCount = getRecordCount(process)

        recentItemService.addRecentItem(process.id, RecentItemType.MEDIATIONPROCESS)
        breadcrumbService.addBreadcrumb(controllerName, actionName, null, process.id)

        if (params.template) {
            render template: params.template, model: [selected: process, recordCount: recordCount]

        } else {
            def filters = filterService.getFilters(FilterType.MEDIATIONPROCESS, params)
            def processes = getFilteredProcesses(filters, params)

            render view: 'list', model: [selected: process, recordCount: recordCount, processes: processes, filters: filters]
        }
    }

    def invoice = {
        def invoiceId = params.int('id')
        def invoice = InvoiceDTO.get(invoiceId)
        def records = mediationSession.getMediationRecordLinesForInvoice(invoiceId)

        render view: 'events', model: [invoice: invoice, records: records]
    }

    def order = {

        def orderId = params.int('id')
        def order, records

        try {
            order = OrderDTO.get(orderId)
            records = mediationSession.getMediationRecordLinesForOrder(orderId)
        } catch (Exception e) {
            flash.info = message(code: 'error.mediation.events.none')
            flash.args = [params.id]
        }
        render view: 'events', model: [ order: order, records: records ]
    }

}
