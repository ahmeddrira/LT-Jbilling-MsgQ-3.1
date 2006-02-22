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
package com.sapienter.jbilling.client.list;

import java.util.Hashtable;
import java.util.Vector;

import com.sapienter.jbilling.server.list.PagedListDTO;

/**
 * @author Emil
 * This is a helper class that goes in the session and holds the info
 * needed to ask for the next/previous page
 * The original object is created by the list tag when called to setup.
 */
public class PagedList extends PagedListDTO {
    private Hashtable parameters = null;
    private Vector pageFrom = null;
    private Boolean direction = null;
    // works as an index, starting with 0.
    // It is always displayed + 1.
    private Integer currentPage = null;
    private String legacyName = null;
    private Boolean doSearch = null;
    private String searchStart = null;
    private String searchEnd = null;
    private Integer searchFieldId = null;

    public Integer getPageNumber() {
        return new Integer(currentPage.intValue() + 1);
    }
    
    public String getLegacyName() {
        return legacyName;
    }
    public void setLegacyName(String legacyName) {
        this.legacyName = legacyName;
    }
    public PagedList(PagedListDTO dto) {
        super(dto);
        pageFrom = new Vector();
        direction = new Boolean(false);
        currentPage = new Integer(0);
    }
    public Long getNumberOfPages() {
        if (getCount() != null) {
            long total = getCount().longValue() / 
                getPageSize().longValue();
            total++;
            return new Long(total);
        } else {
            return null;
        }
    }
    
    public void setNumberOfPages(Long x) {
        
    }
    
    public Integer getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    public Boolean getDirection() {
        return direction;
    }
    public void setDirection(Boolean direction) {
        this.direction = direction;
    }
    public Vector getPageFrom() {
        return pageFrom;
    }
    public void setPageFrom(Vector pageFrom) {
        this.pageFrom = pageFrom;
    }
    public Hashtable getParameters() {
        return parameters;
    }
    public void setParameters(Hashtable parameters) {
        this.parameters = parameters;
    }
    public Boolean getDoSearch() {
        return doSearch;
    }
    public void setDoSearch(Boolean doSearch) {
        this.doSearch = doSearch;
    }
    public String getSearchEnd() {
        return searchEnd;
    }
    public void setSearchEnd(String searchEnd) {
        this.searchEnd = searchEnd;
    }
    public String getSearchStart() {
        return searchStart;
    }
    public void setSearchStart(String searchStart) {
        this.searchStart = searchStart;
    }
    public Integer getSearchFieldId() {
        return searchFieldId;
    }
    public void setSearchFieldId(Integer searchFieldId) {
        this.searchFieldId = searchFieldId;
    }
}
