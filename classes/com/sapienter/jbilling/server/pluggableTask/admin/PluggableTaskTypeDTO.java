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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocal;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocalHome;
import com.sapienter.jbilling.server.util.Constants;

@Entity
@Table(name = "pluggable_task_type")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class PluggableTaskTypeDTO implements Serializable {

    private static final Logger LOG = Logger.getLogger(PluggableTaskTypeDTO.class);
    
    @Id
    private Integer Id;

    @Column(name = "class_name")
    private String className;

    @Column(name="min_parameters")
    private Integer minParameters;

    @ManyToOne
    @JoinColumn(name="category_id")
    private PluggableTaskTypeCategoryDTO category;

    // Custom field accessors --------------------------------------------------
    private DescriptionEntityLocal getDescriptionObject(
            Integer language,
            String column) {
            try {

                JNDILookup EJBFactory = JNDILookup.getFactory(false);
                DescriptionEntityLocalHome descriptionHome =
                    (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                        DescriptionEntityLocalHome.class,
                        DescriptionEntityLocalHome.JNDI_NAME);

                return descriptionHome.findIt(
                    Constants.TABLE_PLUGGABLE_TASK_TYPE,
                    getId(),
                    column,
                    language);
            } catch (Exception e) {
                LOG.warn("Exception while looking for pluggable_task_type inter. field", e);
                return null;
            }

        }

    /**
     * @ejb:interface-method view-type="local"
     */
    public String getTitle(Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language, "title");
        if (desc == null) {
            return "Title not set for this rule";
        } else {
            return desc.getContent();
        }
    }
    public void setTitle(String title, Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language, "title");
        if (desc == null) {
            LOG.error("Can't update a non existing record");
        } else {
            desc.setContent(title);
        }
    }


    /**
     * @ejb:interface-method view-type="local"
     */
    public String getDescription(Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language, "description");
        if (desc == null) {
            return "Description not set for this rule";
        } else {
            return desc.getContent();
        }
    }
    public void setDescription(String description, Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language, "description");
        if (desc == null) {
            LOG.error("Can't update a non existing record");
        } else {
            desc.setContent(description);
        }
    }
    
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public Integer getId() {
        return Id;
    }
    public void setId(Integer id) {
        Id = id;
    }
    public Integer getMinParameters() {
        return minParameters;
    }
    public void setMinParameters(Integer minParameters) {
        this.minParameters = minParameters;
    }
    public void setCategory(PluggableTaskTypeCategoryDTO category) {
        this.category = category;
    }

    public PluggableTaskTypeCategoryDTO getCategory() {
        return category;
    }
    
    public String toString() {
      StringBuffer str = new StringBuffer("{");
      str.append("-" + this.getClass().getName() + "-");
      str.append("id=" + getId() + " " + "className=" + getClassName() + " " + 
              "minParameters=" + getMinParameters() + " " + "category=" + getCategory());
      str.append('}');

      return(str.toString());

    }

}
