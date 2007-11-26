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

package com.sapienter.jbilling.server.list;

import java.io.Serializable;
import java.util.Vector;

public class ListDTO implements Serializable {
    private Vector<Object[]> lines = null;
    private Integer types[] = null;
    
    public ListDTO() {
        lines = new Vector<Object[]>();
    }
    /**
     * @return
     */
    public Vector<Object[]> getLines() {
        return lines;
    }

    /**
     * @return
     */
    public Integer[] getTypes() {
        return types;
    }

    /**
     * @param vector
     */
    public void setLines(Vector<Object[]> vector) {
        lines = vector;
    }

    /**
     * @param integers
     */
    public void setTypes(Integer[] integers) {
        types = integers;
    }

}
