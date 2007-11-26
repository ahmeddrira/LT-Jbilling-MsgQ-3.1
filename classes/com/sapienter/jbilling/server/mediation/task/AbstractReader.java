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
package com.sapienter.jbilling.server.mediation.task;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

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
    
    public boolean validate(Vector<String> messages) {
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
