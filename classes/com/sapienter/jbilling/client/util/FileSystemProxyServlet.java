/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.client.util;

import com.sapienter.jbilling.common.Util;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

/**
 * FileSystemProxyServlet
 *
 * @author Brian Cowdery
 * @since 07-10-2010
 */
public class FileSystemProxyServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(FileSystemProxyServlet.class);

    private static final int BUFFER_SIZE = 1024;
    private static final String RESOURCES_ROOT = Util.getSysProp("base_dir");

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filepath = RESOURCES_ROOT + request.getPathInfo();
        InputStream in = null;
        OutputStream out = null;

        try {
            // Load resource from filesystem, returning HTTP FileNotFound on Error
            try {
                in = new FileInputStream(filepath);
            } catch (FileNotFoundException e) {
                LOG.warn("Unable to access file: " + filepath, e);
                response.sendError(404);
                return;
            }

            // Stream the content to browser
            response.setContentType(URLConnection.guessContentTypeFromName(filepath));

            byte[] buffer = new byte[BUFFER_SIZE];
            out = response.getOutputStream();
            
            int bytesRead = 0;
            while ((bytesRead = in.read(buffer, 0, BUFFER_SIZE)) > -1)
                out.write(buffer, 0, bytesRead);
            out.flush();

        } finally {
            if (in != null)
                in.close();

            if (out != null)
                out.close();
        }
    }
}
