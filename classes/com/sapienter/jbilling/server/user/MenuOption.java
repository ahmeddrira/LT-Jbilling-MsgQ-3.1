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

package com.sapienter.jbilling.server.user;

import java.util.Comparator;
import java.util.Vector;

import com.sapienter.jbilling.server.entity.MenuOptionDTO;

public class MenuOption extends MenuOptionDTO implements Comparator {
    private String display;
    private Boolean selected;
    private Vector options;
    private Integer parentId;

    public MenuOption() {
        options = new Vector();
    }
    
    // this makes the object ordenable, so the options get sorted for
    // a consitent displaying
    public int compare(Object o1, Object o2) {
        
        return ((MenuOption)o1).getId().compareTo(
                ((MenuOption)o2).getId());
    }
    /**
     * @return
     */
    public String getDisplay() {
        return display;
    }

    /**
     * @return
     */
    public Vector getOptions() {
        return options;
    }

    /**
     * @return
     */
    public Boolean getSelected() {
        return selected;
    }

    /**
     * @param string
     */
    public void setDisplay(String string) {
        display = string;
    }

    /**
     * @param vector
     */
    public void setOptions(Vector vector) {
        options = vector;
    }

    /**
     * @param boolean1
     */
    public void setSelected(Boolean boolean1) {
        selected = boolean1;
    }

    /**
     * @return
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * @param integer
     */
    public void setParentId(Integer integer) {
        parentId = integer;
    }


}
