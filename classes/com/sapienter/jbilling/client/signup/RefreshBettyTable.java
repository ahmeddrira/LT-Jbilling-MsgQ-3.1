/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;

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
    
    /*
     * The name of the file has to start with '/'. 
     * Example: "/jbilling.properties"
     * Otherwise it won't find it in the classpath 
     */
    public RefreshBettyTable(String propertiesFile) 
            throws FileNotFoundException, IOException, 
            ClassNotFoundException, SQLException, NamingException {
        if (propertiesFile != null) {
            Properties globalProperties = new Properties();
            globalProperties.load(RefreshBettyTable.class.getResourceAsStream(
                    propertiesFile));
            
            Class.forName(globalProperties.getProperty("driver_class"));
            conn = DriverManager.getConnection(
                    globalProperties.getProperty("connection_url"),
                    globalProperties.getProperty("connection_username"),
                    globalProperties.getProperty("connection_password"));
        } else {
            JNDILookup jndi = JNDILookup.getFactory();
            // the connection will be closed by the RowSet as soon as it
            // finished executing the command
            conn = jndi.lookUpDataSource().getConnection();
        }
        
        Logger.getLogger(this.toString()).debug("Refreshing tables with " +
                "connection = " + conn);
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
