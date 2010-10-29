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
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.ItemPriceDTOEx;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;
import com.sapienter.jbilling.server.item.CurrencyBL;

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
		log.info "Edit: params.selectedId=" + params.selectedId
		log.info "Edit: params.Id=" + params.id
		
		Integer itemId= params?.id?.toInteger()
		log.info "Editing item=" + itemId
		ItemDTO dto= ItemDTO.findById(itemId)
		boolean exists= (dto!=null)		
		UserBL userbl = new UserBL(webServicesSession.getCallerId());
		Integer languageId = userbl.getEntity().getLanguageIdField();
		Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
		CurrencyDTO[] currs= new CurrencyBL().getCurrencies(languageId, entityId)
		log.info "LanguageId=" + languageId + " EntityId=" + entityId + " found Currencies=" + currs.length
		render(view:"addEdit", model: [item:dto, exists:exists,languageId:languageId, currencies:currs])
	}
	
	def changeLanguage = {
		log.info "Id=" + params.id + " languageId=" + params.languageId			
		ItemDTO dto= ItemDTO.findById(params.id?.toInteger())
		boolean exists= (dto!=null)
		UserBL userbl = new UserBL(webServicesSession.getCallerId());		
		Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
		CurrencyDTO[] currs= new CurrencyBL().getCurrencies(languageId, entityId)
		log.info "LanguageId=" + languageId + " EntityId=" + entityId + " found Currencies=" + currs.length
		render(view:"addEdit", model: [item:dto, exists:exists,languageId:languageId, currencies:currs])		
	}
	
	def add = {
		log.info "Add: " + params["id"]
		UserBL userbl = new UserBL(webServicesSession.getCallerId());
		Integer languageId = userbl.getEntity().getLanguageIdField();
		Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
		CurrencyDTO[] currs= new CurrencyBL().getCurrencies(languageId, entityId)		
		log.info "LanguageId=" + languageId + " EntityId=" + entityId + " found Currencies=" + currs.length
		render(view:"addEdit", model:[languageId:languageId, currencies:currs])
	}
	
	def updateOrCreate ={
		ItemDTOEx dto= new ItemDTOEx();
		log.info "Item Id=" + params.id 
		log.info "pricesCnt=" + params.pricesCnt
		int pricesCnt= 1 + params.pricesCnt?.toInteger()
		List<ItemPriceDTOEx> prices= new ArrayList<ItemPriceDTOEx>();		
		for (int iter=0 ; iter < pricesCnt ; iter++ ) 
		{
			prices.add(new ItemPriceDTOEx())
		}
		dto.setPrices(prices);		
		bindData(dto, params)
		
		log.info "dto.id=" + dto.getId()
		log.info "dto.number=" + dto.getNumber()
		log.info "dto.description=" + dto.getDescription()
		log.info "dto.types=" + dto.getTypes()
		log.info "dto.percentage=" + dto.getPercentage()
		
		log.info "dto.prices=" + dto?.prices?.size()
		for (ItemPriceDTOEx price : dto.prices) {
			log.info "dto.prices.currencyId=" + price.getCurrencyId()
			log.info "dto.prices.price=" + price.getPrice()
		}
		
		if (params.hasDecimals)
		{
			dto.setHasDecimals(1)
		} else {
			dto.setHasDecimals(0)
		}
		if (params.priceManual) {
			dto.setPriceManual(1)
		} else {
			dto.setPriceManual(0)
		}
		log.info "dto.hasDecimals=" + dto.getHasDecimals()
		log.info "dto.priceManual=" + dto.getPriceManual()
		if (null != dto.getId() && 0 != dto.getId()) {			
			webServicesSession.updateItem(dto)
		} else {
			webServicesSession.createItem(dto)
		}
		redirect (controller: "item")
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
