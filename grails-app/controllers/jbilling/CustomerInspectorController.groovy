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
import com.sapienter.jbilling.server.order.db.OrderDAS
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO
import com.sapienter.jbilling.server.payment.db.PaymentDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.user.CustomerPriceBL
import com.sapienter.jbilling.server.item.db.ItemDTO
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap;

class CustomerInspectorController {
	
	IWebServicesSessionBean webServicesSession

    def breadcrumbService

	def index = { 
		redirect action: 'inspect', params: params
	}

	def inspect = {
		def user = params.id ? UserDTO.get(params.int('id')) : null

        if (!user) {
            flash.error = 'no.user.found'
            flash.args = [ params.id ]
            return // todo: show generic error page
        }

        def revenue =  webServicesSession.getTotalRevenueByUser(user.id)
        def subscriptions = webServicesSession.getUserSubscriptions(user.id)

        // last invoice
        def invoiceIds = webServicesSession.getLastInvoices(user.id, 1)
        def invoice = invoiceIds ? InvoiceDTO.get(invoiceIds.first()) : null

        // last payment
        def paymentIds = webServicesSession.getLastPayments(user.id, 1)
        def payment = paymentIds ? PaymentDTO.get(paymentIds.first()) : null

        // extra contacts and contact type descriptions
        def contacts = user ? webServicesSession.getUserContactsWS(user.id) : null
        for (ContactWS contact : contacts) {
            def contactType = new ContactTypeDAS().find(contact.type)
            contact.setContactTypeDescr(contactType?.getDescription(session['language_id'].toInteger()))
        }

        // used to find the next invoice date
        def cycle = new OrderDAS().findEarliestActiveOrder(user.id)

        // all customer prices and products
        def company = CompanyDTO.get(session['company_id'])
        def itemTypes = company.itemTypes.sort{ it.id }
        params.typeId = itemTypes?.asList()?.first()?.id

        def products = getProducts(company, params)
        def prices = new CustomerPriceBL(user.id).getCustomerPrices()

        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))

        [
                user: user,
                contacts: contacts,
                invoice: invoice,
                payment: payment,
                subscriptions: subscriptions,
                prices: prices,
                company: company,
                itemTypes: itemTypes,
                products: products,
                currencies: currencies,
                cycle: cycle,
                revenue: revenue
        ]
    }

    def filterProducts = {
        def company = CompanyDTO.get(session['company_id'])
        def itemTypes = company.itemTypes.sort{ it.id }

        // filter using the first item type by default
        if (params.typeId == null)
            params.typeId = itemTypes?.asList()?.first()?.id

        def products = getProducts(company, params)

        render template: 'products', model: [ itemTypes: itemTypes, products: products ]
    }

    def productPrices = {
        def prices = new CustomerPriceBL(params.int('userId')).getCustomerPrices(params.int('id'))

        render template: 'prices', model: [ prices: prices, userId: params.userId, itemId: params.id ]
    }

    def showAll = {
        def prices = new CustomerPriceBL(params.int('userId')).getCustomerPrices()

        render template: 'prices', model: [ prices: prices ]
    }

    /**
     * Get a filtered list of products
     *
     * @param company company
     * @param params parameter map containing filter criteria
     * @return filtered list of products
     */
    def getProducts(CompanyDTO company, GrailsParameterMap params) {
        // filter on item type, item id and internal number
        def products = ItemDTO.createCriteria().list() {
            and {
                if (params.filterBy && params.filterBy != message(code: 'products.filter.by.default')) {
                    or {
                        eq('id', params.int('filterBy'))
                        ilike('internalNumber', "%${params.filterBy}%")
                    }
                }

                if (params.typeId) {
                    itemTypes {
                        eq('id', params.int('typeId'))
                    }
                }

                isEmpty('plans')
                eq('deleted', 0)
                eq('entity', company)
            }
            order('id', 'asc')
        }

        // if no results found, try filtering by description
        if (!products && params.filterBy) {
            products = ItemDTO.createCriteria().list() {
                and {
                    isEmpty('plans')
                    eq('deleted', 0)
                    eq('entity', company)
                }
                order('id', 'asc')
            }.findAll {
                it.getDescription(session['language_id']).toLowerCase().contains(params.filterBy.toLowerCase())
            }
        }

        return products
    }

    def getCurrencies() {
        def currencies = new CurrencyBL().getCurrencies(session['language_id'].toInteger(), session['company_id'].toInteger())
        return currencies.findAll { it.inUse }
    }

}
