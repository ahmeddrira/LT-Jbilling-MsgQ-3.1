package jbilling

import com.sapienter.jbilling.server.process.db.BillingProcessDAS;

class BillingController {

	
	def webServicesSession
	def viewUtils
	def recentItemService
	def breadcrumbService
	

	def index = {
		render "Find billing cycle here"
	}
	
	def runBilling = {
	
		render "run billing if not already running"
	}
		
}
