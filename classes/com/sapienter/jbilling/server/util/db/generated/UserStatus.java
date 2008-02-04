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
package com.sapienter.jbilling.server.util.db.generated;


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sapienter.jbilling.server.user.db.BaseUser;

@Entity
@Table(name="user_status"
    ,schema="public"
)
public class UserStatus  implements java.io.Serializable {


     private int id;
     private short canLogin;
     private Set<AgeingEntityStep> ageingEntitySteps = new HashSet<AgeingEntityStep>(0);
     private Set<BaseUser> baseUsers = new HashSet<BaseUser>(0);

    public UserStatus() {
    }

	
    public UserStatus(int id, short canLogin) {
        this.id = id;
        this.canLogin = canLogin;
    }
    public UserStatus(int id, short canLogin, Set<AgeingEntityStep> ageingEntitySteps, Set<BaseUser> baseUsers) {
       this.id = id;
       this.canLogin = canLogin;
       this.ageingEntitySteps = ageingEntitySteps;
       this.baseUsers = baseUsers;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="can_login", nullable=false)
    public short getCanLogin() {
        return this.canLogin;
    }
    
    public void setCanLogin(short canLogin) {
        this.canLogin = canLogin;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="userStatus")
    public Set<AgeingEntityStep> getAgeingEntitySteps() {
        return this.ageingEntitySteps;
    }
    
    public void setAgeingEntitySteps(Set<AgeingEntityStep> ageingEntitySteps) {
        this.ageingEntitySteps = ageingEntitySteps;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="userStatus")
    public Set<BaseUser> getBaseUsers() {
        return this.baseUsers;
    }
    
    public void setBaseUsers(Set<BaseUser> baseUsers) {
        this.baseUsers = baseUsers;
    }




}


