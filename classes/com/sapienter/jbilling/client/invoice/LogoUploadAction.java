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

Contributor(s): Lucas Pickstone_______________________.
 */

package com.sapienter.jbilling.client.invoice;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.client.util.Constants;

/**
 * Saves uploaded logo image file for the user's entity (company).
 * 
 * @author Lucas Pickstone
 */
public class LogoUploadAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        Logger log = Logger.getLogger(LogoUploadAction.class);
        HttpSession session = request.getSession(false);
        DynaActionForm logoUploadForm = (DynaActionForm) form;
        ActionMessages messages = new ActionMessages();
        ActionErrors errors = new ActionErrors();
        Integer entityId = (Integer) session.getAttribute(
                Constants.SESSION_ENTITY_ID_KEY);

        try {
            FormFile logoFile = (FormFile) logoUploadForm.get("logoFile");

            // if image file was received
            if (logoFile != null) {
                // and is a JPEG image file
                // (mime types: firefox -> image/jpeg, IE -> image/pjpeg)
                if (logoFile.getContentType().equals("image/jpeg")
                        || logoFile.getContentType().equals("image/pjpeg")) {
                    // get file as a stream and write it into a file
                    InputStream inStream = logoFile.getInputStream();
                    File imageFile = new File(Util.getSysProp("base_dir")
                            + "logos" + File.separator + "entity-" + entityId
                            + ".jpg");
                    FileOutputStream outFile = new FileOutputStream(imageFile);

                    while (inStream.available() != 0) {
                        outFile.write(inStream.read());
                    }

                    outFile.close();

                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("invoice.logo.done"));
                    saveMessages(request, messages);

                    return mapping.findForward("done");
                } else {
                    errors.add("logoFile", 
                            new ActionError("invoice.logo.fileType"));
                }
            } else {
                errors.add("logoFile", 
                        new ActionError("invoice.logo.fileSize"));
            }
        } catch (Exception e) {
            log.error("Exception ", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, 
                    new ActionError("invoice.logo.error"));
        }

        saveErrors(request, errors);
        return mapping.findForward("done");
    }
}
