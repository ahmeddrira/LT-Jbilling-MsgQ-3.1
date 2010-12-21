package jbilling

import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.server.util.WebServicesSessionSpringBean;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.user.UserWS;

class InvoiceController {
	
	def IWebServicesSessionBean webServicesSession
	Integer languageId= session["language_id"]
	
	def index = { redirect(action:'lists')}
	
	def delete = {
		int invoiceId =params["id"]?.toInteger()
		int userId= params._userId?.toInteger()
		if (invoiceId) {
			try {
				webServicesSession.deleteInvoice(invoiceId)
			}  catch (Exception e) {
				flash.message = 'error.invoice.details'
				flash.args= [params["id"]]
				redirect(action:'lists')
			}
		}
		log.info "redirecting to view lists for userId ${userId}"
		redirect (action:lists, id:userId)
	}
	
	def lists = {
		InvoiceWS[] invoices= null;
		int userId;
		log.info "Get invoices list for user id [${params?.id}]"
		try {
			if (params["id"] && params["id"].matches("^[0-9]+")) {
				userId= Integer.parseInt(params["id"])
				invoices= webServicesSession.getAllInvoicesForUser(userId)
			} else {
				invoices= webServicesSession.getAllInvoices()
			}
		} catch (Exception e) {
			flash.message = 'invoices.empty'
			flash.args= [params["id"]]
		}
		
		log.info "found invoices [${invoices?.size()}]"
		
		[invoices:invoices, userId:userId]
	}
	
	def show = {
		InvoiceWS invoice;
		UserWS user;
		List<PaymentWS> payments;
		BigDecimal totalRevenue;
		String delegatedInvoices= ""
		
		log.info "Show Invoice |${params.id}|"
		//log.info "Show Invoice |${params.selectedInvId}|"
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			int id= Integer.parseInt(params["id"])
			log.info "Integer Invoice Id=${id}"
			try {
				invoice= webServicesSession.getInvoiceWS(id)
				log.info "Found invoice ${invoice}. Loading..."
				user= webServicesSession.getUserWS(invoice?.getUserId())
				log.info "Found user ${user.contact?.firstName} ${user.contact?.lastName}"
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
				redirect(action:'lists')
			}
		}		
		
		render template: "show", model:[totalRevenue:totalRevenue,languageId:languageId,user:user, invoice:invoice, delegatedInvoices:delegatedInvoices, payments:payments]
	}
}
