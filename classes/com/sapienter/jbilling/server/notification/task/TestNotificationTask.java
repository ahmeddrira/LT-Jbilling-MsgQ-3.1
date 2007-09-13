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
package com.sapienter.jbilling.server.notification.task;

import java.io.FileWriter;
import java.util.Calendar;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.pluggableTask.NotificationTask;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.ContactDTOEx;

public class TestNotificationTask extends PluggableTask implements NotificationTask {
    
    public static final String PARAMETER_FROM = "from";
    public static final Logger LOG = Logger.getLogger(TestNotificationTask.class);

    public void deliver(UserEntityLocal user, MessageDTO sections)
            throws TaskException {
        String directory = Util.getSysProp("base_dir");
        try {
            FileWriter writer = new FileWriter(directory + "/emails_sent.txt", true);
            
            // find the address
            ContactBL contact = new ContactBL();
            Vector<ContactDTOEx> emails = contact.getAll(user.getUserId());
            
            // find the from
            String from = (String) parameters.get(PARAMETER_FROM);
            if (from == null || from.length() == 0) {
                from = Util.getSysProp("email_from");
            }
            
            writer.write("Date: " + Calendar.getInstance().getTime() + "\n");
            writer.write("To: " + emails.get(0).getEmail() + "\n");
            writer.write("From: " + from + "\n");
            writer.write("Subject: " + sections.getContent()[0].getContent() + "\n");
            writer.write("Body: " + sections.getContent()[1].getContent() + "\n");
            writer.write("Attachement: " + sections.getAttachmentFile() + "\n");
            writer.write("        ----------------------        \n");
            
            writer.close();
            
            LOG.debug("Sent test notification to " + user.getUserId());
        } catch (Exception e) {
            LOG.error("Error sending test notification:" + e.getMessage());
            throw new TaskException(e);
        }

    }

}
