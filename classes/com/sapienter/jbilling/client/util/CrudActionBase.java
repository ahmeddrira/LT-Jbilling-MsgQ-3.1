package com.sapienter.jbilling.client.util;

import java.rmi.RemoteException;

import com.sapienter.jbilling.common.SessionInternalError;

public abstract class CrudActionBase<DTO> extends CrudAction {
	private final String myLogFriendlyActionType;
	
	protected abstract DTO doEditFormToDTO() throws RemoteException;
	protected abstract ForwardAndMessage doSetup() throws RemoteException;
	protected abstract ForwardAndMessage doCreate(DTO dto) throws RemoteException;
	protected abstract ForwardAndMessage doUpdate(DTO dto) throws RemoteException;
	protected abstract ForwardAndMessage doDelete() throws RemoteException;
	protected abstract void resetCachedList();
	
	public CrudActionBase(String formName, String logFriendlyActionType){
		myLogFriendlyActionType = logFriendlyActionType;
		setFormName(formName);
	}
	
	@Override
	public final void setup() {
		ForwardAndMessage result; 
		try {
			result = doSetup();
		} catch (RemoteException e) {
			throw wrapError("setup", e);
		}
		addGlobalMessage(result.getMessage());
		setForward(result.getForward());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final void create(Object dtoHolder) {
		ForwardAndMessage result;
		try {
			result = doCreate((DTO)dtoHolder);
		} catch (RemoteException e) {
			throw wrapError("create", e);
		}
		addGlobalMessage(result.getMessage());
		setForward(result.getForward());
		resetCaches();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final String update(Object dtoHolder) {
		ForwardAndMessage result;
		try {
			result = doUpdate((DTO)dtoHolder);
		} catch (RemoteException e){
			throw wrapError("update", e);
		}
		setForward(result.getForward());
		resetCaches();
		return result.getMessage();
	}
	
	@Override
	public final String delete() {
		ForwardAndMessage result;
		try {
			result = doDelete();
		} catch (RemoteException e) {
			throw wrapError("delete", e);
		}
		setForward(result.getForward());
		return result.getMessage();
	}
	
	@Override
	public final Object editFormToDTO() {
		try {
			return doEditFormToDTO();
		} catch (RemoteException e){
			throw wrapError("validate", e);
		}	
	}
	
	@Override
	public void reset() {
		//most of the actions do nothing here
	}
	
	@Override
	public boolean otherAction(String action) {
		//most of the implementors do not want to accept other actions
		return false;
	}
	
	@Override
	public void postDelete() {
		super.postDelete();
		resetCaches();
	}
	
	protected void resetCaches(){
		removeFormFromSession();
		resetCachedList();
	}
	
	protected SessionInternalError wrapError(String activity, RemoteException e){
		return new SessionInternalError(
				myLogFriendlyActionType + " " + activity + " failed: "
					+ e.getMessage());
	}
	
	protected final Integer getIntegerFieldValue(String field){
		return getFormHelper().getIntegerFieldValue(myForm, field);
	}
	
	protected static final class ForwardAndMessage {
		private final String myForward;
		private final String myMessage;
		
		public ForwardAndMessage(String forward){
			this(forward, null);
		}

		public ForwardAndMessage(String forward, String message){
			myForward = forward;
			myMessage = message;
		}
		
		public String getForward() {
			return myForward;
		}
		
		public String getMessage() {
			return myMessage;
		}
	}
}
