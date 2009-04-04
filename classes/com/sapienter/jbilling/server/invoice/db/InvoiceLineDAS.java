package com.sapienter.jbilling.server.invoice.db;

import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.util.db.AbstractDAS;

/**
 * 
 * @author abimael
 * 
 */
public class InvoiceLineDAS extends AbstractDAS<InvoiceLineDTO> {

	public InvoiceLineDTO create(String description, Float amount,
			Double quantity, Float price, Integer typeId, ItemDTO itemId,
			Integer sourceUserId, Integer isPercentage) {

		InvoiceLineDTO newEntity = new InvoiceLineDTO();
		newEntity.setDescription(description);
		newEntity.setAmount(amount);
		newEntity.setQuantity(quantity);
		newEntity.setPrice(price);
		newEntity.setInvoiceLineType(new InvoiceLineTypeDAS().find(typeId));
		newEntity.setItem(itemId);
		newEntity.setSourceUserId(sourceUserId);
		newEntity.setIsPercentage(isPercentage);
		newEntity.setDeleted(0);
		return save(newEntity);
	}

}
