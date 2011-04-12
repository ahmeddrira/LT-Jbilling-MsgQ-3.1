package jbilling

import grails.plugins.springsecurity.Secured
import com.sapienter.jbilling.server.mediation.db.MediationProcess
import com.sapienter.jbilling.server.mediation.MediationRecordWS
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.server.mediation.db.MediationRecordDTO
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO
import com.sapienter.jbilling.server.mediation.db.MediationRecordStatusDTO
import com.sapienter.jbilling.server.order.db.OrderDTO
import org.hibernate.criterion.Restrictions
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.hibernate.FetchMode
import org.hibernate.Criteria

/**
* MediationController
*
* @author Vikas Bodani
* @since 17/02/2011
*/
@Secured(['isAuthenticated()'])
class MediationController {

    static pagination = [ max: 25, offset: 0 ]

	def webServicesSession
	def recentItemService
	def breadcrumbService
	def filterService

	def index = {
		redirect action: list, params: params
	}
	
	def list = {
		def filters = filterService.getFilters(FilterType.MEDIATIONPROCESS, params)
		def processes = getFilteredProcesses(filters, params)

		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)

		if (params.applyFilter) {
			render template: 'list', model: [processes: processes,filters:filters]
		} else {
			render view: "index", model: [processes: processes, filters:filters]
		}
    }
	
	def getFilteredProcesses (filters, GrailsParameterMap params) {
		params.max = (params?.max?.toInteger()) ?: pagination.max
		params.offset = (params?.offset?.toInteger()) ?: pagination.offset

		return MediationProcess.createCriteria().list(
			max:    params.max,
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

			order("id", "desc")
		}
	}

	def show = {
        def process = MediationProcess.get(params.int('id'))

        log.debug("Got process ${process}")

		recentItemService.addRecentItem(process.id, RecentItemType.MEDIATIONPROCESS)
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, process.id)

		if (params.template) {
			render template: params.template, model: [ selected: process ]

		} else {
			def filters = filterService.getFilters(FilterType.MEDIATIONPROCESS, params)
			def processes = getFilteredProcesses(filters, params)

			render view: 'showListAndView', model: [ selected: process, processes: processes, filters: filters ]
		}
	}

    def invoice = {
        def invoice = InvoiceDTO.get(params.int('id'))

        def records = MediationRecordDTO.createCriteria().listDistinct {
            lines {
                orderLine {
                    purchaseOrder {
                        orderProcesses {
                            eq("invoice.id", invoice.id)
                        }
                    }
                }
            }

            recordStatus {
                eq("id", Constants.MEDIATION_RECORD_STATUS_DONE_AND_BILLABLE)
            }
        }

        render view: 'events', model: [ invoice: invoice, records: records ]
    }

    def order = {
        def order = OrderDTO.get(params.int('id'))

        def records = MediationRecordDTO.createCriteria().listDistinct {
            lines {
                orderLine {
                    eq("purchaseOrder.id", order.id)
                }
            }

            recordStatus {
                eq("id", Constants.MEDIATION_RECORD_STATUS_DONE_AND_BILLABLE)
            }
        }

        render view: 'events', model: [ order: order, records: records ]
    }
	
}
