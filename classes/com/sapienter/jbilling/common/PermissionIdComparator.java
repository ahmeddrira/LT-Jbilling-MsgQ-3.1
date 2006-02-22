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
