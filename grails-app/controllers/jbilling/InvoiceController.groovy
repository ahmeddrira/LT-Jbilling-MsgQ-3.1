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
	
	def lists ={ 
		InvoiceWS[] invoices= null;
		log.info "Get invoices list for id [${params?.id}]"
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			int id= Integer.parseInt(params["id"])
			try {
				invoices= webServicesSession.getAllInvoicesForUser(id)
				if (invoices)
					log.info "found invoices [${invoices.size()}]" 
			} catch (Exception e) {
				flash.message = message(code: 'invoices.empty')
				flash.args= [params["id"]]
			}
		}
		[invoices:invoices]
	}
	
	def show = {
		InvoiceWS invoice;
		UserWS user;
		List<PaymentWS> payments;
		BigDecimal totalRevenue;
		String delegatedInvoices= ""
		
		log.info "Show Invoice ${params.id}"
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			int id= Integer.parseInt(params["id"])
			try {
				invoice= webServicesSession.getInvoiceWS(id)
				log.info "Found invoice ${invoice}. Loading..."
				user= webServicesSession.getUserWS(invoice?.getUserId())
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
				flash.message = message(code: 'error.invoice.details')
				flash.args= [params["id"]]
				redirect(action:'lists')
			}
		}		
		
		[totalRevenue:totalRevenue,languageId:languageId,user:user, invoice:invoice, delegatedInvoices:delegatedInvoices, payments:payments]
	}
}
