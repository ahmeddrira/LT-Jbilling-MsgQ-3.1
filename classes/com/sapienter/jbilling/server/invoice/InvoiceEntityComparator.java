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
 * Created on Nov 22, 2004
 *
 */
package com.sapienter.jbilling.server.invoice;

import java.util.Comparator;

import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;

/**
 * @author Emil
 */
public class InvoiceEntityComparator implements Comparator {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        InvoiceEntityLocal parA = (InvoiceEntityLocal) o1;
        InvoiceEntityLocal parB = (InvoiceEntityLocal) o2;
        
        return parA.getId().compareTo(parB.getId());
    }

}
