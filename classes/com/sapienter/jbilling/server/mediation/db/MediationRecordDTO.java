/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.sapienter.jbilling.server.mediation.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "mediation_record")
// no cache : it is hardly ever re-read 
public class MediationRecordDTO implements Serializable {

	private String key;
	private Date started;
	private Date finished;
	private int optlock;
	private MediationProcess process;

	// needed by Hibernate
	protected MediationRecordDTO() {
	}
	
	public MediationRecordDTO(String key, Date started, MediationProcess process) {
		this.key = key;
		this.started = started;
		this.process = process;
	}
	
	@Column(name="end_datetime")
	public Date getFinished() {
		return finished;
	}	
	// needed by Hibernate
	public  void setFinished(Date finished) {
		this.finished = finished;
	}

	@Id 
    @Column(name="id_key", nullable=false)
    public String getKey() {
		return key;
	}
	// needed by Hibernate
	protected void setKey(String key) {
		this.key = key;
	}
	
	@Column(name="start_datetime")
	public Date getStarted() {
		return started;
	}
	// needed by Hibernate
	protected void setStarted(Date started) {
		this.started = started;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="mediation_process_id" )
	public MediationProcess getProcess() {
		return process;
	}

	public void setProcess(MediationProcess process) {
		this.process = process;
	}

	@Version
    @Column(name="OPTLOCK")
	public int getOptlock() {
		return optlock;
	}
	protected void setOptlock(int optlock) {
		this.optlock = optlock;
	}
}
