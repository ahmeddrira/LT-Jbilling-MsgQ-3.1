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
package com.sapienter.jbilling.server.util.db;

import javax.persistence.EntityTransaction;

public class NoTransaction implements EntityTransaction {

    public void begin() {
        // TODO Auto-generated method stub

    }

    public void commit() {
        // TODO Auto-generated method stub

    }

    public boolean getRollbackOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isActive() {
        // TODO Auto-generated method stub
        return false;
    }

    public void rollback() {
        // TODO Auto-generated method stub

    }

    public void setRollbackOnly() {
        // TODO Auto-generated method stub

    }

}
