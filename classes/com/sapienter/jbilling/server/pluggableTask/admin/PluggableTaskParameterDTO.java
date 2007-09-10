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

/*
 * Created on 14-Apr-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.pluggableTask.admin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@TableGenerator(
        name="pluggable_task_parameter_GEN",
        table="jbilling_table",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue="pluggable_task_parameter",
        allocationSize=10
        )
@Table(name = "pluggable_task_parameter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PluggableTaskParameterDTO implements Serializable {

    private static final Logger LOG = Logger.getLogger(PluggableTaskParameterDTO.class);
    
    public static final int INT = 1;
    public static final int STR = 2;
    public static final int FLO = 3;

    // MAPPED COLUMS
    
    @Id @GeneratedValue(strategy=GenerationType.TABLE, generator="pluggable_task_parameter_GEN")
    private Integer id;
    
    @Column(name = "name")
    private String name;

    @Column(name = "int_value")
    private Integer intValue;
 
    @Column(name = "str_value")
    private String strValue;
    
    @Column(name = "float_value")
    private Float floatValue;

    @ManyToOne
    @JoinColumn(name="task_id")
    private PluggableTaskDTO task;
    
    @Version
    @Column(name="OPTLOCK")
    private Integer versionNum;


    // INTERNAL FIELDS
    @Transient
    private Integer type = null; // this indicates the data type of the value
    @Transient
    private String value = null;

    public Float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public PluggableTaskDTO getTask() {
        return task;
    }

    public void setTask(PluggableTaskDTO task) {
        this.task = task;
    }

    public String toString() {
      StringBuffer str = new StringBuffer("{");
        str.append("-" + this.getClass().getName() + "-");

      str.append("id=" + getId() + " " + "name=" + getName() + " " + "intValue=" + 
                getIntValue() + " " + "strValue=" + getStrValue() + " " + "floatValue=" +
                getFloatValue());
      str.append('}');

      return(str.toString());
     }
    
    @PostLoad
    @PostUpdate
    public void populateValue() {
        if (getIntValue() != null) {
            type = new Integer(INT);
            value = String.valueOf(getIntValue());
        } else if (getStrValue() != null) {
            type = new Integer(STR);
            value = getStrValue();
        } else if (getFloatValue() != null) {
            type = new Integer(FLO);
            value = String.valueOf(getFloatValue());
        } else {
            // the value of this parameer is null
            // we default the type to String
            type = new Integer(STR);
        }
    }
    
    public void expandValue() throws NumberFormatException {
        if (type == null) return;
        
        switch(type.intValue()) {
        case INT:
            setIntValue(Integer.valueOf(value));
            setStrValue(null);
            setFloatValue(null);  
        break;
        case STR:
            setIntValue(null);
            setStrValue(value);
            setFloatValue(null);  
        break;
        case FLO:
            setIntValue(null);
            setStrValue(null);
            setFloatValue(Float.valueOf(value));  
        break;
        }
    }

    public Integer getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
    protected int getVersionNum() { return versionNum; }
}
