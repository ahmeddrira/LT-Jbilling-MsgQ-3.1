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
 * Created on May 5, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sapienter.jbilling.common.GatewayBL;

/**
 * @author Emil
 */
public class GatewayServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        GatewayBL bl = null;
        bl = new GatewayBL(request);
        bl.process();
        
        out.print(bl.getResult());
    }
    
}
