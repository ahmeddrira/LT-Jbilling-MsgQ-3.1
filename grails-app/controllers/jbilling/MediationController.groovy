package jbilling

import grails.plugins.springsecurity.Secured
import com.sapienter.jbilling.server.mediation.db.MediationProcess
import com.sapienter.jbilling.server.mediation.MediationRecordWS
import com.sapienter.jbilling.server.util.Constants


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
		
		params.max = (params?.max?.toInteger()) ?: pagination.max
		params.offset = (params?.offset?.toInteger()) ?: pagination.offset
		
		def processes= getFilteredProcesses(filters)

		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)

		log.debug "Found ${processes?.totalCount} Mediation Processs"
		
		if (params.applyFilter) {
			render template: 'list', model: [processes: processes,filters:filters]
		} else {
			render view: "index", model: [processes: processes, filters:filters]
		}
    }
	
	def getFilteredProcesses (filters) {
		return MediationProcess.createCriteria().list(
			max:    params.max,
			offset: params.offset
		) {
			and {
				filters.each { filter ->
					if (filter.value) {
						addToCriteria(filter.getRestrictions());
					}
				}
				
			}
			order("id", "desc")
		}
	}

	def show = {
		int processId= params.int('id')
		
		List<MediationRecordWS> records= webServicesSession.getMediationRecordsByMediationProcess(processId)
		log.debug "Records found ${records.size()}"
		Map<Integer, Integer> map= new HashMap<Integer, Integer>();
		for(MediationRecordWS ws: records) {
			log.debug "Record ${ws.key} Status ID: ${ws.getRecordStatusId()}"
			if (map[ws.getRecordStatusId()]) {
				log.debug "found in map"
				map.put(ws.getRecordStatusId(), new Integer(map[ws.getRecordStatusId()] + 1 ) )
			} else {
				log.debug "not found in map. putting now.."
				map.put(ws.getRecordStatusId(), new Integer(1))
			}
		}
		recentItemService.addRecentItem(processId, RecentItemType.MEDIATIONPROCESS)
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, processId)
		if (params.template) {
			render template: params.template, model: [map:map, processId: processId]
		} else {
			params.max = (params?.max?.toInteger()) ?: pagination.max
			params.offset = (params?.offset?.toInteger()) ?: pagination.offset
			def filters= filterService.getFilters(FilterType.MEDIATIONPROCESS, params)
			def processes= getFilteredProcesses(filters)
			render view: 'showListAndView', model: [map:map, processId: processId, processes:processes, filters:filters ]
		}
	}
	
}
