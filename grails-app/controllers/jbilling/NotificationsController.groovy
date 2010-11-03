package jbilling

import com.sapienter.jbilling.server.notification.db.NotificationMessageDTO;
import com.sapienter.jbilling.server.notification.db.NotificationMessageSectionDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.notification.db.NotificationMessageTypeDTO;
import com.sapienter.jbilling.server.util.db.NotificationCategoryDTO;
import com.sapienter.jbilling.server.util.db.NotificationCategoryDAS;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.util.db.LanguageDTO;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.MessageSection;
import com.sapienter.jbilling.server.util.db.PreferenceDTO;
import com.sapienter.jbilling.server.util.db.PreferenceTypeDTO;
import com.sapienter.jbilling.server.util.db.JbillingTable;

class NotificationsController {
    
    def webServicesSession
    
    def index = { 
        redirect (action:listCategories)
    }
    
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
    
    def preferences = {
        Map<PreferenceDTO> subList= new HashMap<PreferenceDTO>();
        List<PreferenceDTO> masterList= PreferenceDTO.findAllByForeignId(webServicesSession.getCallerCompanyId())
        log.info "masterList.size=" + masterList.size()
        for(PreferenceDTO dto: masterList) {
            Integer prefid= dto.getPreferenceType().getId()
            if (( prefid >= 13 && prefid <= 17) || ( prefid >= 21 && prefid <= 23 ) ) {
                log.info "Adding dto: " + dto.getPreferenceType().getId()
                subList.put(dto.getPreferenceType().getId(), dto)
            }
        }
        
        Integer languageId = webServicesSession.getCallerLanguageId();
        [subList:subList, languageId:languageId]
    }
    
    def savePrefs ={
        log.info "pref[5].value=" + params.get("pref[5].value")
        List<PreferenceDTO> prefDTOs=bindDTOs(params)
        log.info "Calling: webServicesSession.saveNotificationPreferences(prefDTOs); List Size: " + prefDTOs.size()
        webServicesSession.saveNotificationPreferences(prefDTOs);
        log.info "Finished: webServicesSession.saveNotificationPreferences(prefDTOs);"
        
        redirect (action:listCategories)
    }
    
    
    def List<PreferenceDTO> bindDTOs(params)  {
        List<PreferenceDTO> prefDTOs= new ArrayList<PreferenceDTO>();
        def count = params.recCnt.toInteger()
        for (int i=0; i < count; i++) {
            log.info "loop=" + params.get("pref["+i+"].id")
            PreferenceDTO dto = new PreferenceDTO()
            dto.setPreferenceType(new PreferenceTypeDTO())
            dto.setJbillingTable(new JbillingTable())
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
    
    def edit = {
        
        log.info "Id is=" + params.id
        Integer messageTypeId= params.id.toInteger()
        
        Integer languageId;
        if (params.get('language.id')) {
            log.info "params.language.id is not null= " + params.get('language.id')
            languageId= params.get('language.id')?.toInteger()
            log.info "setting language id from requrest= " + languageId
        } else {
            languageId = webServicesSession.getCallerLanguageId();            
            log.info "setting users language id"
        }
        
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
    
    def saveAndRedirect = {
        
    }
    
    def saveNotification = {
        log.info "_Id= " + params._id
        
        NotificationMessageDTO msgDTO = new NotificationMessageDTO()
        msgDTO.setLanguage(new LanguageDTO())
        msgDTO.setEntity(new CompanyDTO())
        bindData(msgDTO, params)
        
        MessageDTO messageDTO= new MessageDTO()
        messageDTO.setLanguageId(msgDTO.getLanguage().getId())
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
        
        redirect (action:listCategories)
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
