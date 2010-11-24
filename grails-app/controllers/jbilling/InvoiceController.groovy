package jbilling

import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.server.invoice.InvoiceWS;

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
}
