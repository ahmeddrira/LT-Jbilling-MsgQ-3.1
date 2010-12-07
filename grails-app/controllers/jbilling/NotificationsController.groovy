package jbilling

import com.sapienter.jbilling.server.notification.MessageDTO
import com.sapienter.jbilling.server.notification.MessageSection
import com.sapienter.jbilling.server.notification.db.NotificationMessageDTO
import com.sapienter.jbilling.server.notification.db.NotificationMessageTypeDTO
import com.sapienter.jbilling.server.user.UserBL
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.server.util.PreferenceTypeWS
import com.sapienter.jbilling.server.util.PreferenceWS
import com.sapienter.jbilling.server.util.db.LanguageDTO
import com.sapienter.jbilling.server.util.db.NotificationCategoryDTO
import com.sapienter.jbilling.server.util.db.PreferenceDTO
import grails.plugins.springsecurity.Secured
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.client.ViewUtils

@Secured(['isAuthenticated()'])
class NotificationsController {
    
    def webServicesSession
	ViewUtils viewUtils
	Integer languageId = session.language_id
	
    def index = { 
        redirect (action: 'listCategories')
    }
    
    def listCategories = {
        List categorylist= NotificationCategoryDTO.list()
        log.info "Categories found= " + categorylist?.size()
        [lst:categorylist, languageId:languageId]
    }

    def preferences = {
        Map<PreferenceDTO> subList= new HashMap<PreferenceDTO>();
        List<PreferenceDTO> masterList= PreferenceDTO.findAllByForeignId(webServicesSession.getCallerCompanyId())
        log.info "masterList.size=" + masterList.size()
        for(PreferenceDTO dto: masterList) {
            Integer prefid= dto.getPreferenceType().getId()
            switch (prefid) {
                case Constants.PREFERENCE_TYPE_SELF_DELIVER_PAPER_INVOICES:
                case Constants.PREFERENCE_TYPE_INCLUDE_CUSTOMER_NOTES:
                case Constants.PREFERENCE_TYPE_DAY_BEFORE_ORDER_NOTIF_EXP:
                case Constants.PREFERENCE_TYPE_DAY_BEFORE_ORDER_NOTIF_EXP2: 
                case Constants.PREFERENCE_TYPE_DAY_BEFORE_ORDER_NOTIF_EXP3:
                case Constants.PREFERENCE_TYPE_USE_INVOICE_REMINDERS:
                case Constants.PREFERENCE_TYPE_NO_OF_DAYS_INVOICE_GEN_1_REMINDER:
                case Constants.PREFERENCE_TYPE_NO_OF_DAYS_NEXT_REMINDER:
                    log.info "Adding dto: " + dto.getPreferenceType().getId()
                    subList.put(dto.getPreferenceType().getId(), dto)
                    break;
            }
        }        
        [subList:subList, languageId:languageId]
    }

	def editPreferences = {
		Map<PreferenceDTO> subList= new HashMap<PreferenceDTO>();
		List<PreferenceDTO> masterList= PreferenceDTO.findAllByForeignId(webServicesSession.getCallerCompanyId())
		log.info "masterList.size=" + masterList.size()
		for(PreferenceDTO dto: masterList) {
			Integer prefid= dto.getPreferenceType().getId()
			switch (prefid) {
				case Constants.PREFERENCE_TYPE_SELF_DELIVER_PAPER_INVOICES:
				case Constants.PREFERENCE_TYPE_INCLUDE_CUSTOMER_NOTES:
				case Constants.PREFERENCE_TYPE_DAY_BEFORE_ORDER_NOTIF_EXP:
				case Constants.PREFERENCE_TYPE_DAY_BEFORE_ORDER_NOTIF_EXP2:
				case Constants.PREFERENCE_TYPE_DAY_BEFORE_ORDER_NOTIF_EXP3:
				case Constants.PREFERENCE_TYPE_USE_INVOICE_REMINDERS:
				case Constants.PREFERENCE_TYPE_NO_OF_DAYS_INVOICE_GEN_1_REMINDER:
				case Constants.PREFERENCE_TYPE_NO_OF_DAYS_NEXT_REMINDER:
					log.info "Adding dto: " + dto.getPreferenceType().getId()
					subList.put(dto.getPreferenceType().getId(), dto)
					break;
			}
		}
		[subList:subList, languageId:languageId]
	}

    def savePrefs ={
        log.info "pref[5].value=" + params.get("pref[5].value")
        List<PreferenceWS> prefDTOs=bindDTOs(params)
        log.info "Calling: webServicesSession.saveNotificationPreferences(prefDTOs); List Size: " + prefDTOs.size()
		PreferenceWS[] array= new PreferenceWS[prefDTOs.size()]
		array= prefDTOs.toArray(array)
        try {
			webServicesSession.saveNotificationPreferences(array)
        } catch (SessionInternalError e) {
			log.error "Error: " + e.getMessage()
			flash.errorMessages= e.getErrorMessages()
			//boolean retValue = viewUtils.resolveExceptionForValidation(flash, session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE', e);			
		}
        log.info "Finished: webServicesSession.saveNotificationPreferences(prefDTOs);"
		if (flash.errorMessages?.size() > 0 )
		{
			redirect (action:preferences)
		} else {
	        flash.message = 'preference.saved.success'
	        redirect (action:listCategories)
		}
    }
    
    
    def List<PreferenceWS> bindDTOs(params)  {
        List<PreferenceWS> prefDTOs= new ArrayList<PreferenceWS>();
        def count = params.recCnt.toInteger()
        for (int i=0; i < count; i++) {
            log.info "loop=" + params.get("pref["+i+"].id")
            PreferenceWS dto = new PreferenceWS()
            dto.setPreferenceType(new PreferenceTypeWS())
            //dto.setJbillingTable(new JbillingTable())
            dto.setForeignId(webServicesSession.getCallerCompanyId())
            bindData(dto, params["pref["+i+"]"])
            
            switch (i) {
                case 0: 
                case 1:
                case 5: 
                    if (params["pref["+i+"].value"]) {
                        dto.setIntValue(1)
                    } else {
                        dto.setIntValue(0)
                    }
                    dto.setStrValue(null)
                    dto.setFloatValue(null)
                    break;
                default: 
                    if (params["pref["+i+"].value"]) {
                        dto.setIntValue(params["pref["+i+"].value"].toInteger())
                    } else {
                        dto.setIntValue(0)
                    }
                    dto.setStrValue(null)
                    dto.setFloatValue(null)
            }
            log.info "dto.intValue=" + dto.getIntValue()
            prefDTOs.add(dto);
        }
        return prefDTOs;
    }
	
	def lists = {
		Integer entityId = webServicesSession.getCallerCompanyId();
		log.info "entityId=" + entityId + " selectedId=" + params.selectedId
		Integer categoryId= params["id"]?.toInteger()
		//params.selectedId.toInteger()*/
		log.info "Category Id selected=" + categoryId
		def lstByCateg= NotificationMessageTypeDTO.findAllByCategory(new NotificationCategoryDTO(categoryId))
		log.info "size of messages=" + lstByCateg.size() + " of total " + NotificationMessageTypeDTO.list()?.size()
		render template: 'lists', model:[lst:lstByCateg, languageId:languageId, entityId:entityId]
	}
	
    def view = {
        
        log.info "Id is=" + params.id
        Integer messageTypeId= params.id.toInteger()

        Integer _languageId;
        if (params.get('language.id')) {
            log.info "params.language.id is not null= " + params.get('language.id')
            _languageId= params.get('language.id')?.toInteger()
            log.info "setting language id from requrest= " + _languageId
        } else {
            _languageId = languageId            
            log.info "setting users language id"
        }
        
        Integer entityId = webServicesSession.getCallerCompanyId();
        
        NotificationMessageTypeDTO typeDto= NotificationMessageTypeDTO.findById(messageTypeId)
        NotificationMessageDTO dto=null
        for (NotificationMessageDTO messageDTO: typeDto.getNotificationMessages()) {
            if (messageDTO?.getEntity()?.getId() == entityId 
            && messageDTO.getLanguage().getId()== _languageId) {
                dto= messageDTO;
                break;
            }
        }
        
        render template:"show", model:[dto:dto, messageTypeId:messageTypeId, languageDto: LanguageDTO.findById(_languageId), entityId:entityId]
    }
    
	def edit = {
		
		//set cookies here..
		log.info ("doNotAskAgain=" + params.doNotAskAgain + " askPreference=" + params.askPreference)
		
		def askPreference=request.getCookie("doNotAskAgain")
		log.info ("Cooke set to was=" + askPreference)
		if ( "true".equals(params.doNotAskAgain) ){
			response.setCookie("doNotAskAgain", String.valueOf(params.askPreference),604800)
			log.info ("Setting the cookie to value " + params.askPreference)
			askPreference= params.askPreference
		}
		
		log.info "Id is=" + params.id
		Integer messageTypeId= params.id.toInteger()
		
		Integer _languageId;
		if (params.get('language.id')) {
			log.info "params.language.id is not null= " + params.get('language.id')
			_languageId= params.get('language.id')?.toInteger()
			log.info "setting language id from requrest= " + _languageId
		} else {
			_languageId = languageId
			log.info "setting users language id"
		}
		
		Integer entityId = webServicesSession.getCallerCompanyId();
		
		NotificationMessageTypeDTO typeDto= NotificationMessageTypeDTO.findById(messageTypeId)
		NotificationMessageDTO dto=null
		for (NotificationMessageDTO messageDTO: typeDto.getNotificationMessages()) {
			if (messageDTO?.getEntity()?.getId() == entityId
			&& messageDTO.getLanguage().getId()== _languageId) {
				dto= messageDTO;
				break;
			}
		}
		
		[dto:dto, languageId:_languageId, entityId:entityId, askPreference:askPreference]
	}
	
    def saveAndRedirect = {
        saveAction(params)
        redirect (action:edit, params:params)
    }
    
    def saveNotification = {
        log.info "_Id= " + params._id
        saveAction(params)
        redirect (action:listCategories)
    }
    
    def saveAction(params) {
        NotificationMessageDTO msgDTO = new NotificationMessageDTO()
        msgDTO.setLanguage(new LanguageDTO())
        msgDTO.setEntity(new CompanyDTO())
        bindData(msgDTO, params)
        
        MessageDTO messageDTO= new MessageDTO()
        log.info "params.get('_languageId')?.toInteger() = " + params.get('_languageId')?.toInteger()
        messageDTO.setLanguageId(params.get('_languageId')?.toInteger())
        messageDTO.setTypeId(params._id.toInteger())
        log.info "params.useFlag=" + params.useFlag
        log.info "params.useFlag && 0 = msgDTO.getUseFlag()=" + ( (params.useFlag) && 0 == msgDTO.getUseFlag() )
        messageDTO.setUseFlag( (params.useFlag) && 0 == msgDTO.getUseFlag())        
        Integer messageId= null;
        Integer entityId= msgDTO.getEntity().getId()        
        if (params.msgDTOId) {
            messageId= params.msgDTOId.toInteger()
        } else {
            //new record
            messageId= null;
        }        
        messageDTO.setContent(bindSections(params))
        log.info "msgDTO.language.id=" + messageDTO?.getLanguageId()
        log.info "msgDTO.type.id=" + messageDTO?.getTypeId()
        log.info "msgDTO.use.flag=" + messageDTO.getUseFlag()
        log.info "entityId= " + entityId
        webServicesSession.createUpdateNofications(entityId, messageId, messageDTO);
		flash.message = 'notification.save.success'
    }
    
    def MessageSection[] bindSections (params) {   
        MessageSection[] lines= new MessageSection[3];	           
        Integer section= null;
        String content= null;
        MessageSection obj= null;
        
        for ( int i=1; i<= 3 ; i++) {            
            log.info "messageSections[" + i + "].section=" + params.get("messageSections[" + i + "].section")
            log.info "messageSections[" + i + "].id=" + params.get("messageSections[" + i + "].id")
            
            if (params.get("messageSections[" + i + "].notificationMessageLines.content")) {
                content= params.get("messageSections[" + i + "].notificationMessageLines.content")
                obj= new MessageSection(i, content)
            } else {
                obj= new MessageSection(i, "")
            }	        
            lines[(i-1)]= obj;
        }
        log.info "Line 1= " + lines[0]
        log.info "Line 2= " + lines[1]
        log.info "Line 3= " + lines[2]
        return lines;
    }
}
