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
import com.sapienter.jbilling.server.item.CurrencyBL
import grails.plugins.springsecurity.Secured;

@Secured(['isAuthenticated()'])
class ProductController {
	
	def webServicesSession
    def index = { render "nothing to see here."}
	Integer languageId= session["language_id"]
	int typeId
	
	
	def type = {		
		log.info params["id"]		
		ItemTypeDTO dto
		def items
		if (params["id"] && params["id"].matches("^[0-9]+")) {			
			typeId= Integer.parseInt(params["id"])			
			dto= ItemTypeDTO.findById(typeId);
			if ( dto.getEntity().getId() == webServicesSession.getCallerCompanyId() ) {
				log.info "caller id and itemtype entity id are same."								
			}			
			items= dto.getItems()
			log.info "Lets see the item size here.. " + items?.size()
			log.info "Showing products of typeId=" + typeId
		} else {
			redirect (action: index)
		}		
		render template: "type", model:[list: items]
	}
	
	def showAll = {
		
		log.info "ProductController.showAll[" + ItemDTO.findAll().size() + "]"
		render template:"type", model:[list:ItemDTO.findAll()]
		
		//TODO The above should be replaced by below, below throws Itempricingtask error.
		//ItemDTOEx[] allItems= webServicesSession.getAllItems()
		//log.info "ProductController.showAll[" + allItems?.length + "]"		
		//render template:"type", model:[list:allItems]
	}
	
	def show = {
		log.info params["id"]
		def prodId= params["id"]?.toInteger()
		log.info "Showing item id=" + prodId 
		ItemDTO dto = new ItemBL(prodId).getEntity();
		// get the info from the caller		
		LanguageDTO lang= new LanguageDTO(languageId);
		String language= lang.getDescription();
		log.info "Language: " + language
		render template: "show", model:[item:dto, languageId:languageId, language:language]
	}
	
	def edit = {
		log.info "Edit: params.selectedId=" + params.selectedId
		log.info "Edit: params.Id=" + params.id
		
		Integer itemId= params?.id?.toInteger()
		log.info "Editing item=" + itemId
		ItemDTO dto= ItemDTO.findById(itemId)
		boolean exists= (dto!=null)		
		UserBL userbl = new UserBL(webServicesSession.getCallerId());
		Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
		CurrencyDTO[] currs= new CurrencyBL().getCurrencies(languageId, entityId)
		log.info "LanguageId=" + languageId + " EntityId=" + entityId + " found Currencies=" + currs.length
		render(template:"addEdit", model: [item:dto, exists:exists,languageId:languageId, currencies:currs])
	}
	
	def changeLanguage = {
		log.info "Id=" + params.id + " languageId=" + params.languageId
        Integer _languageId = Integer.parseInt(params.languageId)
        ItemDTO dto= null;
        if (params.id)
		{
            dto= ItemDTO.findById(params.id?.toInteger())
		}
		boolean exists= (dto!=null)
		UserBL userbl = new UserBL(webServicesSession.getCallerId());		
		Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
		CurrencyDTO[] currs= new CurrencyBL().getCurrencies(_languageId, entityId)
		log.info "LanguageId=" + _languageId + " EntityId=" + entityId + " found Currencies=" + currs.length
		render(template:"addEdit", model: [item:dto, exists:exists,languageId:_languageId, currencies:currs])		
	}
	
	def add = {
		log.info "Add: " + params["id"]
		UserBL userbl = new UserBL(webServicesSession.getCallerId());
		Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
		CurrencyDTO[] currs= new CurrencyBL().getCurrencies(languageId, entityId)		
		log.info "LanguageId=" + languageId + " EntityId=" + entityId + " found Currencies=" + currs.length
		render(template:"addEdit", model:[languageId:languageId, currencies:currs])
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
        Integer _languageId= params.languageId?.toInteger()

		try{
			if (null != dto.getId() && 0 != dto.getId()) {			
				webServicesSession.updateItem(dto)
				flash.messsage = message (code: 'item.update.success')
			} else {
				webServicesSession.createItem(dto)
				flash.messsage = message (code: 'item.create.success')
			}
		} catch (SessionInternalError e) {
			log.error "Error Updating/Creating Item " + dto.getId()
			flash.errorMessages?.addAll(e.getErrorMessages())				
			//boolean retValue = viewUtils.resolveExceptionForValidation(flash, session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE', e);
		}

		flash.args= dto.getId()
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
