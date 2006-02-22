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
 * Created on Apr 28, 2003
 */
package com.sapienter.jbilling.server.pluggableTask;

import com.sapienter.jbilling.interfaces.BillingProcessEntityLocal;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;

/**
 * For those entities that never want to get an invoice delegated
 */
public class NoInvoiceFilterTask extends PluggableTask
    implements InvoiceFilterTask {

    public boolean isApplicable(InvoiceEntityLocal invoice, 
            BillingProcessEntityLocal process) throws TaskException {

        return false;
    }

}
