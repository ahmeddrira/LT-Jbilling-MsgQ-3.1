package com.sapienter.jbilling.server.user;

import java.util.Date;
import java.io.Serializable;

/**
 * @jboss-net.xml-schema urn="sapienter:UserTransitionResponseWS"
 */
public class UserTransitionResponseWS implements Serializable {
	private Integer id;
	private Integer userId;
	private Date transitionDate;
	private Integer fromStatusId;
	private Integer toStatusId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getTransitionDate() {
		return transitionDate;
	}
	public void setTransitionDate(Date transitionDate) {
		this.transitionDate = transitionDate;
	}
	public Integer getFromStatusId() {
		return fromStatusId;
	}
	public void setFromStatusId(Integer fromStatusId) {
		this.fromStatusId = fromStatusId;
	}
	public Integer getToStatusId() {
		return toStatusId;
	}
	public void setToStatusId(Integer toStatusId) {
		this.toStatusId = toStatusId;
	}
	
	public String toString() {
		return "id = " + getId() + " user_id = " + getUserId() +
                " from_status_id = " + getFromStatusId() + " to_status_id = " + getToStatusId() +
                " transition_date = " + getTransitionDate().toString();
	}

}
