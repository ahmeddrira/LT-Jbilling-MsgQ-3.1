/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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
