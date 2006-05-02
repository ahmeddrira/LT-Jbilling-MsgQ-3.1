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

package com.sapienter.jbilling.server.pluggableTask;

import java.io.File;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.MessageSection;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.ContactDTOEx;

public class BasicEmailNotificationTask extends PluggableTask
        implements NotificationTask {

    // pluggable task parameters names
    public static final String PARAMETER_SMTP_SERVER = "smtp_server";
    public static final String PARAMETER_FROM = "from";
    public static final String PARAMETER_FROM_NAME = "from_name";
    public static final String PARAMETER_PORT = "port";
    public static final String PARAMETER_USERNAME = "username";
    public static final String PARAMETER_PASSWORD = "password";
    public static final String PARAMETER_REPLYTO = "reply_to";
    public static final String PARAMETER_BCCTO = "bcc_to";

    /* 
     * This will send an email to the main contant of the provided user
     * It will expect two sections to compose the email message:
     * 1 - The subject
     * 2 - The body 
     */
    public void deliver(UserEntityLocal user, MessageDTO message) 
            throws TaskException {
    	Logger log = Logger.getLogger(BasicEmailNotificationTask.class);
        
        // do not process paper invoices. So far, all the rest are emails
        // This if is necessary because an entity can have some customers
        // with paper invoices and others with emal invoices.
        if (message.getTypeId().compareTo(
                MessageDTO.TYPE_INVOICE_PAPER) == 0) {
            return;
        }
        
        Properties prop = new Properties();   
        // verify that we've got the right number of sections
        MessageSection[] sections = message.getContent();
        if (sections.length != 2) {
            throw new TaskException("This task takes two sections." +
                    sections.length + " found.");             
        }
        
        // set some parameters
        String server = (String) parameters.get(PARAMETER_SMTP_SERVER);
        if (server == null || server.length() == 0) {
            server = Util.getSysProp("smtp_server");
        }
        
        int port = Integer.parseInt(Util.getSysProp("smtp_port"));
        String strPort = (String) parameters.get(PARAMETER_PORT);
        if (strPort != null && strPort.trim().length() > 0) {
        	try {
        		port = Integer.valueOf(strPort).intValue();
        	} catch (NumberFormatException e) {
        		log.error("The port is not a number", e);
        	}
        }
        String username = (String) parameters.get(PARAMETER_USERNAME);
        if (username == null || username.length() == 0) {
        	username = Util.getSysProp("smtp_username");
        }
        String password = (String) parameters.get(PARAMETER_PASSWORD);
        if (password == null || password.length() == 0) {
        	password = Util.getSysProp("smtp_password");
        }
        String replyTo = (String) parameters.get(PARAMETER_REPLYTO);
        
        // create the session & message
        prop.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(prop);
        Message msg = new MimeMessage(session);
        
        // set the message's fields
        // the to address/es
        try {
            ContactBL contact = new ContactBL();
            Vector contacts = contact.getAll(user.getUserId());
            for (int f = 0; f < contacts.size(); f++) {
                ContactDTOEx record = (ContactDTOEx) contacts.get(f);
                String address = record.getEmail();
                if (record.getInclude() != null && 
                        record.getInclude().intValue() == 1 && 
                        address != null && address.trim().length() > 0) {
                    msg.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(address, false));
                }
            }
            if (msg.getAllRecipients() == null || 
                    msg.getAllRecipients().length == 0) {
                // not a huge deal, but no way I can send anything
                log.info("User without email address " + 
                        user.getUserId());
                return;
            }

        } catch (Exception e) {
            log.debug("Exception setting addresses ", e);
            throw new TaskException("Setting addresses");
        }
        
        
        
        // the from address
        String from = (String) parameters.get(PARAMETER_FROM);
        if (from == null || from.length() == 0) {
            from = Util.getSysProp("email_from");
        }

        String fromName = (String) parameters.get(PARAMETER_FROM_NAME);
        try {
            if (fromName == null || fromName.length() == 0) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom(new InternetAddress(from, fromName));
            }
        } catch (Exception e1) {
            throw new TaskException("Invalid from address:" + from + 
                    "."+ e1.getMessage());
        } 
        // the reply to 
        if (replyTo != null && replyTo.length() > 0) {
        	try {
				InternetAddress rep[] = new InternetAddress[1];
				rep[0] = new InternetAddress(replyTo);
				msg.setReplyTo(rep);
			} catch (Exception e5) {
				log.error("Exception when setting the replyTo address: " + 
						replyTo, e5);
			}
        }
        // the bcc if specified
        String bcc = (String) parameters.get(PARAMETER_BCCTO);
        if (bcc != null && bcc.length() > 0) {
            try {
                msg.addRecipient(Message.RecipientType.BCC,
                        new InternetAddress(bcc, false));
            } catch (AddressException e5) {
                log.warn("The bcc address " + bcc + " is not valid. " +
                        "Sending without bcc", e5);
            } catch (MessagingException e5) {
                throw new TaskException("Exception setting bcc " + 
                        e5.getMessage());
            }
        }
        
        // the subject and body
        try {
            for (int f=0; f < sections.length; f++) {
                if (sections[f].getSection().intValue() == 1) {
                    msg.setSubject(sections[f].getContent());
                } else { // it has to be two ..
                    if (message.getAttachmentFile() == null) { 
                        msg.setText(sections[f].getContent());
                    } else {
                        // because of the attachment
                        // it is a 'multi part' email
                        MimeMultipart mp = new MimeMultipart();
                        
                        // the text message is one part
                        MimeBodyPart text = new MimeBodyPart();
                        text.setDisposition(Part.INLINE);
                        text.setContent(sections[f].getContent(), "text/plain");
                        mp.addBodyPart(text);

                        // the attachement is another.
                        MimeBodyPart file_part = new MimeBodyPart();
                        File file = (File) new File(message.getAttachmentFile());
                        FileDataSource fds = new FileDataSource(file);
                        DataHandler dh = new DataHandler(fds);
                        file_part.setFileName(file.getName());
                        file_part.setDisposition(Part.ATTACHMENT);
                        file_part.setDescription("Attached file: " + file.getName());
                        file_part.setDataHandler(dh);
                        mp.addBodyPart(file_part);

                        msg.setContent(mp);
                    }
                }
            }
        } catch (MessagingException e2) {
            throw new TaskException("Exception setting up the subject and/or" +                    " body." + e2.getMessage());
        }
        // the date
        try {
            msg.setSentDate(Calendar.getInstance().getTime());
        } catch (MessagingException e3) {
            throw new TaskException("Exception setting up the date" +
                    "." + e3.getMessage());
        }
        
        // send the message
        try {
            String allEmails = "";
            for (int f =0 ; f < msg.getAllRecipients().length; f++) {
                allEmails = allEmails + " " + 
                        msg.getAllRecipients()[f].toString();
            }
            log.debug(
                    "Sending email to " + allEmails + " bcc " + bcc + " server=" + server + 
                    " port=" + port + " username=" + username + " password=" +
                    password);
        	Transport transport = session.getTransport("smtp");
            transport.connect(server, port, username, password);
            
            transport.sendMessage(msg, msg.getAllRecipients());
            //if there was an attachment, remove the file
            if (message.getAttachmentFile() != null) {
                File file = new File(message.getAttachmentFile());
                if (!file.delete()) {
                    log.debug("Could not delete attachment file " + 
                            file.getName());
                }
            }
        } catch (MessagingException e4) {
            // send an emial to the entity to let it know about the failure
        	try {
                String params[] = new String[6]; // five parameters for this message;
                params[0] = (e4.getMessage() == null ? "No detailed exception message"  : e4.getMessage());
                params[1] = "";
                for (int f =0 ; f < msg.getAllRecipients().length; f++) {
                    params[1] = params[1] + " " + 
                            msg.getAllRecipients()[f].toString();
                }
                params[2] = server;
                params[3] = port + " ";
                params[4] = username;
                params[5] = password;
                
                NotificationBL.sendSapienterEmail(user.getEntity().getId(), 
                        "notification.email.error", null, params);
                
            } catch (Exception e5) {
                log.warn("Exception sending error message to entity", e5);
            } 
            throw new TaskException("Exception sending the message" +
                    "." + e4.getMessage());
        }
    }

}
