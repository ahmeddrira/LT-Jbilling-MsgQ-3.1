package jbilling

import com.sapienter.jbilling.server.item.db.ItemTypeDAS;
import com.sapienter.jbilling.server.item.db.ItemTypeDTO;
import com.sapienter.jbilling.server.item.db.ItemDTO;

class ProductController {
	
	def webServicesSession
    def index = { render "nothing to see here."}
	
	def type = {
		
		log.info params["id"]
		
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			
			int id= Integer.parseInt(params["id"])
			
			ItemTypeDTO dto= ItemTypeDTO.findById(id);
			if ( dto.getEntity().getId() == webServicesSession.getCallerCompanyId() ) {
				log.info "caller id and itemtype entity id are same."								
			}			

			log.info "Lets see the item size here.. " + dto.getItems().size()
			log.info "Showing products of typeId=" + id
			[list:dto.getItems()]
		} else {
			redirect (action: index)
		}
		
	}
}
