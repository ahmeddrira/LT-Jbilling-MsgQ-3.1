package jbilling

import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.client.ViewUtils
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.user.db.SubscriberStatusDTO;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.contact.db.ContactTypeDTO;
import com.sapienter.jbilling.server.user.contact.db.ContactTypeDAS
import com.sapienter.jbilling.server.user.db.UserDTO
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.order.db.OrderDAS;

class CustomerInspectorController {
	
	IWebServicesSessionBean webServicesSession
	ViewUtils viewUtils
	def filterService

	def index = { 
		redirect action: 'inspect', params: params
	}
	
	def inspect = {
		def user = UserDTO.get(params.int('id'))

        // subscription orders
        def subscriptions = user ? webServicesSession.getUserSubscriptions(user.id) : null

        // extra contacts and contact type descriptions
        def contacts = user ? webServicesSession.getUserContactsWS(user.id) : null
        for (ContactWS contact : contacts) {
            def contactType = new ContactTypeDAS().find(contact.getContactTypeId())
            contact.setContactTypeDescr(contactType?.getDescription(session['language_id'].toInteger()))
        }

        // used to find the next invoice date
        def cycle = new OrderDAS().findEarliestActiveOrder(user.id)

		[ user: user, contacts: contacts, subscriptions: subscriptions, currencies: currencies, cycle: cycle ]
	}

    def getCurrencies() {
        def currencies = new CurrencyBL().getCurrencies(session['language_id'].toInteger(), session['company_id'].toInteger())
        return currencies.findAll { it.inUse }
    }
}
