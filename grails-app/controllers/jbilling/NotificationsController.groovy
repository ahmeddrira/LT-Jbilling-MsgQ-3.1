package jbilling

import com.sapienter.jbilling.server.notification.db.NotificationMessageDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.notification.db.NotificationMessageTypeDTO;
import com.sapienter.jbilling.server.util.db.NotificationCategoryDTO;
import com.sapienter.jbilling.server.util.db.NotificationCategoryDAS;

class NotificationsController {
	
	def webServicesSession

    def index = { redirect (action:listCategories)}
	
	def listCategories ={
		UserBL userbl = new UserBL(webServicesSession.getCallerId());
		Integer languageId = userbl.getEntity().getLanguageIdField();
		List categorylist= NotificationCategoryDTO.list()
		log.info "Categories found= " + categorylist?.size()
		render (view:"categories", model:[lst:categorylist, languageId:languageId])
	}
	
	def lists={
		Integer languageId = webServicesSession.getCallerLanguageId();
		Integer entityId = webServicesSession.getCallerCompanyId();
		log.info "entityId=" + entityId
		Integer categoryId= params.selectedId.toInteger()
		log.info "Category Id selected=" + categoryId
		
		def lstByCateg= NotificationMessageTypeDTO.findAllByCategory(new NotificationCategoryDTO(categoryId))
				
		log.info "size of messages=" + lstByCateg.size() + " of total " + NotificationMessageTypeDTO.list()?.size()
		[lst:lstByCateg, languageId:languageId, entityId:entityId]
	}
	
	def preferences ={
		
	}
	
	def edit = {
		log.info "Id is=" + params.id
		Integer messageTypeId= params.id.toInteger()
				
		Integer languageId = webServicesSession.getCallerLanguageId();
		Integer entityId = webServicesSession.getCallerCompanyId();
		
		NotificationMessageTypeDTO typeDto= NotificationMessageTypeDTO.findById(messageTypeId)
		NotificationMessageDTO dto=null
		for (NotificationMessageDTO messageDTO: typeDto.getNotificationMessages()) {
			if (messageDTO?.getEntity()?.getId() == entityId 
				&& messageDTO.getLanguage().getId()== languageId) {
				dto= messageDTO;
				break;
			}
		}
		
		[dto:dto, languageId:languageId, entityId:entityId]
	}
	
}
