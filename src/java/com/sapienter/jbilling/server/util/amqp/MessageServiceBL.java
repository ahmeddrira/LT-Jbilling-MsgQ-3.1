package com.sapienter.jbilling.server.util.amqp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.sapienter.jbilling.client.authentication.CompanyUserDetailsService;
import com.sapienter.jbilling.client.authentication.StaticAuthenticationFilter;
import com.sapienter.jbilling.client.authentication.model.User;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.PlanItemBL;
import com.sapienter.jbilling.server.item.PlanItemWS;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.PlanItemDTO;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.mediation.task.IMediationProcess;
import com.sapienter.jbilling.server.mediation.task.MediationResult;
import com.sapienter.jbilling.server.metafields.MetaFieldBL;
import com.sapienter.jbilling.server.metafields.db.EntityType;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.OrderHelper;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.user.CustomerPriceBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.ValidatePurchaseWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.Context;
import com.sapienter.jbilling.server.util.InternationalDescriptionWS;
import com.sapienter.jbilling.server.util.PreferenceBL;
import com.sapienter.jbilling.server.util.WebServicesSessionSpringBean;
import com.sapienter.jbilling.server.util.db.InternationalDescriptionDAS;
import com.sapienter.jbilling.server.util.db.InternationalDescriptionDTO;
import com.sapienter.jbilling.server.util.db.JbillingTable;
import com.sapienter.jbilling.server.util.db.JbillingTableDAS;

/**
 * Helper functions used by the Message Service API.<br>
 * A lot of these helpers are lifted straight from the code in
 * {@link WebServicesSessionSpringBean}. We couldn't reuse the code in this
 * class as it is dependent on Session and SecurityContexts being initialised.
 * 
 * @author smit005
 * 
 */
@Service
public class MessageServiceBL {
	
	private static final Logger LOG = Logger.getLogger(MessageServiceBL.class);

	private User companyUser;

	/**
	 * There is no authentication on the AMQP service, so using the
	 * 'staticAuthenticationFilter' (see resource.groovy) to get the company
	 * user id
	 * 
	 * @return
	 */
	public User getCompanyUser() {
		// TODO: what to do when there is >1 company in jbilling?
		if (companyUser == null) {

			StaticAuthenticationFilter staticAuthenticationFilter = Context
					.getBean("staticAuthenticationProcessingFilter");

			// Format is '<username>;<userid>"
			String[] userDetails = staticAuthenticationFilter.getUsername()
					.split(";");

			if (userDetails.length != 2) {
				throw new SessionInternalError("staticAuthenticationFilter is misconfigured '"
                                        + staticAuthenticationFilter.getUsername() + "'");
			}

			CompanyUserDetailsService companyUserDetails = Context
					.getBean("userDetailsService");

			companyUser = companyUserDetails.getUserService().getUser(
					userDetails[0], Integer.valueOf(userDetails[1]));

		}
		return companyUser;
	}

	/*
	 * Returns the user ID of the authenticated user account making the web
	 * service call.
	 * 
	 * @return caller user ID
	 */
	public Integer getCallerId() {
		return getCompanyUser().getId();
	}

	/**
	 * Returns the company ID of the authenticated user account making the web
	 * service call.
	 * 
	 * @return caller company ID
	 */
	public Integer getCallerCompanyId() {
		return getCompanyUser().getCompanyId();
	}

	/**
	 * Returns the language ID of the authenticated user account making the web
	 * service call.
	 * 
	 * @return caller language ID
	 */
	public Integer getCallerLanguageId() {
		return getCompanyUser().getLanguageId();
	}

	/**
	 * Returns the currency ID of the authenticated user account making the web
	 * service call.
	 * 
	 * @return caller currency ID
	 */
	public Integer getCallerCurrencyId() {
		return getCompanyUser().getCurrencyId();
	}

	/**
	 * Lifted this from {@link WebServicesSessionSpringBean#doValidatePurchase}
	 * 
	 * @param userBL
	 * @param itemIds
	 * @param fields
	 * @return
	 */
	public ValidatePurchaseWS doValidatePurchase(UserBL userBL,
			Integer[] itemIds, String[] fields) {
		if (userBL == null || (itemIds == null && fields == null)) {
			return null;
		}
	
		List<List<PricingField>> fieldsList = null;
		if (fields != null) {
			fieldsList = new ArrayList<List<PricingField>>(fields.length);
			for (int i = 0; i < fields.length; i++) {
				fieldsList.add(new ArrayList(Arrays.asList(PricingField
						.getPricingFieldsValue(fields[i]))));
			}
		}
	
		List<Integer> itemIdsList = null;
		List<BigDecimal> prices = new ArrayList<BigDecimal>();
		List<ItemDTO> items = new ArrayList<ItemDTO>();
	
		if (itemIds != null) {
			itemIdsList = new ArrayList(Arrays.asList(itemIds));
		} else if (fields != null) {
			itemIdsList = new LinkedList<Integer>();
	
			for (List<PricingField> pricingFields : fieldsList) {
				try {
					// Since there is no item, run the mediation process rules
					// to create line/s. This will run pricing and
					// item management rules as well
	
					// fields need to be in records
					Record record = new Record();
					for (PricingField field : pricingFields) {
						record.addField(field, false); // don't care about isKey
					}
					List<Record> records = new ArrayList<Record>(1);
					records.add(record);
	
					PluggableTaskManager<IMediationProcess> tm = new PluggableTaskManager<IMediationProcess>(
							getCallerCompanyId(),
							Constants.PLUGGABLE_TASK_MEDIATION_PROCESS);
	
					IMediationProcess processTask = tm.getNextClass();
	
					MediationResult result = new MediationResult("WS", false);
					result.setUserId(userBL.getDto().getUserId());
					result.setEventDate(new Date());
					ArrayList results = new ArrayList(1);
					results.add(result);
					processTask.process(records, results, "WS");
	
					// from the lines, get the items and prices
					for (OrderLineDTO line : result.getDiffLines()) {
						items.add(new ItemBL(line.getItemId()).getEntity());
						prices.add(line.getAmount());
					}
	
				} catch (Exception e) {
					// log stacktrace
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					pw.close();
					LOG.error("Validate Purchase error: " + e.getMessage()
							+ "\n" + sw.toString());
	
					ValidatePurchaseWS result = new ValidatePurchaseWS();
					result.setSuccess(false);
					result.setAuthorized(false);
					result.setQuantity(BigDecimal.ZERO);
					result.setMessage(new String[] { "Error: " + e.getMessage() });
	
					return result;
				}
			}
		} else {
			return null;
		}
	
		// find the prices first
		// this will do nothing if the mediation process was uses. In that case
		// the itemIdsList will be empty
		int itemNum = 0;
		for (Integer itemId : itemIdsList) {
			ItemBL item = new ItemBL(itemId);
	
			if (fieldsList != null && !fieldsList.isEmpty()) {
				int fieldsIndex = itemNum;
				// just get first set of fields if only one set
				// for many items
				if (fieldsIndex > fieldsList.size()) {
					fieldsIndex = 0;
				}
				item.setPricingFields(fieldsList.get(fieldsIndex));
			}
	
			// todo: validate purchase should include the quantity purchased for
			// validations
			prices.add(item.getPrice(userBL.getDto().getUserId(),
					BigDecimal.ONE, getCallerCompanyId()));
			items.add(item.getEntity());
			itemNum++;
		}
	
		ValidatePurchaseWS ret = userBL.validatePurchase(items, prices,
				fieldsList);
		return ret;
	}

	private Integer zero2null(Integer var) {
		if (var != null && var.intValue() == 0) {
			return null;
		} else {
			return var;
		}
	}

	private Date zero2null(Date var) {
		if (var != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(var);
			if (cal.get(Calendar.YEAR) == 1) {
				return null;
			}
		}
	
		return var;
	
	}

	private void processLines(OrderDTO order, Integer languageId,
			Integer entityId, Integer userId, Integer currencyId,
			String pricingFields) throws SessionInternalError {
	
		OrderHelper.synchronizeOrderLines(order);
	
		for (OrderLineDTO line : order.getLines()) {
			LOG.debug("Processing line " + line);
	
			if (line.getUseItem()) {
				List<PricingField> fields = pricingFields != null ? Arrays
						.asList(PricingField
								.getPricingFieldsValue(pricingFields)) : null;
	
				ItemBL itemBl = new ItemBL(line.getItemId());
				itemBl.setPricingFields(fields);
	
				// get item with calculated price
				ItemDTO item = itemBl.getDTO(languageId, userId, entityId,
						currencyId, line.getQuantity(), order);
				LOG.debug("Populating line using item " + item);
	
				// set price or percentage from item
				if (item.getPrice() == null) {
					line.setPrice(item.getPercentage());
				} else {
					line.setPrice(item.getPrice());
				}
	
				// set description and line type
				line.setDescription(item.getDescription());
				line.setTypeId(item.getOrderLineTypeId());
			}
		}
	
		OrderHelper.desynchronizeOrderLines(order);
	}

	public void validateOrder(OrderWS order) throws SessionInternalError {
		if (order == null) {
			throw new SessionInternalError("Null parameter");
		}
	
		MetaFieldBL.validateMetaFields(getCallerCompanyId(), EntityType.ORDER,
				order.getMetaFields());
	
		order.setUserId(zero2null(order.getUserId()));
		order.setPeriod(zero2null(order.getPeriod()));
		order.setBillingTypeId(zero2null(order.getBillingTypeId()));
		order.setStatusId(zero2null(order.getStatusId()));
		order.setCurrencyId(zero2null(order.getCurrencyId()));
		order.setNotificationStep(zero2null(order.getNotificationStep()));
		order.setDueDateUnitId(zero2null(order.getDueDateUnitId()));
		// Bug Fix: 1385: Due Date may be zero
		// order.setDueDateValue(zero2null(order.getDueDateValue()));
		order.setDfFm(zero2null(order.getDfFm()));
		order.setAnticipatePeriods(zero2null(order.getAnticipatePeriods()));
		order.setActiveSince(zero2null(order.getActiveSince()));
		order.setActiveUntil(zero2null(order.getActiveUntil()));
		order.setNextBillableDay(zero2null(order.getNextBillableDay()));
		order.setLastNotified(null);
	
		// CXF seems to pass empty array as null
		if (order.getOrderLines() == null) {
			order.setOrderLines(new OrderLineWS[0]);
		}
	
		// todo: additional hibernate validations
		// the lines
		for (int f = 0; f < order.getOrderLines().length; f++) {
			OrderLineWS line = order.getOrderLines()[f];
			if (line.getUseItem() == null) {
				line.setUseItem(false);
			}
			line.setItemId(zero2null(line.getItemId()));
			String error = "";
			// if use the item, I need the item id
			if (line.getUseItem()) {
				if (line.getItemId() == null
						|| line.getItemId().intValue() == 0) {
					error += "OrderLineWS: if useItem == true the itemId is required - ";
				}
				if (line.getQuantityAsDecimal() == null
						|| BigDecimal.ZERO.compareTo(line
								.getQuantityAsDecimal()) == 0) {
					error += "OrderLineWS: if useItem == true the quantity is required - ";
				}
			} else {
				// I need the amount and description
				if (line.getAmount() == null) {
					error += "OrderLineWS: if useItem == false the item amount "
							+ "is required - ";
				}
				if (line.getDescription() == null
						|| line.getDescription().length() == 0) {
					error += "OrderLineWS: if useItem == false the description "
							+ "is required - ";
				}
			}
			if (error.length() > 0) {
				throw new SessionInternalError(error);
			}
		}
	}

	public OrderWS doCreateOrder(OrderWS order, boolean create)
			throws SessionInternalError {
	
		validateOrder(order);
		// get the info from the caller
		Integer executorId = getCallerId();
		Integer entityId = getCallerCompanyId();
	
		// convert to a DTO
		OrderBL orderBL = new OrderBL();
		OrderDTO dto = orderBL.getDTO(order);
	
		// we'll need the langauge later
		UserBL bl = new UserBL(order.getUserId());
		Integer languageId = bl.getEntity().getLanguageIdField();
	
		// process the lines and let the items provide the order line details
		LOG.debug("Processing order lines");
		processLines(dto, languageId, entityId, order.getUserId(),
				order.getCurrencyId(), order.getPricingFields());
	
		LOG.info("before cycle start");
		// set a default cycle starts if needed (obtained from the main
		// subscription order, if it exists)
		if (dto.getCycleStarts() == null && dto.getIsCurrent() == null) {
			Integer mainOrderId = orderBL.getMainOrderId(dto.getUser().getId());
			if (mainOrderId != null) {
				// only set a default if preference use current order is set
				PreferenceBL preferenceBL = new PreferenceBL();
				try {
					preferenceBL.set(entityId,
							Constants.PREFERENCE_USE_CURRENT_ORDER);
				} catch (EmptyResultDataAccessException e) {
					// default preference will be used
				}
				if (preferenceBL.getInt() != 0) {
					OrderDAS das = new OrderDAS();
					OrderDTO mainOrder = das.findNow(mainOrderId);
					LOG.debug("Copying cycle starts from main order");
					dto.setCycleStarts(mainOrder.getCycleStarts());
				}
			}
		}
	
		orderBL.set(dto);
		orderBL.recalculate(entityId);
	
		if (create) {
			LOG.debug("creating order");
	
			dto.setId(null);
			dto.setVersionNum(null);
			for (OrderLineDTO line : dto.getLines()) {
				line.setId(0);
				line.setVersionNum(null);
			}
	
			Integer id = orderBL.create(entityId, executorId, dto);
			orderBL.set(id);
			return orderBL.getWS(languageId);
		}
	
		return getWSFromOrder(orderBL, languageId);
	}

	public OrderWS getWSFromOrder(OrderBL bl, Integer languageId) {
		OrderDTO order = bl.getDTO();
		OrderWS retValue = new OrderWS(order.getId(), order.getBillingTypeId(),
				order.getNotify(), order.getActiveSince(),
				order.getActiveUntil(), order.getCreateDate(),
				order.getNextBillableDay(), order.getCreatedBy(),
				order.getStatusId(), order.getDeleted(), order.getCurrencyId(),
				order.getLastNotified(), order.getNotificationStep(),
				order.getDueDateUnitId(), order.getDueDateValue(),
				order.getAnticipatePeriods(), order.getDfFm(),
				order.getIsCurrent(), order.getNotes(),
				order.getNotesInInvoice(), order.getOwnInvoice(), order
						.getOrderPeriod().getId(), order.getBaseUserByUserId()
						.getId(), order.getVersionNum(), order.getCycleStarts());
	
		retValue.setPeriodStr(order.getOrderPeriod().getDescription(languageId));
		retValue.setBillingTypeStr(order.getOrderBillingType().getDescription(
				languageId));
		retValue.setTotal(order.getTotal());
	
		retValue.setMetaFields(MetaFieldBL.convertMetaFieldsToWS(
				getCallerCompanyId(), order));
	
		List<OrderLineWS> lines = new ArrayList<OrderLineWS>();
		for (Iterator<OrderLineDTO> it = order.getLines().iterator(); it
				.hasNext();) {
			OrderLineDTO line = (OrderLineDTO) it.next();
	
			if (line.getDeleted() == 0) {
				OrderLineWS lineWS = new OrderLineWS(line.getId(), line
						.getItem().getId(), line.getDescription(),
						line.getAmount(), line.getQuantity(), line.getPrice(),
						line.getCreateDatetime(), line.getDeleted(), line
								.getOrderLineType().getId(),
						line.getEditable(),
						(line.getPurchaseOrder() != null ? line
								.getPurchaseOrder().getId() : null),
						line.getUseItem(), line.getVersionNum(),
						line.getProvisioningStatusId(),
						line.getProvisioningRequestId());
	
				lines.add(lineWS);
			}
		}
		retValue.setOrderLines(new OrderLineWS[lines.size()]);
		lines.toArray(retValue.getOrderLines());
		return retValue;
	}

	public void doUpdateOrder(OrderWS order) {
		validateOrder(order);
		// start by locking the order
		OrderBL oldOrder = new OrderBL();
		oldOrder.setForUpdate(order.getId());
	
		// do some transformation from WS to DTO :(
		OrderBL orderBL = new OrderBL();
		OrderDTO dto = orderBL.getDTO(order);
	
		// get the info from the caller
		Integer executorId = getCallerId();
		Integer entityId = getCallerCompanyId();
		Integer languageId = getCallerLanguageId();
	
		// see if the related items should provide info
		processLines(dto, languageId, entityId, order.getUserId(),
				order.getCurrencyId(), order.getPricingFields());
	
		// recalculate
		orderBL.set(dto);
		orderBL.recalculate(entityId);
	
		// update
		oldOrder.update(executorId, dto);
	}
	
    private List<InternationalDescriptionWS> getAllItemDescriptions(int itemId) {
        JbillingTableDAS tableDas = Context.getBean(Context.Name.JBILLING_TABLE_DAS);
        JbillingTable table = tableDas.findByName(Constants.TABLE_ITEM);

        InternationalDescriptionDAS descriptionDas = (InternationalDescriptionDAS) Context
                .getBean(Context.Name.DESCRIPTION_DAS);
        Collection<InternationalDescriptionDTO> descriptionsDTO = descriptionDas.findAll(table.getId(), itemId,
                "description");

        List<InternationalDescriptionWS> descriptionsWS = new ArrayList<InternationalDescriptionWS>();
        for (InternationalDescriptionDTO descriptionDTO : descriptionsDTO) {
            descriptionsWS.add(new InternationalDescriptionWS(descriptionDTO));
        }
        return descriptionsWS;
    }

	public ItemDTOEx getItem(Integer userId, Integer itemId) {
        ItemBL helper = new ItemBL(itemId);
        List<PricingField> f = new ArrayList<PricingField>();
        helper.setPricingFields(f);

        Integer callerId = getCallerId(); 
        Integer entityId = getCallerCompanyId(); 
        Integer languageId = getCallerLanguageId(); 

        // use the currency of the given user if provided, otherwise
        // default to the currency of the caller (admin user)
        Integer currencyId = (userId != null
                              ? new UserBL(userId).getCurrencyId()
                              : getCallerCurrencyId());

        ItemDTOEx retValue = helper.getWS(helper.getDTO(languageId, userId, entityId, currencyId));
        // get descriptions
        retValue.setDescriptions(getAllItemDescriptions(retValue.getId()));
        return retValue;
	}
	
	public Collection<ItemDTOEx> getSubscribedItems(Integer userId) {
		Collection<ItemDTOEx> items = new ArrayList<ItemDTOEx>();

		List<PlanItemDTO> prices = new CustomerPriceBL(userId).getCustomerPrices();
		if (prices == null) {
			return items;
		}
		
        List<PlanItemWS> ws = PlanItemBL.getWS(prices);
        if (ws == null) {
        	return items;
        }

		for (PlanItemWS subPlan : ws) {
			items.add(getItem(userId, subPlan.getItemId()));
		}
		
		return items;
	}

}
