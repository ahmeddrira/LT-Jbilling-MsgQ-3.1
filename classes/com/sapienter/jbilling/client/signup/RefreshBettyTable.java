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
 * Created on Dec 31, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sapienter.jbilling.client.signup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Emil
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RefreshBettyTable {
    private Connection conn = null;
    public static void main(String[] args) {
        try {
            RefreshBettyTable toCall = new RefreshBettyTable("signup.properties");
            toCall.refresh();
        } catch (Exception e) {
        }
    }
    
    public RefreshBettyTable(String propertiesFile) 
            throws FileNotFoundException, IOException, 
            ClassNotFoundException, SQLException {
        Properties globalProperties = new Properties();
        globalProperties.load(RefreshBettyTable.class.getResourceAsStream("/jbilling.properties"));
        
        Class.forName(globalProperties.getProperty("driver_class"));
        conn = DriverManager.getConnection(
                globalProperties.getProperty("connection_url"),
                globalProperties.getProperty("connection_username"),
                globalProperties.getProperty("connection_password"));
    }
    
    public void refresh() {
        try {
            Logger log = Logger.getLogger(RefreshBettyTable.class);
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement("select name from jbilling_table");
            ResultSet res = stmt.executeQuery();
            Statement exe = conn.createStatement();
            while (res.next()) {
                String table = res.getString(1);
                if (!table.endsWith("_map")) {
                    PreparedStatement stmt2 = conn.prepareStatement(
                            "select max(id) from " + table);
                    ResultSet res2 = stmt2.executeQuery();
                    int id = 0;
                    if (res2.next()) {
                        id = res2.getInt(1) + 1;
                    }
                    log.debug("Updateing " + table);
                    exe.execute("update jbilling_table set next_id = " + id + 
                            " where name = '" + table + "'");
                }
            }
            res.close();
            stmt.close();
            conn.commit();
            conn.close();
            log.debug("Done");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
