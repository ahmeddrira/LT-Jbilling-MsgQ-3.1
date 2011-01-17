package jbilling

import grails.plugins.springsecurity.Secured;
import com.sapienter.jbilling.server.process.BillingProcessConfigurationWS;
import com.sapienter.jbilling.common.SessionInternalError;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* BillingController
*
* @author Vikas Bodani
* @since 11/01/11
*/
@Secured(['isAuthenticated()'])
class BillingconfigurationController {

	def webServicesSession
	def viewUtils
	def recentItemService
	def breadcrumbService
    
    def index = {
		
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
		def configuration= webServicesSession.getBillingProcessConfiguration()
		boolean isBillingRunning= webServicesSession.isBillingRunning()
		//if (isBillingRunning)
		//{
		//	flash.message = 'prompt.billing.running'
		//}
		[configuration:configuration, isBillingRunning: isBillingRunning]
	}
	
	def saveConfig = {
		
		log.info "${params}"
		def configuration= new BillingProcessConfigurationWS() 
		bindData(configuration, params)

		//set all checkbox values as int
		configuration.setGenerateReport params.generateReport ? 1 : 0
		configuration.setInvoiceDateProcess params.invoiceDateProcess ? 1 : 0 
		configuration.setOnlyRecurring params.onlyRecurring ? 1 : 0
		configuration.setAutoPayment params.autoPayment ? 1 : 0
		configuration.setAutoPaymentApplication params.autoPaymentApplication ? 1 : 0
		configuration.setNextRunDate (new SimpleDateFormat("dd-MMM-yyyy").parse(params.nextRunDate) )
		configuration.setEntityId webServicesSession.getCallerCompanyId()
		
		log.info "Generate Report ${params.generateReport}"
		
		try {
			webServicesSession.createUpdateBillingProcessConfiguration(configuration)
			flash.message = 'billing.configuration.save.success'
		} catch (SessionInternalError e){
			viewUtils.resolveException(flash, session.locale, e);
		} catch (Exception e) {
			log.info e.getMessage()
			flash.error = 'billing.configuration.save.fail'
		}
		
		redirect action: index
	}
	
	def runBilling = {
		
		//TODO Check if billing is already running. We may add a method
		//to IBillingProcessSessionBean that returns true/false
		
		try {
			if (!webServicesSession.isBillingRunning()) {
				webServicesSession.triggerBillingAsync(new Date())
				flash.message = 'prompt.billing.trigger'
			} else {
				flash.error = 'prompt.billing.already.running'
			}
		} catch (Exception e) {
			//e.printStackTrace()
			log.error e.getMessage()
			flash.error = 'error.billing.trigger'
		}
		redirect action: 'index'
	}
	
}
