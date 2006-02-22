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
 * Created on Jan 22, 2005
 *
 */
package com.sapienter.jbilling.client.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.PaymentSession;
import com.sapienter.jbilling.interfaces.PaymentSessionHome;

/**
 * @author Emil
 *
 */
public class ExternalCallbackServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {
        Logger log = Logger.getLogger(ExternalCallbackServlet.class);
        try {
            log.debug("callback received");
            
            if (request.getParameter("caller") == null ||
                    !request.getParameter("caller").equals("paypal")) {
                log.debug("caller not supported");
                return;
            }
            // go over the parameters, making my string for the validation
            // call to paypal
            String validationStr = "cmd=_notify-validate";
            Enumeration parameters = request.getParameterNames();
            while (parameters.hasMoreElements()) {
                String parameter = (String) parameters.nextElement();
                String value = request.getParameter(parameter);
                log.debug("parameter : " + parameter + 
                        " value : " + value);
                validationStr = validationStr + "&" + parameter + "=" + 
                    URLEncoder.encode(value);
            }
            
            log.debug("About to call paypal for validation.  Request" + validationStr);
            URL u = new URL("https://www.paypal.com/cgi-bin/webscr");
            URLConnection uc = u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            PrintWriter pw = new PrintWriter(uc.getOutputStream());
            pw.println(validationStr);
            pw.close();
    
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(uc.getInputStream()));
            String res = in.readLine();
            in.close();
    
            //check notification validation
            log.debug("Validation result is " + res);
            if(res.equals("VERIFIED")) {
                log.debug("ok");
                boolean ok = true;
                String invoiceNumber = request.getParameter("invoice");
                String paymentStatus = request.getParameter("payment_status");
                String paymentAmount = request.getParameter("mc_gross");
                String paymentCurrency = request.getParameter("mc_currency");
                String receiverEmail = request.getParameter("receiver_email");
                
                if (paymentStatus == null || !paymentStatus.equalsIgnoreCase(
                        "completed")) {
                    ok = false;
                    log.debug("payment status is " + paymentStatus + " Rejecting");
                } else { 
                    try {
                    
                        JNDILookup EJBFactory = JNDILookup.getFactory(false);            
                        PaymentSessionHome paymentHome =
                                (PaymentSessionHome) EJBFactory.lookUpHome(
                                PaymentSessionHome.class,
                                PaymentSessionHome.JNDI_NAME);
        
                        PaymentSession paymentSession = paymentHome.create();
                        Integer invoiceId = Integer.valueOf(invoiceNumber);
                        Float amount = Float.valueOf(paymentAmount);
                        Boolean result = paymentSession.processPaypalPayment(
                                invoiceId, receiverEmail, amount, paymentCurrency);
                        
                        log.debug("Finished callback with result " + result);
                    } catch (Exception e) {
                        log.error("Exception processing a paypal callback ", e);
                    }
                   
                }
            }
            else if(res.equals("INVALID")) {
                log.debug("invalid");
            }
            else {
                log.debug("error");
            }
            log.debug("done callback");
        } catch (Exception e) {
            log.error("Error processing external callback", e);
        }
    }
}
