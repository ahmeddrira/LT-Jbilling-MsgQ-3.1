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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "pluggable_task_type_category")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class PluggableTaskTypeCategoryDTO implements Serializable {

    private static final Logger LOG = Logger.getLogger(PluggableTaskTypeCategoryDTO.class);
    
    @Id
    public Integer Id;

    @Column(name = "interface_name")
    private String interfaceName;

    @Column(name="description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
    
    public String toString() {
        StringBuffer str = new StringBuffer("{");
        str.append("-" + this.getClass().getName() + "-");
        str.append("id=" + getId() + " " + "interfaceName=" + getInterfaceName() + " " + 
                " " + "description=" + getDescription());
        str.append('}');

        return(str.toString());

      }
}
