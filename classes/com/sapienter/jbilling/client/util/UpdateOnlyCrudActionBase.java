package com.sapienter.jbilling.client.util;

import java.rmi.RemoteException;

public abstract class UpdateOnlyCrudActionBase<DTO> extends CrudActionBase<DTO> {
	private ForwardAndMessage myForwardEdit;
	
	public UpdateOnlyCrudActionBase(String formName, String logFriendlyActionType, String forwardEdit) {
		super(formName, logFriendlyActionType);
		myForwardEdit = new ForwardAndMessage(forwardEdit);
	}
	
	protected final ForwardAndMessage getForwardEdit(){
		return myForwardEdit;
	}
	
	protected final ForwardAndMessage getForwardEdit(String messageKey){
		return new ForwardAndMessage(myForwardEdit.getForward(), messageKey);
	}
	
	@Override
	protected final void resetCaches() {
		// nothing to reset, the form is singleton and should not be reset
	}

	@Override
	protected final void resetCachedList() {
		// not in list
	}

	@Override
	protected final ForwardAndMessage doCreate(DTO dto) throws RemoteException {
		throw wrapError("create", new RemoteException(
				"Can not create -- it is always here"));
	}

	@Override
	protected final ForwardAndMessage doDelete() throws RemoteException {
		throw wrapError("delete", new RemoteException(
				"Can not delete -- it is always here"));
	}
	
}
