package jbilling

import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.server.util.WebServicesSessionSpringBean;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO;
import com.sapienter.jbilling.server.user.UserWS;

class InvoiceController {
	
	Integer languageId= session["language_id"]
	
	static pagination = [ max: 25, offset: 0 ]

    def webServicesSession
    def viewUtils
    def filterService
    def recentItemService

    def index = {
        redirect action: list, params: params
    }

	def list = {

		def invoices;
		int userId;
		log.info "Invoice 'list' method, userid=[${params.id}], "
		try {
			def filters = filterService.getFilters(FilterType.INVOICE, params)
			if ( params["id"] && params["id"].matches("^[0-9]+") ) {
				userId= Integer.parseInt(params["id"])
				invoices= getInvoices(userId, filters)
			} else {
				invoices= getInvoices(filters)
			}
		} catch (Exception e) {
			flash.message = 'invoices.empty'
			flash.args= [params["id"]]
		}

		log.info "found invoices [${invoices?.size()}]"
		[invoices:invoices, userId:userId]
	}
	
	def getInvoices(Integer userId, filters) {
		params.max = params?.max?.toInteger() ?: pagination.max
		params.offset = params?.offset?.toInteger() ?: pagination.offset
		
		return InvoiceDTO.createCriteria().list(
			max:    params.max,
			offset: params.offset
			) {
				and {
					baseUser {
						eq('id', userId)
					}
					eq('deleted', 0)
				}
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
					eq('deleted', 0)
				}
			}
	}
	
	def show = {
		log.info "method invoice.show for id ${params.id} & userId ${params.userId}"
		InvoiceWS invoice;
		UserWS user;
		List<PaymentWS> payments;
		BigDecimal totalRevenue;
		String delegatedInvoices= ""
		
		log.info "Show Invoice |${params.id}|"
		//log.info "Show Invoice |${params.selectedInvId}|"
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			
			int id= Integer.parseInt(params["id"])
			
			recentItemService.addRecentItem(id, RecentItemType.INVOICE)
			
			try {
				invoice= webServicesSession.getInvoiceWS(id)
				log.info "Found invoice ${invoice?.number}, for user: ${params.userId}. Loading..."
				if (params.userId && params.userId.matches("^[0-9]+")) {
					user= webServicesSession.getUserWS(params.userId?.toInteger())
				} else {
					user= webServicesSession.getUserWS(invoice?.getUserId())
				}
				log.info "Found user ${user}"
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
					delegatedInvoices= delegatedInvoices.substring(3)
			} catch (Exception e) {
				e.printStackTrace()
				log.error e.getMessage()
				//TODO add messages to properites and set here
				flash.message = 'error.invoice.details'
				flash.args= [params["id"]]
				redirect(action:'list')
			}
		}		
		
		render template: "show", model:[_userId:user?.getUserId(), totalRevenue:totalRevenue,languageId:languageId,user:user, invoice:invoice, delegatedInvoices:delegatedInvoices, payments:payments]
	}
	
	def delete = {
		
		log.info "Delete params= id: ${params.id} , for userId: ${params._userId}"
		int invoiceId =params["id"]?.toInteger()
		int userId= params._userId?.toInteger()
		if (invoiceId) {
			try {
				webServicesSession.deleteInvoice(invoiceId)
			}  catch (Exception e) {
				log.info (e.getMessage() + "\n${e.getClass().getName()}")
				flash.error = 'error.invoice.delete'
				flash.args= [params["id"]]
				redirect(action: 'list', params:[id:userId])
			}
		}
		
		redirect (action:list, params:[id:userId])
	}
}
