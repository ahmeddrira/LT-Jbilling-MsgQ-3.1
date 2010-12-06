package jbilling

import java.util.List;

import com.sapienter.jbilling.server.util.db.LanguageDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.item.db.ItemTypeDAS;
import com.sapienter.jbilling.server.item.db.ItemTypeDTO;
import com.sapienter.jbilling.server.item.ItemTypeWS;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.ItemDAS;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.ItemPriceDTOEx;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import grails.plugins.springsecurity.Secured;

@Secured(['isAuthenticated()'])
class ProductController {
	
	def webServicesSession   
	Integer languageId= session["language_id"]
	int typeId
	
	def index = {
		log.info 'Index called..'
		def companyId= webServicesSession.getCallerCompanyId()
		log.info "Company ID " + companyId
		// TODO Code review. Can instead the web services call be use to fetch the categories?
		// Using a finder is not bad, but using the API is better.
		List<ItemTypeDTO> categories= ItemTypeDTO.findAllByEntity(new CompanyDTO(companyId))
		log.info categories
		[categories:categories]
	}
	
	
	def addEditCategory = {
		//redirect to add item category form
		log.info "addEditCategory - if Edit id=${params.id}"		
		ItemTypeDTO dto= null
		if (params.id) {
			dto= ItemTypeDTO.findById(params.id)
		}
		render view: 'addEdit/category', model:[dto:dto, languageId:languageId]
	}
	
	def saveCategory = {
		log.info 'Save called..'
		int loggedUserId= webServicesSession.getCallerId()
		log.info "Logged User Id="+ loggedUserId
		
		def dto = new ItemTypeWS()
		bindData(dto, params)
		
		log.info "dto.id=" + dto.getId() + " dto.description=" + dto.getDescription() + " dto.orderLineTypeId=" + dto.getOrderLineTypeId()
		try {
			if (dto.getId() == 0 || !(dto.getId()) ){
				webServicesSession.createItemCategory(dto)
			} else {
				webServicesSession.updateItemCategory(dto)
			}
		} catch (SessionInternalError e) {
			log.error "Error Updating/Creating Item Category " + dto.getId()
			flash.errorMessages?.addAll(e.getErrorMessages())
			//boolean retValue = viewUtils.resolveExceptionForValidation(flash, session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE', e);
		}
		if (!(flash.errorMessages?.size() > 0) )
		{
			flash.message = message(code: 'item.category.saved')
		}
		//TODO move this to product controller
		redirect (action: 'index')
	}
	
	def deleteCategory = {
		Integer itemId= params.id?.toInteger()
		log.info "Deleting item type=" + itemId
		
		try {
			ItemTypeDTO dto= ItemTypeDTO.findById(itemId)
			Set items= dto.getItems()
			log.info "Size of items=" + items?.size()
			if (items) {
				throw new SessionInternalError("This category has products. Remove those before deleting the category.")
			}
			webServicesSession.deleteItemCategory(itemId);
			flash.message = "item.category.deleted"
		} catch (SessionInternalError e) {
			log.error "Error delete Item Category " + itemId
			flash.error = "item.category.delete.failed"
		}
		flash.args= [itemId]
		redirect (action: index)
	}
	
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
		render template: "type", model:[list: items,languageId:languageId]
	}
	
	def showAll = {
		
		log.info "ProductController.showAll[" + ItemDTO.findAll().size() + "]"
		[list:ItemDTO.findAll(),languageId:languageId]
		
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
	
	def addEditProduct ={
		log.info "Edit: params.Id=" + params.id
		ItemDTO dto=null
		if (params.id) {			
			Integer itemId= params?.id?.toInteger()
			log.info "Editing item=" + itemId
			dto= ItemDTO.findById(itemId)
		}
		boolean exists= (dto!=null)
		UserBL userbl = new UserBL(webServicesSession.getCallerId());
		Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
		CurrencyDTO[] currs= new CurrencyBL().getCurrencies(languageId, entityId)
		log.info "LanguageId=" + languageId + " EntityId=" + entityId + " found Currencies=" + currs.length
		render view:"addEdit/product", model: [item:dto, exists:exists,languageId:languageId, currencies:currs]
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
		render(view:"addEdit/product", model: [item:dto, exists:exists,languageId:_languageId, currencies:currs])		
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
		int pricesCnt= params.pricesCnt?.toInteger()
		log.info "pricesCnt= ${pricesCnt}"
		List<ItemPriceDTOEx> prices= new ArrayList<ItemPriceDTOEx>();
		for (int iter=0 ; iter < pricesCnt ; iter++ ) 
		{
			prices.add(new ItemPriceDTOEx())
		}
		dto.setPrices(prices);
		bindData(dto, params)

		for (ItemPriceDTOEx price : dto.prices) {
			if (!price.getPrice())
			{
				price.setPrice "0"
				log.info "dto.prices.price=${price.getPrice()}"
				log.info "dto.prices.currencyId=${price.getCurrencyId()}"
			}
		}
		
		dto.setHasDecimals((params.hasDecimals? 1: 0))
		dto.setPriceManual((params.priceManual? 1 : 0))
		log.info "dto.hasDecimals=" + dto.getHasDecimals()
		log.info "dto.priceManual=" + dto.getPriceManual()
		Integer _languageId= params.languageId?.toInteger()

		log.info "dto.id=" + dto.getId()
		log.info "dto.number=" + dto.getNumber()
		log.info "dto.description=" + dto.getDescription()
		log.info "dto.types=" + dto.getTypes()
		log.info "dto.percentage=" + dto.getPercentage()
		log.info "dto.prices=" + dto?.prices?.size()

		try{
			if (null != dto.getId() && 0 != dto.getId()) {
				webServicesSession.updateItem(dto)
				flash.messsage = message (code: 'item.update.success')
			} else {
				webServicesSession.createItem(dto)
				flash.messsage = message (code: 'item.create.success')
			}
		} catch (SessionInternalError e) {
			log.error "Error Updating/Creating ${dto.getId()}\n" + e.printStackTrace() 
			flash.errorMessages?.addAll(e.getErrorMessages())
			//boolean retValue = viewUtils.resolveExceptionForValidation(flash, session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE', e);
		}

		flash.args= dto.getId()
		redirect (action: "index")
	}
	
	def deleteProduct = {
		def itemId= params.id?.toInteger()
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
				flash.message = 'item.delete.success'
			}
			//[id:typeId]
		} catch (SessionInternalError e) {
			log.error "Error delete Item " + itemId
			flash.error = message(code: 'item.delete.failed')
		}
		flash.args= [itemId]
		redirect (action: "index")
	}
	
}
