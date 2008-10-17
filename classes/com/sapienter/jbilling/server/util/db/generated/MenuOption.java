/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="menu_option")
public class MenuOption  implements java.io.Serializable {


     private int id;
     private MenuOption menuOption;
     private String link;
     private int levelField;
     private Set<MenuOption> menuOptions = new HashSet<MenuOption>(0);

    public MenuOption() {
    }

	
    public MenuOption(int id, String link, int levelField) {
        this.id = id;
        this.link = link;
        this.levelField = levelField;
    }
    public MenuOption(int id, MenuOption menuOption, String link, int levelField, Set<MenuOption> menuOptions) {
       this.id = id;
       this.menuOption = menuOption;
       this.link = link;
       this.levelField = levelField;
       this.menuOptions = menuOptions;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    public MenuOption getMenuOption() {
        return this.menuOption;
    }
    
    public void setMenuOption(MenuOption menuOption) {
        this.menuOption = menuOption;
    }
    
    @Column(name="link", nullable=false, length=100)
    public String getLink() {
        return this.link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    @Column(name="level_field", nullable=false)
    public int getLevelField() {
        return this.levelField;
    }
    
    public void setLevelField(int levelField) {
        this.levelField = levelField;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="menuOption")
    public Set<MenuOption> getMenuOptions() {
        return this.menuOptions;
    }
    
    public void setMenuOptions(Set<MenuOption> menuOptions) {
        this.menuOptions = menuOptions;
    }




}


