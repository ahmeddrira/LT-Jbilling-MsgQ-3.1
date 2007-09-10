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

package com.sapienter.jbilling.server.pluggableTask.admin;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@TableGenerator(
        name="pluggable_task_GEN",
        table="jbilling_table",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue="pluggable_task",
        allocationSize=10
        )
@Table(name = "pluggable_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PluggableTaskDTO implements java.io.Serializable {
    private static final Logger LOG = Logger.getLogger(PluggableTaskDTO.class);
    //  this is in synch with the DB (pluggable task type)
    public static final Integer TYPE_EMAIL = new Integer(9);
   
    @Id @GeneratedValue(strategy=GenerationType.TABLE, generator="pluggable_task_GEN")
    private Integer id;
   
    @Column(name = "entity_id")
    private Integer entityId;
    
    @Column(name = "processing_order")
    private Integer processingOrder;
    
    @ManyToOne
    @JoinColumn(name="type_id")
    private PluggableTaskTypeDTO type;
    
    @OneToMany(mappedBy="task", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Collection<PluggableTaskParameterDTO> parameters;
    
    @Version
    @Column(name="OPTLOCK")
    private Integer versionNum;
    
    public PluggableTaskDTO() {
        type = new PluggableTaskTypeDTO();
    }

 
   public String toString() {
	  StringBuffer str = new StringBuffer("{");
      str.append("-" + this.getClass().getName() + "-");

	  str.append("id=" + getId() + " " + "entityId=" + getEntityId() + " " + "processingOrder=" + 
              getProcessingOrder() + " " + "type=" + getType());
	  str.append('}');

	  return(str.toString());
   }


    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProcessingOrder() {
        return processingOrder;
    }

    public void setProcessingOrder(Integer processingOrder) {
        this.processingOrder = processingOrder;
    }

   public int hashCode(){
	  int result = 17;
      result = 37*result + ((this.id != null) ? this.id.hashCode() : 0);

      result = 37*result + ((this.entityId != null) ? this.entityId.hashCode() : 0);

      result = 37*result + ((this.processingOrder != null) ? this.processingOrder.hashCode() : 0);

	  return result;
   }


   public Collection<PluggableTaskParameterDTO> getParameters() {
        return parameters;
    }

    public void setParameters(Collection<PluggableTaskParameterDTO> parameters) {
        this.parameters = parameters;
    }

    public PluggableTaskTypeDTO getType() {
        return type;
    }

    public void setType(PluggableTaskTypeDTO type) {
        this.type = type;
    }

    protected int getVersionNum() { return versionNum; }

}
