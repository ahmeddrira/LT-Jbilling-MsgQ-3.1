package jbilling

import java.util.Iterator;
import java.math.BigDecimal;
import com.sapienter.jbilling.server.process.BillingProcessRunTotalDTOEx;
import com.sapienter.jbilling.server.process.db.BillingProcessDTO;
import com.sapienter.jbilling.server.process.db.BillingProcessDAS;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.util.db.CurrencyDAS;

class BillingController {

	static pagination = [ max: 25, offset: 0 ]

	def webServicesSession
	def recentItemService
	def breadcrumbService
	def filterService
	def filters

	def index = {

		List lstBillingProcesses= new ArrayList<BillingProcessDTO>();
		Map dataHashMap = new HashMap();

		filters = filterService.getFilters(FilterType.BILLINGPROCESS, params)
		
		def lst= getProcesses(filters)
		log.info "---------\nRecords found ${lst.totalCount}"
		
		for (BillingProcessDTO dto: lst) {
			log.info "billing_process id: ${dto.getId()}"
			Iterator iter= new BillingProcessDAS().getCountAndSum(dto.getId())
		
			Object[] row = (Object[]) iter.next();
			Object[] record= new Object[3]
			log.info "Row is ${row}"

			record[0]= row[0]
			record[1]= row[1]
			record[2]= new CurrencyDAS().find ((Integer)row[2])
			
			lstBillingProcesses.add( dto )
			dataHashMap.put (dto.getId(), record)
		}

		log.info "Processes ${lstBillingProcesses.size} & Data Size = ${dataHashMap.size()}"
			
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
		if (params.applyFilter) {
			render template: 'list', model: [lstBillingProcesses: lstBillingProcesses, dataHashMap:dataHashMap, filters:filters]
		} else {
			render view: "index", model: [lstBillingProcesses: lstBillingProcesses, dataHashMap:dataHashMap, filters:filters]
		}
			
	}

	def getProcesses(filters) {
		params.max = params?.max?.toInteger() ?: pagination.max
		params.offset = params?.offset?.toInteger() ?: pagination.offset

		return BillingProcessDTO.createCriteria().list(
			max: params.max,
			offset: params.offset
		) {
			and {
				filters.each { filter ->
					if (filter.value) {
						switch (filter.constraintType) {
							case FilterConstraint.DATE_BETWEEN:
								between(filter.field, filter.startDateValue, filter.endDateValue)
								break
						}
					}
				}
				eq('entity', new CompanyDTO(session['company_id']))
				eq('isReview', 0)
			}
			order("id", "desc")
		}
	}


	def show = { render "Billing Process Details screen." }

	def runBilling = {

		render "run billing if not already running"
		//check if billing is already running
		//new BillingProcessDAS().isPresent(entityId, 0, config.getNextRunDate()) != null) {
		//if not, run billing now
		//else show message
	}
}
