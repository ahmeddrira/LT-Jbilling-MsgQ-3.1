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

package com.sapienter.jbilling.client.list;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.server.list.ListDTO;

/**
 * This is the base for custom tag that will be used to list data.
 * The class that extends this one has to implement the doStartTag and
 * get the CachedRowSet from the session bean.
 * @author emilc
 *
 */

public abstract class ListTagBase extends TagSupport {

    public static final int METHOD_JDBC = 1;
    public static final int METHOD_EJB = 2;

    protected CachedRowSet queryResults = null;
    protected ListDTO queryDtoResults = null;
    protected int dtoIndex = 0;
    protected int queryMethod = METHOD_JDBC;
    protected Logger log = null;
    
    public int doAfterBody() throws JspException {
        int retValue = SKIP_BODY;
        // now verify that there's another row to render
        try {
            if (queryMethod == METHOD_JDBC) {
                if (queryResults.next()) {
                    retValue = EVAL_BODY_AGAIN;
                }
            } else {
                dtoIndex++;
                if (dtoIndex < queryDtoResults.getLines().size()) {
                    retValue = EVAL_BODY_AGAIN;
                }
            }
        } catch (Exception e) {
            log.error("Exception at RowSet tag", e);
            throw new JspException("Web error" + e.getMessage());
        }

        return retValue;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
    
    public CachedRowSet getQueryResults() {
        return queryResults;
    }

    /**
     * @return
     */
    public int getDtoIndex() {
        return dtoIndex;
    }

    /**
     * @return
     */
    public ListDTO getQueryDtoResults() {
        return queryDtoResults;
    }

    /**
     * @return
     */
    public int getQueryMethod() {
        return queryMethod;
    }

    /**
     * @param i
     */
    public void setDtoIndex(int i) {
        dtoIndex = i;
    }

    /**
     * @param vector
     */
    public void setQueryDtoResults(ListDTO vector) {
        queryDtoResults = vector;
    }

    /**
     * @param i
     */
    public void setQueryMethod(int i) {
        queryMethod = i;
    }

}
