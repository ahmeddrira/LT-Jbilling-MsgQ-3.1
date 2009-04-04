/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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
package com.sapienter.jbilling.server.payment.tasks;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sapienter.jbilling.server.payment.PaymentAuthorizationBL;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.payment.db.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.payment.db.PaymentResultDAS;
import com.sapienter.jbilling.server.pluggableTask.PaymentTask;
import com.sapienter.jbilling.server.pluggableTask.PaymentTaskWithTimeout;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.ContactDTOEx;
import com.sapienter.jbilling.server.user.contact.db.ContactFieldDTO;
import com.sapienter.jbilling.server.util.Constants;

public class PaymentAuthorizeNetCIMTask extends PaymentTaskWithTimeout implements PaymentTask {

    // Required parameters
    private static final String PARAMETER_NAME = "login";

    private static final String PARAMETER_KEY = "transaction_key";

    private static final String PARAMETER_CUSTOMER_PROFILE_ID = "ccf_profile_id";

    private static final String PARAMETER_CUSTOMER_PAYMENT_PROFILE_ID = "ccf_payment_id";

    // Optional parameters
    private static final String PARAMETER_TEST_MODE = "test"; // true or false

    // Authorize.Net Web Service Resources
    private static final String AUTHNET_XML_TEST_URL = "https://apitest.authorize.net/xml/v1/request.api";

    private static final String AUTHNET_XML_PROD_URL = "https://api.authorize.net/xml/v1/request.api";

    private static final String AUTHNET_XML_NAMESPACE = "AnetApi/xml/v1/schema/AnetApiSchema.xsd";

    private static final Logger LOG = Logger.getLogger(PaymentAuthorizeNetCIMTask.class);

    /**
     * Process jbilling payment
     */
    public boolean process(PaymentDTOEx paymentInfo) throws PluggableTaskException {

        try {

            ContactBL contact = new ContactBL();
            contact.set(paymentInfo.getUserId());
            String XML = getCustomerProfileTransactionRequest(paymentInfo,
                    "profileTransAuthCapture", null);
            String HTTPResponse = sendViaXML(XML, paymentInfo);
            PaymentAuthorizationDTO paymentDTO = parseResponse(HTTPResponse);

            if (paymentDTO.getCode1().equals("1")) {

                paymentInfo.setPaymentResult(new PaymentResultDAS().find(Constants.RESULT_OK));
                paymentInfo.setAuthorization(paymentDTO);
                PaymentAuthorizationBL bl = new PaymentAuthorizationBL();
                bl.create(paymentDTO, paymentInfo.getId());
                return false;
            } else {

                paymentInfo.setPaymentResult(new PaymentResultDAS().find(Constants.RESULT_FAIL));
                paymentInfo.setAuthorization(paymentDTO);
                PaymentAuthorizationBL bl = new PaymentAuthorizationBL();
                bl.create(paymentDTO, paymentInfo.getId());
                return false;
            }
        } catch (Exception e) {

            LOG.error(e);
            paymentInfo.setPaymentResult(new PaymentResultDAS().find(Constants.RESULT_UNAVAILABLE));
            return true;
        }
    }

    public boolean preAuth(PaymentDTOEx paymentInfo) throws PluggableTaskException {

        try {
            ContactBL contact = new ContactBL();
            contact.set(paymentInfo.getUserId());
            String XML = getCustomerProfileTransactionRequest(paymentInfo, "profileTransAuthOnly",
                    null);
            String HTTPResponse = sendViaXML(XML, paymentInfo);
            PaymentAuthorizationDTO paymentDTO = parseResponse(HTTPResponse);

            if (paymentDTO.getCode1().equals("1")) {

                paymentInfo.setPaymentResult(new PaymentResultDAS().find(Constants.RESULT_OK));
                paymentInfo.setAuthorization(paymentDTO);
                PaymentAuthorizationBL bl = new PaymentAuthorizationBL();
                bl.create(paymentDTO, paymentInfo.getId());
                return false;
            } else {

                paymentInfo.setPaymentResult(new PaymentResultDAS().find(Constants.RESULT_FAIL));
                paymentInfo.setAuthorization(paymentDTO);
                PaymentAuthorizationBL bl = new PaymentAuthorizationBL();
                bl.create(paymentDTO, paymentInfo.getId());
                return false;
            }
        } catch (Exception e) {
            LOG.error(e);
            paymentInfo.setPaymentResult(new PaymentResultDAS().find(Constants.RESULT_UNAVAILABLE));
            return true;
        }
    }

    public boolean confirmPreAuth(PaymentAuthorizationDTO paymentAuthDTO, PaymentDTOEx paymentInfo)
            throws PluggableTaskException {

        try {
            ContactBL contact = new ContactBL();
            contact.set(paymentInfo.getUserId());
            String XML = getCustomerProfileTransactionRequest(paymentInfo,
                    "profileTransCaptureOnly", paymentAuthDTO.getApprovalCode());
            String HTTPResponse = sendViaXML(XML, paymentInfo);
            PaymentAuthorizationDTO paymentDTO = parseResponse(HTTPResponse);

            if (paymentDTO.getCode1().equals("1")) {

                paymentInfo.setPaymentResult(new PaymentResultDAS().find(Constants.RESULT_OK));
                paymentInfo.setAuthorization(paymentDTO);
                PaymentAuthorizationBL bl = new PaymentAuthorizationBL();
                bl.create(paymentDTO, paymentInfo.getId());
                return false;
            } else {

                paymentInfo.setPaymentResult(new PaymentResultDAS().find(Constants.RESULT_FAIL));
                paymentInfo.setAuthorization(paymentDTO);
                PaymentAuthorizationBL bl = new PaymentAuthorizationBL();
                bl.create(paymentDTO, paymentInfo.getId());
                return false;
            }
        } catch (Exception e) {

            LOG.error(e);
            paymentInfo.setPaymentResult(new PaymentResultDAS().find(Constants.RESULT_UNAVAILABLE));
            return true;
        }

    }

    public void failure(Integer userId, Integer retry) {
    }

    /**
     * Builds the XML 'CustomerProfileTransactionRequest' to send to
     * Authorize.Net
     * 
     * @param PaymentDTOEx
     *            paymentInfo The PaymentDTOEx object as passed to the
     *            PaymentTask interface method
     * @param String
     *            TransactionType The type of transaction to be processed.
     * @param String
     *            approvalCode The authorizationCode as returned from
     *            Authorize.Net during a 'preAuth'
     * @return String The 'CustomerProfileTransactionRequest' XML data
     * @throws PluggableTaskException
     */
    private String getCustomerProfileTransactionRequest(PaymentDTOEx paymentInfo,
            String transactionType, String approvalCode) throws PluggableTaskException {

        StringBuffer XML = new StringBuffer();
        ContactBL contactLoader;
        contactLoader = new ContactBL();
        contactLoader.set(paymentInfo.getUserId());

        ContactDTOEx contact = contactLoader.getDTO();
        String ccfProfileIdName = ensureGetParameter(PARAMETER_CUSTOMER_PROFILE_ID);
        String ccfPaymentIdName = ensureGetParameter(PARAMETER_CUSTOMER_PAYMENT_PROFILE_ID);

        ContactFieldDTO customerProfileIdField = (ContactFieldDTO) contact.getFieldsTable().get(
                ccfProfileIdName);
        ContactFieldDTO customerPaymentProfileIdField = (ContactFieldDTO) contact.getFieldsTable()
                .get(ccfPaymentIdName);

        if (customerProfileIdField == null)
            throw new PluggableTaskException("invalid CCF field '" + PARAMETER_CUSTOMER_PROFILE_ID
                    + "'");

        if (customerPaymentProfileIdField == null)
            throw new PluggableTaskException("invalid CCF field '"
                    + PARAMETER_CUSTOMER_PAYMENT_PROFILE_ID + "'");

        XML.append("<createCustomerProfileTransactionRequest xmlns=\"" + AUTHNET_XML_NAMESPACE
                + "\">");
        XML.append(getMerchantAuthenticationXML());
        XML.append("<transaction>");
        XML.append("<" + transactionType + ">");
        XML.append("<amount>" + paymentInfo.getAmount() + "</amount>");
        XML.append("<customerProfileId>" + customerProfileIdField.getContent()
                + "</customerProfileId>");
        XML.append("<customerPaymentProfileId>" + customerPaymentProfileIdField.getContent()
                + "</customerPaymentProfileId>");

        if (transactionType == "profileTransCaptureOnly")
            XML.append("<approvalCode>" + approvalCode + "</approvalCode>");

        XML.append("</" + transactionType + ">");
        XML.append("</transaction>");
        XML.append("</createCustomerProfileTransactionRequest>");

        return XML.toString();
    }

    /**
     * Returns a 'MerchantAuthentication' XML hierarchy
     * 
     * @return The formatted 'MerchantAuthentication' XML data
     * @throws PluggableTaskException
     */
    private String getMerchantAuthenticationXML() throws PluggableTaskException {

        String name = ensureGetParameter(PARAMETER_NAME);
        String key = ensureGetParameter(PARAMETER_KEY);

        StringBuffer xml = new StringBuffer();

        xml.append("<merchantAuthentication>");
        xml.append("<name>" + name + "</name>");
        xml.append("<transactionKey>" + key + "</transactionKey>");
        xml.append("</merchantAuthentication>");

        return xml.toString();
    }

    /**
     * Sends the request to the Authorize.Net payment processor
     * 
     * @param postVars
     *            String The HTTP POST formatted as a GET string
     * @return String
     * @throws PluggableTaskException
     */
    private String sendViaXML(String data, PaymentDTOEx paymentInfo) throws PluggableTaskException {

        int ch;
        StringBuffer responseText = new StringBuffer();
        String XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + data;

        try {

            // Set up the connection
            String mode = getOptionalParameter(PARAMETER_TEST_MODE, "false");
            URL url = (Boolean.valueOf(mode)) ? new URL(AUTHNET_XML_TEST_URL) : new URL(
                    AUTHNET_XML_PROD_URL);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("CONTENT-TYPE", "application/xml");
            conn.setConnectTimeout(getTimeoutSeconds() * 1000);
            conn.setDoOutput(true);

            LOG.debug("Sending request: " + XML);

            // Send the request
            OutputStream ostream = conn.getOutputStream();
            ostream.write(XML.getBytes());
            ostream.close();

            // Get the response
            InputStream istream = conn.getInputStream();
            while ((ch = istream.read()) != -1)
                responseText.append((char) ch);
            istream.close();
            responseText.replace(0, 3, ""); // KLUDGE: Strips erroneous chars
                                            // from response stream.

            LOG.debug("Authorize.Net response: " + responseText);

            return responseText.toString();

        } catch (Exception e) {

            LOG.error(e);
            throw new PluggableTaskException(e);
        }
    }

    /**
     * Parses the XML response and stores the values in the
     * PaymentAuthorizationDTO
     * 
     * @param HTTPResponse
     *            The HTTP response XML string
     * @return PaymentDTO
     * @throws PluggableTaskException
     */
    private PaymentAuthorizationDTO parseResponse(String HTTPResponse)
            throws PluggableTaskException {

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inStream = new InputSource();
            inStream.setCharacterStream(new StringReader(HTTPResponse));
            Document doc = builder.parse(inStream);
            doc.getDocumentElement().normalize();
            Element rootElement = doc.getDocumentElement();
            NodeList nodeLst = rootElement.getChildNodes();
            NodeList messagesNodeLst = nodeLst.item(0).getChildNodes();
            NodeList resultCodeNodeLst = messagesNodeLst.item(0).getChildNodes();
            String resultCode = resultCodeNodeLst.item(0).getNodeValue();
            NodeList messageNodeLst = messagesNodeLst.item(1).getChildNodes();
            NodeList codeNodeLst = messageNodeLst.item(0).getChildNodes();
            String code = codeNodeLst.item(0).getNodeValue();
            NodeList textNodeLst = messageNodeLst.item(1).getChildNodes();
            String text = textNodeLst.item(0).getNodeValue();

            PaymentAuthorizationDTO paymentDTO = new PaymentAuthorizationDTO();

            // check for errors
            if (!resultCode.equals("Ok")) {

                paymentDTO.setCode1(resultCode);
                paymentDTO.setCode2(code);
                paymentDTO.setResponseMessage(text);
                paymentDTO.setProcessor("PaymentAuthorizeNetCIMTask");

                return paymentDTO;
            }
            /**
             * If the response was ok the direct response node gets parsed and
             * PaymentAuthorizationDTO gets updated with the parsed values
             */
            NodeList directResponseNodeLst = nodeLst.item(1).getChildNodes();
            String response = directResponseNodeLst.item(0).getNodeValue();
            String[] responseList = response.split("\\|", -2);
            paymentDTO.setApprovalCode(responseList[4]);
            paymentDTO.setAvs(responseList[5]);
            paymentDTO.setProcessor("PaymentAuthorizeNetCIMTask");
            paymentDTO.setCode1(responseList[0]);
            paymentDTO.setCode2(responseList[1]);
            paymentDTO.setCode3(responseList[2]);
            paymentDTO.setResponseMessage(responseList[3]);
            paymentDTO.setTransactionId(responseList[6]);
            paymentDTO.setMD5(responseList[37]);
            paymentDTO.setCreateDate(Calendar.getInstance().getTime());

            return paymentDTO;
        } catch (Exception e) {

            LOG.error(e);
            throw new PluggableTaskException(e);
        }
    }
}
