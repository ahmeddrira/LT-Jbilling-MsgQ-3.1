package com.sapienter.jbilling.server.mediation.task;

import java.util.Vector;

public class SeparatorFileReader extends AbstractFileReader {
    
    private String fieldSeparator;

    public SeparatorFileReader() {
    }
    
    @Override
    public boolean validate(Vector<String> messages) {
        boolean retValue = super.validate(messages); 
        
        // optionals
        fieldSeparator = ((String) parameters.get("separator") == null) 
                ? "," : (String) parameters.get("separator");
       
        return retValue;
    }
    
    @Override
    protected String[] splitFields(String line) {
        return line.split(fieldSeparator);
    }
}
