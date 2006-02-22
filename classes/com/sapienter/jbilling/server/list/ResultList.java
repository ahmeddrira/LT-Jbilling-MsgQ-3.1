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
 * Created on 27-Feb-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.list;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.JNDILookup;

/**
 * Makes a JDBC query using the sql string got as a parameter.
 * It has no clue about the sql to be executed, it simply gets
 * the connection from the pool, makes the query and returns the
 * ResultSet.
 * 
 * In this first cut, it uses a CachedRowSet with all the rows in
 * it to send to the client. No paging from the application server
 * is considered. This is ment for list that are small enough to
 * fit in memory in the web server tier.
 * 
 * @author Emil
 */


public class ResultList {
	protected CachedRowSet cachedResults;
	protected Connection conn = null;
    protected Logger log = null;
	
	protected ResultList() {
        log = Logger.getLogger(ResultList.class);
    }
	
	protected void prepareStatement(String SQL) throws SQLException, NamingException {
	    
	    // the default is TYPE_SCROLL_INSENSITIVE and CONCUR_READ_ONLY
	    // which is good
        cachedResults = new CachedRowSet();
        
        cachedResults.setCommand(SQL);
        cachedResults.setFetchDirection(ResultSet.FETCH_UNKNOWN);
        // the BL list class will set the parameters of the query
	}
	
    protected void execute() throws SQLException, NamingException {
        JNDILookup jndi = JNDILookup.getFactory();
        // the connection will be closed by the RowSet as soon as it
        // finished executing the command
        conn = jndi.lookUpDataSource().getConnection();
        
		cachedResults.execute(conn);	    
	}

}
