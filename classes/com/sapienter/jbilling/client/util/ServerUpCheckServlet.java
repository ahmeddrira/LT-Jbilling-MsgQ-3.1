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
package com.sapienter.jbilling.client.util;

import java.io.IOException;

import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sapienter.jbilling.server.user.EntityBL;

/**
 * Simply server meant to be called by a monitoring service.
 * It will just find entity 1 and return PASSED, or something else if there
 * is a problem.
 * A PASSED return means that the basic health of jbilling is good. The classes
 * are loaded, the database is running and the app server JNDI service is working.
 * @author Emil
 *
 */
public class ServerUpCheckServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doCheck(request,response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doCheck(request,response);
    }
    
    private void doCheck(HttpServletRequest request, HttpServletResponse response) {
        
        try {
            ServletOutputStream output = response.getOutputStream();
            try {
                EntityBL entity = new EntityBL(new Integer(1));
                output.print("PASSED");
            } catch (FinderException e) {
                output.print("ERROR: Can not find entity 1." + e.getMessage());
            } catch (NamingException e) {
                output.print("ERROR: JNDI problem." + e.getMessage());
            } catch (Throwable e) {
                output.print("ERROR: Exception." + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
