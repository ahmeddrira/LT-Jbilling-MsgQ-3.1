/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

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
package com.sapienter.jbilling.server.mediation.task;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.mediation.Format;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;

public abstract class AbstractReader extends PluggableTask implements
        IMediationReader {

    private static final Logger LOG = Logger.getLogger(AbstractReader.class);
    protected String formatFileName = null;
    protected Format format = null;
    
    public boolean validate(List<String> messages) {
        String formatFile = (String) parameters.get("format_file");
        if (formatFile == null) {
            messages.add("parameter format_file is required");
            return false;
        }
        
        String formatDirectory = ((String) parameters.get("format_directory") == null) 
            ? Util.getSysProp("base_dir") + "mediation" : (String) parameters.get("format_directory");

        formatFileName = formatDirectory + "/" + formatFile;
        
        return true;
    }

    public abstract Iterator<Record> iterator();

    protected Format getFormat() 
            throws IOException, SAXException {
        // parse the XML ...
        // create a field object per field element
        if (format == null) {
            Digester digester = new Digester();
            digester.setValidating(true);
            digester.addObjectCreate("format", "com.sapienter.jbilling.server.mediation.Format");
            digester.addObjectCreate("format/field", "com.sapienter.jbilling.server.mediation.FormatField");
            digester.addCallMethod("format/field/name","setName",0);
            digester.addCallMethod("format/field/type","setType",0);
            digester.addCallMethod("format/field/startPosition","setStartPosition",0);
            digester.addCallMethod("format/field/durationFormat","setDurationFormat",0);
            digester.addCallMethod("format/field/length","setLength",0);
            digester.addCallMethod("format/field/isKey","isKeyTrue");
            digester.addSetNext("format/field", "addField", "com.sapienter.jbilling.server.mediation.FormatField");
    
            format = (Format) digester.parse(new File(formatFileName));
            
            LOG.debug("using format: " + format);
        } 

        return format;
        
    }
}
