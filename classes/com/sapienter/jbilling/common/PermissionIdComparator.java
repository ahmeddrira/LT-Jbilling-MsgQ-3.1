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

/*
 * Created on Dec 11, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.common;

import java.util.Comparator;

import com.sapienter.jbilling.server.entity.PermissionDTO;

/**
 * @author Emil
 */
public class PermissionIdComparator implements Comparator {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1) {
        PermissionDTO perA = (PermissionDTO) arg0;
        PermissionDTO perB = (PermissionDTO) arg1;
        
        return perA.getId().compareTo(perB.getId());
        
    }

}
