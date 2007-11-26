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
package com.sapienter.jbilling.server.mediation;

public class FormatField {
    private String name;
    private String type;
    private Integer startPosition;
    private Integer length;
    private boolean isKey;
    private String durationFormat;
    
    public void isKeyTrue() {
        this.isKey = true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FormatField() {
        isKey = false;
    }
    
    public boolean getIsKey() {
        return isKey;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    
    public String toString() {
        return "name: " + name + " type: " + type + " isKey: " + isKey + 
                " startPosition " + startPosition + " length " + length;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = Integer.valueOf(length);
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = Integer.valueOf(startPosition);
    }

    public String getDurationFormat() {
        return durationFormat;
    }

    public void setDurationFormat(String durationFormat) {
        this.durationFormat = durationFormat;
    }
}
