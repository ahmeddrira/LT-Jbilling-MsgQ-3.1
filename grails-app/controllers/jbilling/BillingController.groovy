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

	def index = {
		redirect action: list, params: params
	}
	
	
	/*
	 * Renders/display list of Billing Processes Ordered by Process Id descending
	 * so that the lastest process shows first.
	 */
	def list = {

		Map dataHashMap = new HashMap()
		Iterator iter= null

		def filters = filterService.getFilters(FilterType.BILLINGPROCESS, params)
		
		def filteredList= filterProcesses(filters)

		//def filteredList= BillingProcessDTO.findAllByEntityAndIsReview(new CompanyDTO(session['company_id']), 0)

		for (BillingProcessDTO dto: filteredList) { 
			Integer _processId= dto.getId()?.toInteger()
			log.debug "billing_process id: ${_processId}"
			iter= new BillingProcessDAS().getCountAndSum(_processId)
			Object[] row = (Object[]) iter.next();
			row[2]= new CurrencyDAS().find ((Integer)row[2])
			dataHashMap.put (_processId, row)
		}
		
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
		if (params.applyFilter) {
			render template: 'list', model: [lstBillingProcesses: filteredList, dataHashMap:dataHashMap, filters:filters]
		} else {
			render view: "index", model: [lstBillingProcesses: filteredList, dataHashMap:dataHashMap, filters:filters]
		}
	}

	/*
	 * Filter the process results based on the parameter filter values
	 */
	def filterProcesses(filters) {
		
		params.max = (params?.max?.toInteger()) ?: pagination.max
		params.offset = (params?.offset?.toInteger()) ?: pagination.offset
		
		return BillingProcessDTO.createCriteria().list(
			max:    params.max,
			offset: params.offset
			) {
				and {
					filters.each { filter ->
						if (filter.value) {
							switch (filter.constraintType) {
								case FilterConstraint.DATE_BETWEEN:
									between(filter.field, filter.startDateValue, filter.endDateValue)
									break
								case FilterConstraint.EQ:
									eq(filter.field, filter.value)
									break
							}
						}
					}
					eq('isReview', 0)
					eq('entity', new CompanyDTO(session['company_id']))
				}
				order("id", "desc")
			}
	}

	/*
	 * To display the run details of a given Process Id
	 */
	def show = { 
		log.debug "BillingController.show - ID: ${params.id}"
		
		Integer processId= params.id.toInteger()
		BillingProcessDTO process = new BillingProcessDAS().find(processId);
		
		log.debug "process.orderProcesses: ${process.orderProcesses?.size()}"
		
		def das= new BillingProcessDAS() 
		
		def countAndSumByCurrency= new ArrayList()
		Iterator iter= das.getCountAndSum(processId)
		log.debug "*******Records found - getCountAndSum${processId}*******"
		while (iter.hasNext()) {
			Object[] row = (Object[]) iter.next();
			Integer currId= row[2] as Integer
			row[2]= new CurrencyDAS().find (currId)
			countAndSumByCurrency.add (row)
			log.debug "1. ${row[0]}" //count
			log.debug "2. ${row[1]}" //sum
			log.debug "3. ${row[2]}" 
			log.debug "---------------------------------"
		}
		
		def mapOfPaymentListByCurrency= new HashMap()
		iter= das.getSuccessfulProcessCurrencyMethodAndSum (processId)
		log.debug "*******Records found - getSuccessfulProcessCurrencyMethodAndSum ${processId}*******"
		def tempList
		while (iter.hasNext()) {
			Object[] row = (Object[]) iter.next();
			tempList= mapOfPaymentListByCurrency.get( row[0] as Integer)
			if ( null == tempList) {
				tempList= new ArrayList()
				mapOfPaymentListByCurrency.put (row[0] as Integer, tempList)
			}
			tempList.add ( [new PaymentMethodDTO(row[1] as int), new BigDecimal(row[2])] as Object[])
			
			log.debug "1. ${new CurrencyDAS().find (row[0] as int).getCode()}"
			log.debug "2. ${new PaymentMethodDTO(row[1] as int).getDescription(session.language_id)}"
			log.debug "3. ${row[2]}"
			log.debug "---------------------------------"
		}
		
		def failedAmountsByCurrency= new ArrayList()
		iter= das.getFailedProcessCurrencyAndSum (processId)
		log.debug "*******Records found - getFailedProcessCurrencyAndSum ${processId}*******"
		while (iter.hasNext()) {
			Object[] row = (Object[]) iter.next();
			failedAmountsByCurrency.add (row[1])
			log.debug "1. ${new CurrencyDAS().find (row[0] as int).getCode()}"
			log.debug "2. ${row[1]}"
			log.debug "---------------------------------"
		}
		recentItemService.addRecentItem(processId, RecentItemType.BILLINGPROCESS)
		[process:process, countAndSumByCurrency: countAndSumByCurrency, mapOfPaymentListByCurrency: mapOfPaymentListByCurrency, failedAmountsByCurrency: failedAmountsByCurrency] 
	}

	/*
	 * Applies a filter on billing_process_id on the InvoiceDTO 
	 */
	def showInvoices = {
		Integer _processId= params.id as Integer
		log.debug "showInvoices, params.id=${_processId}"
		def filter =  new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.EQ, field: 'billingProcess.id', template: 'id', visible: false, integerValue: _processId)
		filterService.setFilter(FilterType.INVOICE, filter)
		
		redirect controller: 'invoice', action: 'list'
	}
	
	def showOrders = { 
		Integer _processId= params.id as Integer
		log.debug "showInvoices, params.id=${_processId}"
		BillingProcessDTO dto= new BillingProcessDAS().find(_processId)
		//redirect to orders with list of order ids
	}
}
