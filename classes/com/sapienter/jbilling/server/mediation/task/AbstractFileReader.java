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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.Format;
import com.sapienter.jbilling.server.mediation.FormatField;
import com.sapienter.jbilling.server.mediation.GroupRecordComparator;
import com.sapienter.jbilling.server.mediation.Record;

public abstract class AbstractFileReader extends AbstractReader {
    
    private String directory;
    private String suffix;
    private boolean rename;
    private SimpleDateFormat dateFormat;
    private boolean removeQuote;
    private boolean autoID;
    private static final Logger LOG = Logger.getLogger(AbstractFileReader.class);

    public AbstractFileReader() {
    }
    
    @Override
    public boolean validate(List<String> messages) {
        boolean retValue = super.validate(messages); 
        
        // optionals
        directory = ((String) parameters.get("directory") == null) 
                ? Util.getSysProp("base_dir") + "mediation" : (String) parameters.get("directory");
        suffix = ((String) parameters.get("suffix") == null) 
                ? "ALL" : (String) parameters.get("suffix");
        rename = Boolean.parseBoolean(((String) parameters.get("rename") == null) 
                ? "false" : (String) parameters.get("rename"));
        dateFormat = new SimpleDateFormat(((String) parameters.get("date_format") == null) 
                ? "yyyyMMdd-HHmmss" : (String) parameters.get("date_format"));
        removeQuote = Boolean.parseBoolean(((String) parameters.get("removeQuote") == null)
                ? "true" : (String) parameters.get("removeQuote"));
        autoID = Boolean.parseBoolean(((String) parameters.get("autoID") == null)
                ? "false" : (String) parameters.get("autoID"));

        if (directory == null) {
            messages.add("The plug-in parameter 'directory' is mandatory");
            retValue = false;
        }

        LOG.debug("Started with " + " directory: " + directory + " suffix " + suffix + " rename " +
                rename + " date  format " + dateFormat.toPattern() + " removeQuote " + removeQuote +
                " autoID " + autoID);
       
        return retValue;
    }
    
    @Override
    public Iterator<Record> iterator() {
        try {
            return new Reader();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    public class Reader implements Iterator<Record> {
        private final Logger LOG = Logger.getLogger(Reader.class);
        private File[] files = null;
        private int fileIndex = 0;
        private Record previous = null;
        private BufferedReader reader = null;
        private String line;
        private int groupPosition;
        private int counter;
        private final GroupRecordComparator groupComparator;
        private final Format format;
        
        protected Reader() throws FileNotFoundException, IOException, SAXException {
            files = new File(directory).listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (suffix.equalsIgnoreCase("all") || pathname.getName().endsWith(suffix)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            
            if (!nextReader()) {
                LOG.info("No files found to process");
                format = null;
                groupComparator = null;
            } else {
                LOG.debug("Files to process = " + files.length);
                format = getFormat();
                groupComparator = new GroupRecordComparator(format);
                counter = 0;
            }
        }
       
        /**
         * Get the next line of a whatever file is in the list
         */
        public boolean hasNext() {
            if (reader == null) {
                return false;
            }
            
            try {
                line = reader.readLine();
                counter++;
                if (line == null) {
                    // we are done with this file
                    reader.close();
                    // rename it to avoid re-processing, if configured
                    if (rename) {
                        if (!files[fileIndex].renameTo(
                                new File(files[fileIndex].getAbsolutePath() + ".done"))) {
                            LOG.warn("Could not rename file " + files[fileIndex].getName());
                        }
                    }
                    // reached the last line, go to the next file
                    if (!nextReader()) {
                        return false; // all done then
                    } else {
                        // read the first line from the next file
                        line = reader.readLine();
                        counter = 1;
                    }
                }
            } catch (Exception e) {
                throw new SessionInternalError(e);
            } 
            return line != null;
        }
        
        /**
         * Takes the last read line and transforms it into a Record
         */
        public Record next() {
            if (line == null) {
                throw new NoSuchElementException();
            }

            // get the raw fields from the line
            String tokens[] = splitFields(line);
            if (tokens.length != format.getFields().size() && !autoID) {
                throw new SessionInternalError("Mismatch of number of fields between " +
                        "the format and the file for line " + line + " Expected " + 
                        format.getFields().size() + " found " + tokens.length);
                
            }
            // remove quotes if needed
            if (removeQuote) {
                for (int f = 0; f < tokens.length; f++) {
                    if (tokens[f].length() < 2) {
                        continue;
                    }
                    // remove first and last char, if they are quotes
                    if ((tokens[f].charAt(0) == '\"' || tokens[f].charAt(0) == '\'') &&
                            (tokens[f].charAt(tokens[f].length() - 1) == '\"' || tokens[f].charAt(tokens[f].length() - 1) == '\'')) {
                        tokens[f] = tokens[f].substring(1, tokens[f].length() - 1);
                    }
                }
            }
            
            // create the record
            Record record = new Record();
            int tkIdx = 0;
            for (FormatField field:format.getFields()) {

                if (autoID && field.getIsKey()) {
                    record.addField(new PricingField(field.getName(),
                                files[fileIndex].getName() + "-" + counter ), field.getIsKey());
                    continue;
                }
                
                switch (PricingField.mapType(field.getType())) {
                    case STRING:
                        record.addField(new PricingField(field.getName(), 
                                tokens[tkIdx++]), field.getIsKey());
                        break;
                    case INTEGER:
                        String intStr = tokens[tkIdx++].trim();
                        if (field.getDurationFormat() != null && field.getDurationFormat().length() > 0) {
                            // requires hour/minute conversion
                            record.addField(new PricingField(field.getName(), intStr.length() > 0 ?
                                    convertDuration(intStr, field.getDurationFormat()) : null),
                                    	field.getIsKey());
                        } else {
                            try {
                                record.addField(new PricingField(field.getName(), intStr.length() > 0 ?
                                        Integer.valueOf(intStr.trim()) : null), field.getIsKey());
                            } catch (NumberFormatException e) {
                                throw new SessionInternalError("Converting to integer " + field + 
                                        " line " + line, AbstractFileReader.class, e);
                            }
                        }
                        break;
                    case DATE:
                        try {
                            String dateStr = tokens[tkIdx++];
                            record.addField(new PricingField(field.getName(), dateStr.length() > 0 ?
                                    dateFormat.parse(dateStr) : null), field.getIsKey());
                        } catch (ParseException e) {
                            throw new SessionInternalError("Using format: " + dateFormat + "[" +
                                    parameters.get("date_format") + "]", 
                                    AbstractFileReader.class,e);
                        }
                        break;
                    case DECIMAL:
                        String floatStr = tokens[tkIdx++].trim();
                        record.addField(new PricingField(field.getName(), floatStr.length() > 0 ?
                                new BigDecimal(floatStr) : null), field.getIsKey());
                        break;
                }
            }
            
            // to assign the position, we need to compare with the previous record
            if (groupComparator.compare(record, previous) == 0) {
                groupPosition++;
            } else {
                groupPosition = 1;
            }
            record.setPosition(groupPosition);
            previous = record;
            return record;
        }
        
        private boolean nextReader() throws FileNotFoundException {
            if (reader != null) { // first call
                fileIndex++;
            }
            
            if (files.length > fileIndex) { // any more to process ?
                reader = new BufferedReader(new java.io.FileReader(files[fileIndex]));
                LOG.debug("Now processing file " + files[fileIndex].getName());
                return true;
            }
            
            reader = null;
            return false;
        }
        
        public void remove() {
            // needed to comply with Iterator only
            throw new SessionInternalError("remove not supported");
        }
    }
    
    /**
     * Chars 'H', 'M' and 'S' have to be grouped or the behaviour will be unexpected
     * @param content
     * @param format
     * @return
     */
    public static int convertDuration(String content, String format) {
        int totalSeconds = 0;
        // hours
        
        try {
            try {
                totalSeconds += Integer.valueOf(content.substring(format.indexOf('H'),
                        format.lastIndexOf('H') + 1).trim()) * 60 * 60;
            } catch (IndexOutOfBoundsException e) {
                // no hours. ok
            }
            // minutes
            try {
                totalSeconds += Integer.valueOf(content.substring(format.indexOf('M'),
                        format.lastIndexOf('M') + 1).trim()) * 60;
            } catch (IndexOutOfBoundsException e) {
                // no minutes. ok
            }
            // seconds
            try {
                totalSeconds += Integer.valueOf(content.substring(format.indexOf('S'),
                        format.lastIndexOf('S') + 1).trim());
            } catch (IndexOutOfBoundsException e) {
                // no seconds. ok
            }
        } catch (NumberFormatException e) {
            throw new SessionInternalError("converting duration format " + format + " content " + content,
                    AbstractFileReader.class, e);
        }
        
        return totalSeconds;
    }

    protected abstract String[] splitFields(String line);
}
