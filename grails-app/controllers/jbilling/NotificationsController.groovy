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
import com.sapienter.jbilling.common.SessionInternalError


@Secured(['isAuthenticated()'])
class NotificationsController {
    
    def webServicesSession
	def breadcrumbService
	def viewUtils
	
    def index = { 
        redirect (action: 'listCategories')
    }
    
    def listCategories = {
        List categorylist= NotificationCategoryDTO.list()
        log.debug  "Categories found= ${categorylist?.size()}"
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
        [lst:categorylist]
    }

    def preferences = {
        render template: "preferences", model: [subList:getPreferenceMapByTypeId()]
    }
	
	def list = {
		
		log.debug  "Id=${params.id} selectedId= ${params.selectedId}"
		Integer categoryId= params.int('id')
		def lstByCateg= NotificationMessageTypeDTO.findAllByCategory(new NotificationCategoryDTO(categoryId))
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, categoryId)
		log.debug  "size of messages=" + lstByCateg.size() + " of total " + NotificationMessageTypeDTO.list()?.size()
		if (params.template)
			render template: 'list', model:[lstByCategory:lstByCateg]
		else 
			render (view: 'listCategories', model: [selected: categoryId, lst: NotificationCategoryDTO.list(), lstByCategory: lstByCateg])
	}

	def show = {
		
		log.debug  "Id is=" + params.id
		Integer messageTypeId= params.id.toInteger()

		Integer _languageId= session['language_id']
		if (params.get('language.id')) {
			log.debug  "params.language.id is not null= " + params.get('language.id')
			_languageId= params.get('language.id')?.toInteger()
			log.debug  "setting language id from requrest= " + _languageId
		} 
		
		Integer entityId = webServicesSession.getCallerCompanyId();
		
		NotificationMessageTypeDTO typeDto= NotificationMessageTypeDTO.findById(messageTypeId)
		NotificationMessageDTO dto=null
		for (NotificationMessageDTO messageDTO: typeDto.getNotificationMessages()) {
			if (messageDTO?.getEntity()?.getId()?.equals(entityId) 
				&& messageDTO.getLanguage().getId().equals(_languageId)) {
				dto= messageDTO;
				break;
			}
		}
		
		render template:"show", model:[dto:dto, messageTypeId:messageTypeId, languageDto: LanguageDTO.findById(_languageId), entityId:entityId]
	}
	
	private getPreferenceMapByTypeId () {
		Map<PreferenceDTO> subList= new HashMap<PreferenceDTO>();
		List<PreferenceDTO> masterList= PreferenceDTO.findAllByForeignId(webServicesSession.getCallerCompanyId())
		log.debug  "masterList.size=" + masterList.size()
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
					log.debug  "Adding dto: " + dto.getPreferenceType().getId()
					subList.put(dto.getPreferenceType().getId(), dto)
					break;
			}
		}
		subList
	}
	
	def cancelEditPrefs = {
		render view: "viewPrefs", model: [lst:NotificationCategoryDTO.list(), subList:getPreferenceMapByTypeId()]
	}
	
	def editPreferences = {
		Map<PreferenceDTO> subList= new HashMap<PreferenceDTO>();
		List<PreferenceDTO> masterList= PreferenceDTO.findAllByForeignId(webServicesSession.getCallerCompanyId())
		log.debug  "masterList.size=" + masterList.size()
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
					log.debug  "Adding dto: " + dto.getPreferenceType().getId()
					subList.put(dto.getPreferenceType().getId(), dto)
					break;
			}
		}
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
		[subList:subList, languageId:session['language_id']]
	}

    def savePrefs ={
        log.debug  "pref[5].value=" + params.get("pref[5].value")
		List<PreferenceWS> prefDTOs
		
		try {
			prefDTOs=bindDTOs(params)
		} catch (SessionInternalError e) {
			viewUtils.resolveExceptionForValidation(flash, session.locale, e);
			redirect action: "editPreferences"
		}
        log.debug  "Calling: webServicesSession.saveNotificationPreferences(prefDTOs); List Size: " + prefDTOs.size()
		PreferenceWS[] array= new PreferenceWS[prefDTOs.size()]
		array= prefDTOs.toArray(array)
        try {
			webServicesSession.saveNotificationPreferences(array)
        } catch (SessionInternalError e) {
			log.error "Error: " + e.getMessage()
			flash.errorMessages= e.getErrorMessages()
			//boolean retValue = viewUtils.resolveExceptionForValidation(flash, session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE', e);			
		}
        log.debug  "Finished: webServicesSession.saveNotificationPreferences(prefDTOs);"
		if (flash.errorMessages?.size() > 0 )
		{
			redirect (action:editPreferences)
		} else {
	        flash.message = 'preference.saved.success'
	        redirect (action:cancelEditPrefs)
		}
    }


	def List<PreferenceWS> bindDTOs(params)  {
        List<PreferenceWS> prefDTOs= new ArrayList<PreferenceWS>();
        def count = params.recCnt.toInteger()
        for (int i=0; i < count; i++) {
            log.debug  "loop=" + params.get("pref["+i+"].id")
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
						def val=params["pref["+i+"].value"]
						try {
							dto.setIntValue(val.toInteger())
						} catch (NumberFormatException e) {
							SessionInternalError exception = new SessionInternalError("Validation of Preference Value");
							String [] errmsgs= new String[1]
							errmsgs[0]="PreferenceWS,intValue,validation.error.nonnumeric.days.order.notification," + val;
							exception.setErrorMessages(errmsgs);
							throw exception;
						}
                    } else {
                        dto.setIntValue(0)
                    }
                    dto.setStrValue(null)
                    dto.setFloatValue(null)
            }
            log.debug  "dto.intValue=" + dto.getIntValue()
            prefDTOs.add(dto);
        }
        return prefDTOs;
    }

	def edit = {
		
		//set cookies here..
		log.debug  ("doNotAskAgain=" + params.doNotAskAgain + " askPreference=" + params.askPreference)
		
		def askPreference=request.getCookie("doNotAskAgain")
		log.debug  ("Cooke set to was=" + askPreference)
		if ( "true".equals(params.doNotAskAgain) ){
			response.setCookie('doNotAskAgain', String.valueOf(params.askPreference),604800)
			log.debug  ("Setting the cookie to value ${params.askPreference}")
			askPreference= params.askPreference
		}
		
		log.debug  "Id is=" + params.id
		Integer messageTypeId= params.id?.toInteger()
		
		if (!messageTypeId) {
			redirect action: 'listCategories'
		}
		
		Integer _languageId= session['language_id']
		if (params.get('language.id')) {
			log.debug  "params.language.id is not null= ${params.get('language.id')}"
			_languageId= params.get('language.id')?.toInteger()
			log.debug  "setting language id from requrest= ${_languageId}"
		}

		Integer entityId = webServicesSession.getCallerCompanyId()

		NotificationMessageTypeDTO typeDto= NotificationMessageTypeDTO.findById(messageTypeId)
		NotificationMessageDTO dto=null
		for (NotificationMessageDTO messageDTO: typeDto.getNotificationMessages()) {
			if (messageDTO?.getEntity()?.getId().equals(entityId)
			&& messageDTO.getLanguage().getId().equals(_languageId)) {
				dto= messageDTO;
				break;
			}
		}
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, messageTypeId)
		
		[dto:dto, messageTypeId: messageTypeId, languageId:_languageId, entityId:entityId, askPreference:askPreference]
	}
	
	def saveAndRedirect = {
		try {
			saveAction(params)
		} catch (SessionInternalError e) {
			log.error "Error: " + e.getMessage()
			flash.error= "error.illegal.modification"
			//
		}
        redirect (action:edit, params:params)
    }
    
    def saveNotification = {
        log.debug  "_Id= " + params._id
		def _id=params._id
        try {
			saveAction(params)
		} catch (SessionInternalError e) {
			log.error "Error: " + e.getMessage()
			flash.error= "error.illegal.modification"
		}
        redirect (action: 'cancelEdit', params: [id: _id])
    }
    
    def saveAction(params) {
        NotificationMessageDTO msgDTO = new NotificationMessageDTO()
        msgDTO.setLanguage(new LanguageDTO())
        msgDTO.setEntity(new CompanyDTO())
        bindData(msgDTO, params)

        MessageDTO messageDTO= new MessageDTO()
        log.debug  "params.get('_languageId')?.toInteger() = " + params.get('_languageId')?.toInteger()
        messageDTO.setLanguageId(params.get('_languageId')?.toInteger())
        messageDTO.setTypeId(params._id.toInteger())
        log.debug  "params.useFlag=" + params.useFlag
        log.debug  "params.useFlag && 0 = msgDTO.getUseFlag()=" + ( (params.useFlag) && 0 == msgDTO.getUseFlag() )
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
        log.debug  "msgDTO.language.id=" + messageDTO?.getLanguageId()
        log.debug  "msgDTO.type.id=" + messageDTO?.getTypeId()
        log.debug  "msgDTO.use.flag=" + messageDTO.getUseFlag()
        log.debug  "entityId= " + entityId

		if (entityId != webServicesSession.getCallerCompanyId()) {
			throw new SessionInternalError("Cannot update another company's data.")
		}
		webServicesSession.createUpdateNofications(messageId, messageDTO)
		flash.message = 'notification.save.success'
    }

    def MessageSection[] bindSections (params) {
        MessageSection[] lines= new MessageSection[3];
        Integer section= null;
        String content= null;
        MessageSection obj= null;
        
        for ( int i=1; i<= 3 ; i++) {
            log.debug  "messageSections[" + i + "].section=" + params.get("messageSections[" + i + "].section")
            log.debug  "messageSections[" + i + "].id=" + params.get("messageSections[" + i + "].id")
            
            if (params.get("messageSections[" + i + "].notificationMessageLines.content")) {
                content= params.get("messageSections[" + i + "].notificationMessageLines.content")
                obj= new MessageSection(i, content)
            } else {
                obj= new MessageSection(i, "")
            }	        
            lines[(i-1)]= obj;
        }
        log.debug  "Line 1= " + lines[0]
        log.debug  "Line 2= " + lines[1]
        log.debug  "Line 3= " + lines[2]
        return lines;
    }
	
	def cancelEdit = {
		log.debug  "id=${params['id']}"
		NotificationMessageTypeDTO typeDto= NotificationMessageTypeDTO.findById( Integer.parseInt (params["id"]) )
		Integer entityId= webServicesSession.getCallerCompanyId()
		NotificationMessageDTO dto=null
		Integer languageId= session['language_id']
		for (NotificationMessageDTO messageDTO: typeDto.getNotificationMessages()) {
			if (messageDTO?.getEntity()?.getId().equals(entityId)
			&& messageDTO.getLanguage().getId().equals(languageId)) {
				dto= messageDTO;
				break;
			}
		}

		def lstByCateg= NotificationMessageTypeDTO.findAllByCategory(new NotificationCategoryDTO(typeDto.getCategory().getId()))

		[lstByCategory:lstByCateg, entityId:entityId, dto:dto, messageTypeId:typeDto.getId(), languageDto: LanguageDTO.findById(languageId)]

	}

}
