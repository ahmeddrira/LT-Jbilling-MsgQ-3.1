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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="role"
    ,schema="public"
)
public class Role  implements java.io.Serializable {


     private int id;
     private Set<BaseUser> baseUsers = new HashSet<BaseUser>(0);
     private Set<Permission> permissions = new HashSet<Permission>(0);

    public Role() {
    }

	
    public Role(int id) {
        this.id = id;
    }
    public Role(int id, Set<BaseUser> baseUsers, Set<Permission> permissions) {
       this.id = id;
       this.baseUsers = baseUsers;
       this.permissions = permissions;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="user_role_map", schema="public", joinColumns = { 
        @JoinColumn(name="role_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="user_id", updatable=false) })
    public Set<BaseUser> getBaseUsers() {
        return this.baseUsers;
    }
    
    public void setBaseUsers(Set<BaseUser> baseUsers) {
        this.baseUsers = baseUsers;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="permission_role_map", schema="public", joinColumns = { 
        @JoinColumn(name="role_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="permission_id", updatable=false) })
    public Set<Permission> getPermissions() {
        return this.permissions;
    }
    
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }




}


