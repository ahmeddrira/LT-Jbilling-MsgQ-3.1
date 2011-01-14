package jbilling

import grails.plugins.springsecurity.Secured;
import java.util.Iterator;
import java.math.BigDecimal;
import com.sapienter.jbilling.server.process.BillingProcessRunTotalDTOEx;
import com.sapienter.jbilling.server.process.db.BillingProcessDTO;
import com.sapienter.jbilling.server.process.db.BillingProcessDAS;
import com.sapienter.jbilling.server.process.db.ProcessRunDTO;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.util.db.CurrencyDAS;
import com.sapienter.jbilling.server.payment.db.PaymentMethodDTO;

/**
* BillingController
*
* @author Vikas Bodani
* @since 07/01/11
*/
@Secured(['isAuthenticated()'])
class BillingController {

	static pagination = [ max: 25, offset: 0 ]

	def webServicesSession
	def recentItemService
	def breadcrumbService
	def filterService
	def filters

	/**
	 * Renders/display list of Billing Processes Ordered by Process Id descending
	 * so that the lastest process shows first.
	 */
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

	/**
	 * Filter the process results based on the parameter filter values
	 */
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

	/**
	 * To display the run details of a given Process Id
	 */
	def show = { 
		log.info "BillingController.show - ID: ${params.id}"
		Integer processId= params.id.toInteger()
		BillingProcessDTO process = new BillingProcessDAS().find(processId);
		def das= new BillingProcessDAS() 
		
		def countAndSumByCurrency= new ArrayList()
		Iterator iter= das.getCountAndSum(processId)
		log.info "*******Records found - getCountAndSum${processId}*******"
		while (iter.hasNext()) {
			Object[] row = (Object[]) iter.next();
			Integer currId= row[2] as Integer
			row[2]= new CurrencyDAS().find (currId)
			countAndSumByCurrency.add (row)
			log.info "1. ${row[0]}" //count
			log.info "2. ${row[1]}" //sum
			log.info "3. ${row[2]}" 
			log.info "---------------------------------"
		}
		
		def mapOfPaymentListByCurrency= new HashMap()
		iter= das.getSuccessfulProcessCurrencyMethodAndSum (processId)
		log.info "*******Records found - getSuccessfulProcessCurrencyMethodAndSum ${processId}*******"
		def tempList
		while (iter.hasNext()) {
			Object[] row = (Object[]) iter.next();
			tempList= mapOfPaymentListByCurrency.get( row[0] as Integer)
			if ( null == tempList) {
				tempList= new ArrayList()
				mapOfPaymentListByCurrency.put (row[0] as Integer, tempList)
			}
			tempList.add ( [new PaymentMethodDTO(row[1] as int), new BigDecimal(row[2])] as Object[])
			
			log.info "1. ${new CurrencyDAS().find (row[0] as int).getCode()}"
			log.info "2. ${new PaymentMethodDTO(row[1] as int).getDescription(session.language_id)}"
			log.info "3. ${row[2]}"
			log.info "---------------------------------"
		}
		
		def failedAmountsByCurrency= new ArrayList()
		iter= das.getFailedProcessCurrencyAndSum (processId)
		log.info "*******Records found - getFailedProcessCurrencyAndSum ${processId}*******"
		while (iter.hasNext()) {
			Object[] row = (Object[]) iter.next();
			failedAmountsByCurrency.add (row[1])
			log.info "1. ${new CurrencyDAS().find (row[0] as int).getCode()}"
			log.info "2. ${row[1]}"
			log.info "---------------------------------"
		}
		
		[process:process, countAndSumByCurrency: countAndSumByCurrency, mapOfPaymentListByCurrency: mapOfPaymentListByCurrency, failedAmountsByCurrency: failedAmountsByCurrency] 
	}

	def runBilling = {

		//check if billing is already running
		//new BillingProcessDAS().isPresent(entityId, 0, config.getNextRunDate()) != null) {
		//if not, run billing now
		//else show message
		render "run billing if not already running"
	}
}
