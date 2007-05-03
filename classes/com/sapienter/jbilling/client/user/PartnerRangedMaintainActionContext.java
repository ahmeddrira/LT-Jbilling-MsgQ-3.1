package com.sapienter.jbilling.client.user;

import com.sapienter.jbilling.server.entity.PartnerRangeDTO;

/**
 * Intentionally package local. This is helper that used only by
 * PartnerRangedMaintainAction, just can not be declared as inner class because
 * used in the template binding.
 * 
 * NOTE: It is NOT intended for any use outside of PartnerRangedMaintainAction
 * scope.
 */
class PartnerRangedMaintainActionContext {
	private final PartnerRangeDTO[] myData;

	public PartnerRangedMaintainActionContext(PartnerRangeDTO[] ranges) {
		myData = ranges;
	}

	/**
	 * Generally it is bad idea to return not guarded array. Intentionally done
	 * this way, but made package local.
	 */
	PartnerRangeDTO[] getData() {
		return myData;
	}

}
