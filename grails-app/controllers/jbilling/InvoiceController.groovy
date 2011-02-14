package jbilling

import grails.plugins.springsecurity.Secured;
import javax.servlet.ServletOutputStream
import grails.converters.JSON
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.server.util.WebServicesSessionSpringBean;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.common.SessionInternalError;

/**
* BillingController
*
* @author Vikas Bodani
* @since 
*/
@Secured(['isAuthenticated()'])
class InvoiceController {
	
	static pagination = [ max: 25, offset: 0 ]

    def webServicesSession
    def viewUtils
    def filterService
    def recentItemService
	def breadcrumbService

    def index = {
        redirect action: 'list', params: params
    }

	def list = {

		if (params.id) {
			redirect (action: 'showListAndInvoice', params: [id: params.id as Integer])
		}
		
		def filters = filterService.getFilters(FilterType.INVOICE, params)
		def invoiceList= getInvoices(filters)
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
		if (params.applyFilter) {
			render template: 'lists', model: [invoices:invoiceList, filters:filters]
		} else {
			[invoices:invoiceList, filters:filters]
		}
	}

	def getInvoices(filters) {
		params.max = params?.max?.toInteger() ?: pagination.max
		params.offset = params?.offset?.toInteger() ?: pagination.offset
		
		return InvoiceDTO.createCriteria().list(
			max:    params.max,
			offset: params.offset
			) {
				and {
					filters.each { filter ->
						if (filter.value) {
							switch (filter.constraintType) {
								case FilterConstraint.EQ:
									eq(filter.field, filter.value)
									break
	
								case FilterConstraint.DATE_BETWEEN:
									between(filter.field, filter.startDateValue, filter.endDateValue)
									break
									
								case FilterConstraint.LIKE:
									like(filter.field, filter.stringValue)
									break
							}
						}
					}
					eq('deleted', 0)
				}
			}
	}
	
	def showListAndInvoice = { 
		try {
		def invId= params.id as Integer
		
		log.debug "showListAndInvoice(${invId}) called.."
		
		def filters = filterService.getFilters(FilterType.INVOICE, params)
		def invoices= getInvoices(filters)
		
		def invoice= webServicesSession.getInvoiceWS(invId)
		def user= webServicesSession.getUserWS(invoice?.getUserId())
		
		log.debug "Found invoice ${invoice?.number}, Loading..."
		log.debug "Found user ${user}"
		
		def payments= new ArrayList<PaymentWS>(invoice?.payments?.length)
		for(Integer pid: invoice?.payments) {
			PaymentWS payment=webServicesSession.getPayment(pid)
			payments.add(payment)
		}
		def totalRevenue= webServicesSession.getTotalRevenueByUser(invoice?.getUserId())
		
		InvoiceWS temp= invoice;
		String delegatedInvoices = ""
		while (temp?.getDelegatedInvoiceId()) {
			delegatedInvoices += (" > " + temp?.getDelegatedInvoiceId())
			temp= webServicesSession.getInvoiceWS(temp?.getDelegatedInvoiceId())
		}
		if (delegatedInvoices.length() > 0 ) {
			delegatedInvoices= delegatedInvoices.substring(3)
		}
		log.debug "rendering view showListAndInvoice"
		
		recentItemService.addRecentItem(invId, RecentItemType.INVOICE)
		breadcrumbService.addBreadcrumb(controllerName, 'list', null, invId)
		
		render view: 'showListAndInvoice', model:[invoices:invoices, totalRevenue:totalRevenue, user:user, invoice:invoice, delegatedInvoices:delegatedInvoices, payments:payments]
		}catch (Exception e) {
			log.error e.getMessage()
			flash.error = 'error.invoice.details'
			flash.args= [params["id"]]
			redirect(action:'list')
		}
	}

    /**
     * Convenience shortcut, this action shows all invoices for the given user id.
     */
    def user = {
        def filter = new Filter(type: FilterType.ALL, constraintType: FilterConstraint.EQ, field: 'baseUser.id', template: 'id', visible: true, integerValue: params.int('id'))
        filterService.setFilter(FilterType.INVOICE, filter)

		redirect action: 'list'
    }

	def show = {
		log.debug "method invoice.show for id ${params.id} & userId ${params.userId}"
		InvoiceWS invoice;
		UserWS user;
		List<PaymentWS> payments;
		BigDecimal totalRevenue;
		String delegatedInvoices= ""
		
		log.debug "Show Invoice |${params.id}|"
		
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			
			int invId= Integer.parseInt(params["id"])
			log.debug "Template: ${params.template}"

			try {
				invoice= webServicesSession.getInvoiceWS(invId)

				//TODO handle this check appropriately
				//if (!invoice) throw new Exception();
				
				user= webServicesSession.getUserWS(invoice?.getUserId())
				
				log.debug "Found invoice ${invoice?.number}, Loading..."
				log.debug "Found user ${user?.contact?.firstName}"
				
				payments= new ArrayList<PaymentWS>(invoice?.payments?.length)
				for(Integer pid: invoice?.payments) {
					PaymentWS payment=webServicesSession.getPayment(pid)
					payments.add(payment)
				}
				totalRevenue= webServicesSession.getTotalRevenueByUser(invoice?.getUserId())
				
				InvoiceWS temp= invoice;
				while (temp?.getDelegatedInvoiceId()) {
					delegatedInvoices += (" > " + temp?.getDelegatedInvoiceId())
					temp= webServicesSession.getInvoiceWS(temp?.getDelegatedInvoiceId())
				}
				if (delegatedInvoices.length() > 0 )
				{
					delegatedInvoices= delegatedInvoices.substring(3)
				}

				recentItemService.addRecentItem(invId, RecentItemType.INVOICE)
				breadcrumbService.addBreadcrumb(controllerName, 'list', null, invId)
				
			} catch (Exception e) {
				log.error e.getMessage()
				flash.error = 'error.invoice.details'
				flash.args= [params["id"]]
				render template: 'show'
			}
		}		
		
		render template: params.template ?: 'show', model:[totalRevenue:totalRevenue, user:user, invoice:invoice, delegatedInvoices:delegatedInvoices, payments:payments]
	}
	
	def snapshot = {
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			int invId= Integer.parseInt(params["id"])
			InvoiceWS invoice= webServicesSession.getInvoiceWS(invId)
			render template: 'snapshot', model: [invoice:invoice]
		}
	}
	
	def delete = {
		
		log.debug "Delete params= id: ${params.id} , for userId: ${params._userId}"
		int invoiceId =params["id"]?.toInteger()
		int userId= params._userId?.toInteger()
		if (invoiceId) {
			try {
				webServicesSession.deleteInvoice(invoiceId)
				flash.message = 'invoice.delete.success'
				flash.args= [invoiceId]
			}  catch (Exception e) {
				log.debug (e.getMessage())
				flash.error = 'error.invoice.delete'
				flash.args= [params["id"]]
				redirect(action: 'list', params:[id:userId])
			}
		}
		
		redirect (action:list, params:[id:userId])
	}
	
	def notifyInvoiceByEmail = {
		Integer invId= params.id as Integer
		log.debug "invoice.sendInvoiceByEmail ${invId}"
		try {
			webServicesSession.notifyInvoiceByEmail(invId)
			flash.message = 'invoice.prompt.success.email.invoice'
		} catch (Exception e) { 
			log.error e.getMessage()
			flash.error= 'invoice.prompt.failure.email.invoice'
			flash.args= params.id 
		}
		redirect(action: 'showListAndInvoice', params:[id:invId])
	}
	
	def downloadPdf = {
		log.debug 'calling downloadPdf'
		Integer invId= params.id as Integer
		try { 
			byte[] pdfBytes= webServicesSession.getPaperInvoicePDF(invId)
			log.debug "Byte Size ${pdfBytes.length}"
			
			ServletOutputStream servletOutputStream = response.outputStream
			
			response.setContentType("application/pdf")
			response.setContentLength(pdfBytes.length)
			
			response.setHeader("Content-disposition", "inline; filename=Invoice${invId}.pdf")
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			servletOutputStream << pdfBytes

		} catch (Exception e ) {
			log.error e.getMessage()
			flash.error = 'invoice.prompt.failure.downloadPdf'
			redirect(action: 'showListAndInvoice', params:[id:invId])
		}
	}
	
	def removePaymentLink = {
		
		Integer invId= params.id as Integer
		Integer paymentId= params.paymentId as Integer
		log.debug "Parameters[Invoice Id: ${invId}, Payment Id: ${paymentId}"
		
		try {
			webServicesSession.removePaymentLink(invId, paymentId)
			flash.message = "payment.unlink.success"
		} catch (SessionInternalError e){
			viewUtils.resolveException(flash, session.locale, e);
		} catch (Exception e) {
			log.error e.getMessage()
			flash.error = "error.invoice.unlink.payment"
		}
		redirect(action: 'showListAndInvoice', params:[id:invId])
	}
	
}
