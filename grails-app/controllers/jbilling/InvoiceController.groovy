package jbilling

import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.server.util.WebServicesSessionSpringBean;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.user.UserWS;

class InvoiceController {
	
	def IWebServicesSessionBean webServicesSession

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
		log.info "Show Invoice " + params.id
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			int id= Integer.parseInt(params["id"])
			try {
				invoice= webServicesSession.getInvoiceWS(id)
				log.info "Found invoice. Loading..."
				user= webServicesSession.getUserWS(invoice.getUserId())
			} catch (Exception e) {
				//TODO add messages to properites and set here
				//flash.message = message(code: 'invoices.empty')
				//flash.args= [params["id"]]
			}
		}
		
		List<Integer> delegatedInvoices= new ArrayList<Integer>()
		InvoiceWS temp= invoice;
		while (temp.getDelegatedInvoiceId()) {
			delegatedInvoices.add(temp.getDelegatedInvoiceId())
			temp= webServicesSession.getInvoiceWS(temp.getDelegatedInvoiceId())
		}
		
		[user:user, invoice:invoice, delegatedInvoices:delegatedInvoices]
	}
}
