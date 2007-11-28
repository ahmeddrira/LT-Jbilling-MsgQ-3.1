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
