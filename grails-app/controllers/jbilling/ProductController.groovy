package jbilling

import com.sapienter.jbilling.server.util.db.LanguageDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.item.db.ItemTypeDAS;
import com.sapienter.jbilling.server.item.db.ItemTypeDTO;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.ItemDAS;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.common.SessionInternalError;

class ProductController {
	
	def webServicesSession
    def index = { render "nothing to see here."}
	int typeId
	
	def type = {		
		log.info params["id"]		
		if (params["id"] && params["id"].matches("^[0-9]+")) {			
			typeId= Integer.parseInt(params["id"])			
			ItemTypeDTO dto= ItemTypeDTO.findById(typeId);
			if ( dto.getEntity().getId() == webServicesSession.getCallerCompanyId() ) {
				log.info "caller id and itemtype entity id are same."								
			}			
			log.info "Lets see the item size here.. " + dto.getItems().size()
			log.info "Showing products of typeId=" + typeId
			[list:dto.getItems()]
		} else {
			redirect (action: index)
		}		
	}
	
	def showAll = {
		log.info "ProductController.showAll[" + ItemDTO.findAll().size() + "]"
		// render the view with the specified model
		render(view:"type",model:[list:ItemDTO.findAll()])
	}
	
	def show = {
		log.info params["id"]
		def prodId= params.productId.toInteger()
		log.info "Showing item id=" + prodId 
		ItemDTO dto = new ItemBL(prodId).getEntity();
		// get the info from the caller
		UserBL userbl = new UserBL(webServicesSession.getCallerId());
		Integer languageId = userbl.getEntity().getLanguageIdField();
		LanguageDTO lang= LanguageDTO.findById(languageId);
		String language= lang.getDescription();
		log.info "Language: " + language
		[item:dto, languageId:languageId, language:language]
	}
	
	def edit = {
		log.info params["id"]
	}
	
	def del = {
		def itemId= params.selectedId.toInteger()
		log.info "Deleting item=" + itemId
		try {
			List lines= OrderLineDTO.findAllByItem(new ItemDAS().find(itemId))
			log.info "Lines returned=" + lines?.size()
			if (lines){
				log.info "Orders exists for item " + itemId
				throw new SessionInternalError(lines.size() + "Orders exists for Item.");
			} else {
				log.info "Orders DO NOT exists for item " + itemId
				webServicesSession.deleteItem(itemId)
			}
			//[id:typeId]
		} catch (SessionInternalError e) {
			log.error "Error delete Item " + itemId
			flash.message = message(code: 'item.delete.failed')
		}
		flash.args= [itemId]
		redirect (controller: "item")
	}
	
}
