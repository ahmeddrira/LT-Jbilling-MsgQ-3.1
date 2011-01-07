package jbilling

import com.sapienter.jbilling.server.process.BillingProcessConfigurationWS;

class BillingController {

	
	def webServicesSession
	def recentItemService
	def breadcrumbService
	
    def index = {
		
		def configuration= webServicesSession.getBillingProcessConfiguration()
		[configuration:configuration]
	}
	
	def saveConfig = {
		//do something
		
		log.info "${params}"
		
		def configuration= new BillingProcessConfigurationWS() 
		
		bindData(configuration, params)
		
		try {
			webServicesSession.createUpdateBillingProcessConfiguration(configuration)
			//flash.message = ''
		}  catch (Exception e) {
			//log.info (e.getMessage())
			//flash.error = 'error.'
		}
		
		redirect action: index
	}
}
