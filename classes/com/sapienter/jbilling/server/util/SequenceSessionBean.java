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

package com.sapienter.jbilling.server.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.TableEntityLocalHome;

/**
 *
 * Primary key generator, adapted version of book example.
 *
 * @author emilc
 * @ejb:bean name="com/sapienter/jbilling/server/util/SequenceSession"
 *           display-name="A stateless bean for primary key generation"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="local"
 *           local-jndi-name="com/sapienter/jbilling/server/util/SequenceSessionLocal"
 *           jndi-name="com/sapienter/jbilling/server/util/SequenceSession"
 * 
 * @ejb.env-entry name="retryCount"
 *                type="java.lang.Integer"
 *                value="5"
 *  
 * @ejb.env-entry name="blockSize"
 *                type="java.lang.Integer"
 *                value="10"
 *
 **/

public class SequenceSessionBean implements javax.ejb.SessionBean {

    private Hashtable _entries = new Hashtable();
    JNDILookup EJBFactory;
    private int _blockSize;
    private int _retryCount;
    private TableEntityLocalHome _sequenceHome;
    Logger log = null;

    public void ejbActivate() {
        log.debug("getting activated");
    }

    /**
    * @ejb:create-method view-type="local"
    */
    public void ejbCreate() {
    }
    public void ejbPassivate() {
        log.debug("getting passivated");
    }
    public void ejbRemove() {
        log.debug("getting removed");
    }

    /**
    * @ejb:interface-method view-type="local"
    * @ejb.transaction type="Required"    
    */
    public int getNextSequenceNumber(String name) 
    		throws SessionInternalError {

    	try {
			if (_entries == null) {
				log.debug("The hashtable was null when getting a new key");
				_entries = new Hashtable();
			}
			Entry entry = (Entry) _entries.get(name);

			if (entry == null) {
			    // add an entry to the sequence table
			    entry = new Entry();
			    try {
			        entry.sequence = _sequenceHome.findByTableName(name);
			        entry.current = entry.sequence.getValueAndIncrementingBy(_blockSize);
                    entry.last = entry.sequence.getIndex();
			        log.debug("Init: " + name + " got last_id = " + entry.last +
                            " current = " + entry.current);
			    } catch (javax.ejb.FinderException e) {
			        log.fatal("The table " + name + " is missing in jbilling_tables");
			        throw new javax.ejb.EJBException(e);
			    }
			    _entries.put(name, entry);
			} else {
			    log.debug("table " + name + " current " + entry.current + 
                        " last: " + entry.last);
                entry.current++;
			    if (entry.current == entry.last) {
                    log.debug("Looking for new block ");
			        for (int retry = 0; true; retry++) {
			            try {
                            //log.debug("Fetching, retry " + retry);
                            entry.current = entry.sequence.
                                    getValueAndIncrementingBy(_blockSize);
                            entry.last = entry.sequence.getIndex();
			                break;
			            } catch (javax.ejb.TransactionRolledbackLocalException e) {
			                if (retry < _retryCount) {
			                    // we hit a concurrency exception, so try again...
			                    log.warn(
			                        "Concurrency exception creating primary"
			                            + " key. Retrying.");
			                    continue;
			                } else {
			                    log.fatal("Too many retries .. aborting creation", e);
			                    // we tried too many times, so fail...
			                    throw new javax.ejb.EJBException(e);
			                }
			            }
			        }
			        log.debug("Got new block for " + name + " Now id = " + entry.current);
			    } 
			}
			
            int validated = validateResult(name, entry.current);
            if (validated != entry.current) {
                Logger.getLogger(SequenceSessionBean.class).debug(
                        "Auto recovering from wrong id generation");
                entry.sequence.setIndex(validated + 1);
                _entries.remove(name); // this will force a new read;
                entry = null; // garbage remove this piece of garbage
            }
            log.debug("Returning PK for " + name + " " + validated);
			return validated;
		} catch (Exception e) {
			Logger.getLogger(SequenceSessionBean.class).error("Exception generating " +
					"a key. Table = " + name, e);
			throw new SessionInternalError(e);
		}

    }
    
    private int validateResult(String table, int ret) {
        int retValue = ret;
        try {
            // by default connections are in auto-commit
            Connection conn = EJBFactory.lookUpDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select id from " + 
                    table + " where id = ?");
            stmt.setInt(1, ret);
            ResultSet set = stmt.executeQuery();
            if (set.next()) {
                set.close();
                stmt.close();
                Logger log = Logger.getLogger(SequenceSessionBean.class);
                log.error("Found row id " + ret + " in table " + table + 
                        " which I was about to return");
                
                // this is a problem, this row should not be there!
                stmt = conn.prepareStatement("select max(id) from " + table);
                set = stmt.executeQuery();
                set.next();
                retValue = set.getInt(1) + 1;
                set.close();
                stmt.close();
            }
            conn.close();
        } catch (Exception e) {
            Logger.getLogger(SequenceSessionBean.class).error("Exception in " +
                    "validation ", e);
        }
        
        return retValue;
    }

    public void setSessionContext(javax.ejb.SessionContext sessionContext) {
        try {
            javax.naming.Context namingContext =
                new javax.naming.InitialContext();
            _blockSize =
                ((Integer) namingContext.lookup("java:comp/env/blockSize"))
                    .intValue();
            _retryCount =
                ((Integer) namingContext.lookup("java:comp/env/retryCount"))
                    .intValue();

            EJBFactory = JNDILookup.getFactory(false);
            _sequenceHome =
                (TableEntityLocalHome) EJBFactory.lookUpLocalHome(
                    TableEntityLocalHome.class,
                    TableEntityLocalHome.JNDI_NAME);
        } catch (javax.naming.NamingException e) {
            throw new javax.ejb.EJBException(e);
        }
        log = Logger.getLogger(SequenceSessionBean.class);
        log.debug("Getting session context");
    }
}
