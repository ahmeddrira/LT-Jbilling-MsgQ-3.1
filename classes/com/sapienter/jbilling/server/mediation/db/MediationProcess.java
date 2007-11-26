/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/
package com.sapienter.jbilling.server.mediation.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@TableGenerator(
        name="mediation_process_GEN",
        table="jbilling_table",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue="mediation_process",
        allocationSize=10
        )
@Table(name = "mediation_process")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MediationProcess implements Serializable {
    //private static final Logger LOG = Logger.getLogger(MediationProcess.class);
   
    @Id @GeneratedValue(strategy=GenerationType.TABLE, generator="mediation_process_GEN")
    private Integer id;
   
    @Column(name = "start_datetime")
    private Date startDatetime;
    
    @Column(name = "end_datetime")
    private Date endDatetime;

    @Column(name = "orders_affected")
    private Integer ordersAffected;
    
    @ManyToOne
    @JoinColumn(name="configuration_id")
    private MediationConfiguration configuration;

    @OneToMany
    @JoinColumn (name = "mediation_process_id") 
    public Collection<MediationOrderMap> orderMap;
    
    @Version
    @Column(name="OPTLOCK")
    private Integer versionNum;

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrdersAffected() {
        return ordersAffected;
    }

    public void setOrdersAffected(Integer ordersAffected) {
        this.ordersAffected = ordersAffected;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public Collection<MediationOrderMap> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Collection<MediationOrderMap> orderMap) {
        this.orderMap = orderMap;
    }

    public MediationConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(MediationConfiguration configuration) {
        this.configuration = configuration;
    }

}
