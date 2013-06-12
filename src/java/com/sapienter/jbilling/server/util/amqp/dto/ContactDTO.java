package com.sapienter.jbilling.server.util.amqp.dto;

import java.util.Date;

public class ContactDTO {
	private Integer id;
	private String organizationName;
	private String address1;
	private String address2;
	private String city;
	private String stateProvince;
	private String postalCode;
	private String countryCode;
	private String lastName;
	private String firstName;
	private String initial;
	private String title;
	private Integer phoneCountryCode;
	private Integer phoneAreaCode;
	private String phoneNumber;
	private Integer faxCountryCode;
	private Integer faxAreaCode;
	private String faxNumber;
	private String email;
	private Date createDate;
	private int deleted;
	private Boolean include;

	private Integer type = null; // the contact type

	private Integer contactTypeId = null;
	private String contactTypeDescr = null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPhoneCountryCode() {
		return phoneCountryCode;
	}

	public void setPhoneCountryCode(Integer phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	public Integer getPhoneAreaCode() {
		return phoneAreaCode;
	}

	public void setPhoneAreaCode(Integer phoneAreaCode) {
		this.phoneAreaCode = phoneAreaCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getFaxCountryCode() {
		return faxCountryCode;
	}

	public void setFaxCountryCode(Integer faxCountryCode) {
		this.faxCountryCode = faxCountryCode;
	}

	public Integer getFaxAreaCode() {
		return faxAreaCode;
	}

	public void setFaxAreaCode(Integer faxAreaCode) {
		this.faxAreaCode = faxAreaCode;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public Boolean getInclude() {
		return include;
	}

	public void setInclude(Boolean include) {
		this.include = include;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getContactTypeId() {
		return contactTypeId;
	}

	public void setContactTypeId(Integer contactTypeId) {
		this.contactTypeId = contactTypeId;
	}

	public String getContactTypeDescr() {
		return contactTypeDescr;
	}

	public void setContactTypeDescr(String contactTypeDescr) {
		this.contactTypeDescr = contactTypeDescr;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContactDTO [id=").append(id)
				.append(", organizationName=").append(organizationName)
				.append(", address1=").append(address1).append(", address2=")
				.append(address2).append(", city=").append(city)
				.append(", stateProvince=").append(stateProvince)
				.append(", postalCode=").append(postalCode)
				.append(", countryCode=").append(countryCode)
				.append(", lastName=").append(lastName).append(", firstName=")
				.append(firstName).append(", initial=").append(initial)
				.append(", title=").append(title).append(", phoneCountryCode=")
				.append(phoneCountryCode).append(", phoneAreaCode=")
				.append(phoneAreaCode).append(", phoneNumber=")
				.append(phoneNumber).append(", faxCountryCode=")
				.append(faxCountryCode).append(", faxAreaCode=")
				.append(faxAreaCode).append(", faxNumber=").append(faxNumber)
				.append(", email=").append(email).append(", createDate=")
				.append(createDate).append(", deleted=").append(deleted)
				.append(", include=").append(include).append(", type=")
				.append(type).append(", contactTypeId=").append(contactTypeId)
				.append(", contactTypeDescr=").append(contactTypeDescr)
				.append("]");
		return builder.toString();
	}
}
