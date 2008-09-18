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


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="contact", uniqueConstraints = @UniqueConstraint(columnNames="user_id"))
public class Contact  implements java.io.Serializable {


     private int id;
     private String organizationName;
     private String streetAddres1;
     private String streetAddres2;
     private String city;
     private String stateProvince;
     private String postalCode;
     private String countryCode;
     private String lastName;
     private String firstName;
     private String personInitial;
     private String personTitle;
     private Integer phoneCountryCode;
     private Integer phoneAreaCode;
     private String phonePhoneNumber;
     private Integer faxCountryCode;
     private Integer faxAreaCode;
     private String faxPhoneNumber;
     private String email;
     private Date createDatetime;
     private short deleted;
     private Short notificationInclude;
     private Integer userId;
     private Set<ContactMap> contactMaps = new HashSet<ContactMap>(0);
     private Set<ContactField> contactFields = new HashSet<ContactField>(0);

    public Contact() {
    }

	
    public Contact(int id, Date createDatetime, short deleted) {
        this.id = id;
        this.createDatetime = createDatetime;
        this.deleted = deleted;
    }
    public Contact(int id, String organizationName, String streetAddres1, String streetAddres2, String city, String stateProvince, String postalCode, String countryCode, String lastName, String firstName, String personInitial, String personTitle, Integer phoneCountryCode, Integer phoneAreaCode, String phonePhoneNumber, Integer faxCountryCode, Integer faxAreaCode, String faxPhoneNumber, String email, Date createDatetime, short deleted, Short notificationInclude, Integer userId, Set<ContactMap> contactMaps, Set<ContactField> contactFields) {
       this.id = id;
       this.organizationName = organizationName;
       this.streetAddres1 = streetAddres1;
       this.streetAddres2 = streetAddres2;
       this.city = city;
       this.stateProvince = stateProvince;
       this.postalCode = postalCode;
       this.countryCode = countryCode;
       this.lastName = lastName;
       this.firstName = firstName;
       this.personInitial = personInitial;
       this.personTitle = personTitle;
       this.phoneCountryCode = phoneCountryCode;
       this.phoneAreaCode = phoneAreaCode;
       this.phonePhoneNumber = phonePhoneNumber;
       this.faxCountryCode = faxCountryCode;
       this.faxAreaCode = faxAreaCode;
       this.faxPhoneNumber = faxPhoneNumber;
       this.email = email;
       this.createDatetime = createDatetime;
       this.deleted = deleted;
       this.notificationInclude = notificationInclude;
       this.userId = userId;
       this.contactMaps = contactMaps;
       this.contactFields = contactFields;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="organization_name", length=200)
    public String getOrganizationName() {
        return this.organizationName;
    }
    
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    
    @Column(name="street_addres1", length=100)
    public String getStreetAddres1() {
        return this.streetAddres1;
    }
    
    public void setStreetAddres1(String streetAddres1) {
        this.streetAddres1 = streetAddres1;
    }
    
    @Column(name="street_addres2", length=100)
    public String getStreetAddres2() {
        return this.streetAddres2;
    }
    
    public void setStreetAddres2(String streetAddres2) {
        this.streetAddres2 = streetAddres2;
    }
    
    @Column(name="city", length=50)
    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    @Column(name="state_province", length=30)
    public String getStateProvince() {
        return this.stateProvince;
    }
    
    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }
    
    @Column(name="postal_code", length=15)
    public String getPostalCode() {
        return this.postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    @Column(name="country_code", length=2)
    public String getCountryCode() {
        return this.countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    
    @Column(name="last_name", length=30)
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    @Column(name="first_name", length=30)
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @Column(name="person_initial", length=5)
    public String getPersonInitial() {
        return this.personInitial;
    }
    
    public void setPersonInitial(String personInitial) {
        this.personInitial = personInitial;
    }
    
    @Column(name="person_title", length=40)
    public String getPersonTitle() {
        return this.personTitle;
    }
    
    public void setPersonTitle(String personTitle) {
        this.personTitle = personTitle;
    }
    
    @Column(name="phone_country_code")
    public Integer getPhoneCountryCode() {
        return this.phoneCountryCode;
    }
    
    public void setPhoneCountryCode(Integer phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }
    
    @Column(name="phone_area_code")
    public Integer getPhoneAreaCode() {
        return this.phoneAreaCode;
    }
    
    public void setPhoneAreaCode(Integer phoneAreaCode) {
        this.phoneAreaCode = phoneAreaCode;
    }
    
    @Column(name="phone_phone_number", length=20)
    public String getPhonePhoneNumber() {
        return this.phonePhoneNumber;
    }
    
    public void setPhonePhoneNumber(String phonePhoneNumber) {
        this.phonePhoneNumber = phonePhoneNumber;
    }
    
    @Column(name="fax_country_code")
    public Integer getFaxCountryCode() {
        return this.faxCountryCode;
    }
    
    public void setFaxCountryCode(Integer faxCountryCode) {
        this.faxCountryCode = faxCountryCode;
    }
    
    @Column(name="fax_area_code")
    public Integer getFaxAreaCode() {
        return this.faxAreaCode;
    }
    
    public void setFaxAreaCode(Integer faxAreaCode) {
        this.faxAreaCode = faxAreaCode;
    }
    
    @Column(name="fax_phone_number", length=20)
    public String getFaxPhoneNumber() {
        return this.faxPhoneNumber;
    }
    
    public void setFaxPhoneNumber(String faxPhoneNumber) {
        this.faxPhoneNumber = faxPhoneNumber;
    }
    
    @Column(name="email", length=200)
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_datetime", nullable=false, length=29)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    
    @Column(name="deleted", nullable=false)
    public short getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(short deleted) {
        this.deleted = deleted;
    }
    
    @Column(name="notification_include")
    public Short getNotificationInclude() {
        return this.notificationInclude;
    }
    
    public void setNotificationInclude(Short notificationInclude) {
        this.notificationInclude = notificationInclude;
    }
    
    @Column(name="user_id", unique=true)
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="contact")
    public Set<ContactMap> getContactMaps() {
        return this.contactMaps;
    }
    
    public void setContactMaps(Set<ContactMap> contactMaps) {
        this.contactMaps = contactMaps;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="contact")
    public Set<ContactField> getContactFields() {
        return this.contactFields;
    }
    
    public void setContactFields(Set<ContactField> contactFields) {
        this.contactFields = contactFields;
    }




}


