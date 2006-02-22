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
 * Created on Dec 2, 2004
 *
 */
package com.sapienter.jbilling.server.list;

import java.io.Serializable;

import com.sapienter.jbilling.server.entity.ListFieldDTO;

/**
 * @author Emil
 *
 */
public class PagedListDTO implements Serializable {
    private Integer count = null;
    private Integer keyFieldId = null;
    private Integer pageSize = null;
    private Integer listId = null;
    private String titleKey = null; 
    // now only those searchable go here
    private ListFieldDTO fields[] = null;
    
    public PagedListDTO() {}
    
    public PagedListDTO(PagedListDTO another) {
        count = another.getCount();
        keyFieldId = another.getKeyFieldId();
        pageSize = another.getPageSize();
        listId = another.getListId();
        fields = another.fields;
        titleKey = another.titleKey;
    }
    
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public Integer getKeyFieldId() {
        return keyFieldId;
    }
    public void setKeyFieldId(Integer keyFieldId) {
        this.keyFieldId = keyFieldId;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getListId() {
        return listId;
    }
    public void setListId(Integer listId) {
        this.listId = listId;
    }
    public ListFieldDTO[] getFields() {
        return fields;
    }
    public void setFields(ListFieldDTO[] fields) {
        this.fields = fields;
    }
    public String getTitleKey() {
        return titleKey;
    }
    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }
}
