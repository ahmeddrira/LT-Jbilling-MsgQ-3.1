package jbilling

import com.sapienter.jbilling.server.item.ItemTypeBL;
import com.sapienter.jbilling.server.item.db.ItemTypeDTO;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.server.item.db.ItemTypeDAS;

class ItemController {
	
	def webServicesSession
	List<ItemTypeDTO> categories;

    def index = { 
		log.info 'Index called..'
		def companyId= webServicesSession.getCallerCompanyId()
		log.info "Company ID " + companyId
		categories= ItemTypeDTO.findAllByEntity(new CompanyDTO(companyId))
		log.info categories
		[categories:categories]
	}
	
	def delete = {
		def itemId= params.deleteItemId.toInteger()
		//def delOrderTypeId= params.delOrderTypeId.toInteger()
		log.info "Deleting item type=" + itemId //+ " OrderLineTypeId=" + delOrderTypeId
//		new ItemTypeDAS().delete(new ItemTypeDTO(itemId, webServicesSession.getCallerCompanyId(), delOrderTypeId))
		ItemTypeBL bl = new ItemTypeBL(itemId);
		bl.delete(webServicesSession.getCallerId())
		redirect (action: index)
	}
	
	def save = {
		log.info 'Save called..'
		
		int loggedUserId= webServicesSession.getCallerId()
		log.info "Logged User Id="+ loggedUserId
		bindDTOs(params);
		for (ItemTypeDTO dto : categories) {
			log.info "dto.id=" + dto.getId() + " dto.description=" + dto.getDescription() + " dto.orderLineTypeId=" + dto.getOrderLineTypeId()
			
//			dto.save();
//			ItemTypeDAS das=new ItemTypeDAS();
//			das.save(dto);
			ItemTypeBL bl = new ItemTypeBL(dto.getId());
			if (dto.getId() == 0 || !(dto.getId()) ){
				dto.setEntity(new CompanyDTO(webServicesSession.getCallerCompanyId()))
				new ItemTypeDAS().save(dto)
			} else {
				bl.update(loggedUserId, dto);
			}
		}
		
		redirect (action: index)
	}
	
	def List<ItemTypeDTO> bindDTOs(params)  {
		categories= new ArrayList<ItemTypeDTO>();
		def count = params.recCnt.toInteger()
		for (int i=0; i < count; i++) {
			def dto = new ItemTypeDTO()
			bindData(dto, params["categories["+i+"]"])
			categories.add(dto);
		}
		categories;
	}
}
