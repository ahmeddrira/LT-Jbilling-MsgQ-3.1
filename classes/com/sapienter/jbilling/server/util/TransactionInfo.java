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

package com.sapienter.jbilling.server.util;

import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.TransactionManager;


/**
 * @author Collin VanDyck
 *
 */
public class TransactionInfo {

    public static final boolean inActiveTransaction()
    {
        try
        {
            TransactionManager tm = (TransactionManager) new InitialContext().lookup("java:/TransactionManager");
            int status = tm.getStatus();
            if (status == Status.STATUS_ACTIVE)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public static final int getTransactionStatus()
    {
        try
        {
            TransactionManager tm = (TransactionManager) new InitialContext().lookup("java:/TransactionManager");
            int status = tm.getStatus();
            return status;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    
    public static final String getTransactionInformation()
    {
        String result = "";

        try
        {
            TransactionManager tm = (TransactionManager) new InitialContext().lookup("java:/TransactionManager");
            int status = tm.getStatus();
            
            switch (status)
            {
                case Status.STATUS_ACTIVE:
                    result = "ACTIVE";
                    break;
                case Status.STATUS_COMMITTED:
                    result = "COMMITTED";
                    break;
                case Status.STATUS_COMMITTING:
                    result = "COMMITTING";
                    break;
                case Status.STATUS_MARKED_ROLLBACK:
                    result = "MARKED_ROLLBACK";
                    break;
                case Status.STATUS_NO_TRANSACTION:
                    result = "NO_TRANSACTION";
                    break;
                case Status.STATUS_PREPARED:
                    result = "PREPARED";
                    break;
                case Status.STATUS_PREPARING:
                    result = "PREPARING";
                    break;
                case Status.STATUS_ROLLEDBACK:
                    result = "ROLLEDBACK";
                    break;
                case Status.STATUS_ROLLING_BACK:
                    result = "ROLLING_BACK";
                    break;
                case Status.STATUS_UNKNOWN:
                    result = "UNKNOWN";
                    break;
                default:
                    result = "UNDEFINED";
            }
        }
        catch (Exception e)
        {
            result = "ERROR: could not get tx status: " + e.getMessage();
        }
        
        return result;
    }

}