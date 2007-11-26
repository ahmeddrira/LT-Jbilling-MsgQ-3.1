package com.sapienter.jbilling.server.mediation.task;

import java.util.Vector;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.mediation.FormatField;

public class FixedFileReader extends AbstractFileReader {

    @Override
    protected String[] splitFields(String line) {
        Vector<String> fields = new Vector<String>();
        
        for(FormatField formatField: format.getFields()) {
            if (formatField.getStartPosition() == null || formatField.getLength() == null ||
                    formatField.getStartPosition() <= 0 || formatField.getLength() <= 0) {
                throw new SessionInternalError("position and length are required and have to " +
                        "be positive integers: " + formatField);
            }
            int startIndex = formatField.getStartPosition() - 1;
            String field;
            try {
                field = line.substring(startIndex, startIndex + formatField.getLength());
            } catch (Exception e) {
                throw new SessionInternalError("Error with field " + formatField + 
                        " on line " + line, FixedFileReader.class, e);
            }
            fields.add(field);
        }
        
        String retValue[] = new String[fields.size()];
        retValue = fields.toArray(retValue);
        return retValue;
    }

}
