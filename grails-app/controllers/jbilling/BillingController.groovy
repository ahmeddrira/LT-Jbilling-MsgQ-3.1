
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

import grails.plugins.springsecurity.Secured;
import java.util.Iterator;
import java.math.BigDecimal;

import com.sapienter.jbilling.server.process.db.BillingProcessDTO;
import com.sapienter.jbilling.server.process.db.BillingProcessDAS;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.payment.db.PaymentMethodDTO;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.invoice.db.InvoiceDAS
import com.sapienter.jbilling.client.util.SortableCriteria
import com.sapienter.jbilling.server.util.db.CurrencyDTO;

/**
* BillingController
*
* @author Vikas Bodani
* @since 07/01/11
*/
@Secured(["MENU_94"])
class BillingController {

	static pagination = [ max: 10, offset: 0, sort: 'id', order: 'desc' ]

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

		def filters = filterService.getFilters(FilterType.BILLINGPROCESS, params)
		def filteredList= getProcesses(filters)

		Map dataHashMap= new HashMap()
        Iterator cntAndSumIter= null
        def dataArr= null;
        def processDas= new BillingProcessDAS()
        for (BillingProcessDTO dto : filteredList) {
            dataHashMap.put(Integer.valueOf(dto.getId()), [])
            for (Object[] row: new BillingProcessDAS().getCountAndSum(dto.id)) {
                dataArr= new Object[3]
                dataArr[0]= row[0]
                dataArr[1]= row[1]
                dataArr[2]= CurrencyDTO.get(row[2] as Integer)
                dataHashMap.get(Integer.valueOf(dto.getId())) << dataArr
            }
        }

        breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
        if (params.applyFilter || params.partial) {
            render template: 'list', model: [lstBillingProcesses: filteredList, dataHashMap:dataHashMap, filters:filters]
            return
        }
		render view: "index", model: [lstBillingProcesses: filteredList, dataHashMap:dataHashMap, filters:filters]
	
	}

	/*
	 * Filter the process results based on the parameter filter values
	 */
	def getProcesses(filters) {
        
		params.max = (params?.max?.toInteger()) ?: pagination.max
		params.offset = (params?.offset?.toInteger()) ?: pagination.offset
        params.sort = params?.sort ?: pagination.sort
        params.order = params?.order ?: pagination.order
		
		return BillingProcessDTO.createCriteria().list(
			max:    params.max,
			offset: params.offset
			) {
				and {
					filters.each { filter ->
						if (filter.value) {
							addToCriteria(filter.getRestrictions());
						}
					}
					//eq('isReview', 0)
					eq('entity', new CompanyDTO(session['company_id']))
				}

                // apply sorting
                SortableCriteria.sort(params, delegate)
			}
	}

	/*
	 * To display the run details of a given Process Id
	 */
	def show = {
		Integer processId = params.id.toInteger()
        
		BillingProcessDTO process = new BillingProcessDAS().find(processId);

		def genInvoices = new InvoiceDAS().findByProcess(process)
		def invoicesGenerated = genInvoices?.size() ?: 0

		def countAndSumByCurrency= new HashMap()
        List listByCurrency= new BillingProcessDAS().getCountAndSum(processId) 
        for (Iterator iterator = listByCurrency.iterator(); iterator.hasNext();) {
            Object[] row= (Object[]) iterator.next();
			countAndSumByCurrency.put(CurrencyDTO.get(row[2] as Integer),row[1]) 
		}
		log.debug("Fetching count and sum by currency. $countAndSumByCurrency")

		def mapOfPaymentListByCurrency= new HashMap()
        for ( Object[] row: new BillingProcessDAS().getSuccessfulProcessCurrencyMethodAndSum(processId) ) {
			def payments = mapOfPaymentListByCurrency.get(row[0] as Integer) ?: new ArrayList()
            payments.add([new PaymentMethodDTO(row[1]), new BigDecimal(row[2])] as Object[])
            mapOfPaymentListByCurrency.put(row[0], payments)
		}
		log.debug("Fetching payment list by currency. $mapOfPaymentListByCurrency")

		def failedAmountsByCurrency= new HashMap()
        for (Object[] row: new BillingProcessDAS().getFailedProcessCurrencyAndSum(processId)) {
            def AMT = failedAmountsByCurrency.get(String.valueOf(row[0])) ?: BigDecimal.ZERO
            AMT.add(new BigDecimal(row[1]))
            //failedAmountsByCurrency.put(String.valueOf(row[0]), AMT)
		}
		log.debug("Fetching failed amounts by currency. $failedAmountsByCurrency")

		recentItemService.addRecentItem(processId, RecentItemType.BILLINGPROCESS)
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, processId)
		[process:process, invoicesGenerated:invoicesGenerated, countAndSumByCurrency: countAndSumByCurrency, mapOfPaymentListByCurrency: mapOfPaymentListByCurrency, failedAmountsByCurrency: failedAmountsByCurrency, reviewConfiguration: webServicesSession.getBillingProcessConfiguration()] 
	}

	def showInvoices = {
		def _processId= params.int('id')
		log.debug "redirecting to invoice controller for process id=${_processId}"
		//def filter =  new Filter(type: FilterType.INVOICE, constraintType: FilterConstraint.EQ, field: 'billingProcess.id', template: 'id', visible: false, integerValue: _processId)
		//filterService.setFilter(FilterType.INVOICE, filter)
		redirect controller: 'invoice', action: 'byProcess', id:_processId
	}
	
	def showOrders = {
		def _processId= params.int('id')
		log.debug "redirect to order controller for processId $_processId"
		redirect controller: 'order', action: 'byProcess', id:_processId
	}

    @Secured(["BILLING_80"])
	def approve = {
		try {
			webServicesSession.setReviewApproval(Boolean.TRUE)
		} catch (Exception e) {
			throw new SessionInternalError(e)
		}
		flash.message = 'billing.review.approve.success'
		redirect action: 'list'
	}

    @Secured(["BILLING_80"])
	def disapprove = {
		try {
			webServicesSession.setReviewApproval(Boolean.FALSE)
		} catch (Exception e) {
			throw new SessionInternalError(e)
		}
		flash.message = 'billing.review.disapprove.success'
		redirect action: 'list'
	}
}
