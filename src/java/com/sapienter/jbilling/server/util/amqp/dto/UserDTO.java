package com.sapienter.jbilling.server.util.amqp.dto;

import java.math.BigDecimal;

import com.sapienter.jbilling.server.util.amqp.types.AccountStatus;
import com.sapienter.jbilling.server.util.amqp.types.BalanceType;

public class UserDTO {
	private Integer id;
	private String userName;
	private String companyName;
	private AccountStatus accountStatus;
	private Integer currencyId;
	private BalanceType balanceType;
	private BigDecimal balance;
	private BigDecimal dynamicBalance;
	private BigDecimal creditLimit;
	private ContactDTO contact;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public BalanceType getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(BalanceType balanceType) {
		this.balanceType = balanceType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getDynamicBalance() {
		return dynamicBalance;
	}

	public void setDynamicBalance(BigDecimal dynamicBalance) {
		this.dynamicBalance = dynamicBalance;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public ContactDTO getContact() {
		return contact;
	}

	public void setContact(ContactDTO contact) {
		this.contact = contact;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDTO [id=").append(id).append(", userName=")
				.append(userName).append(", companyName=").append(companyName)
				.append(", accountStatus=").append(accountStatus)
				.append(", currencyId=").append(currencyId)
				.append(", balanceType=").append(balanceType)
				.append(", balance=").append(balance)
				.append(", dynamicBalance=").append(dynamicBalance)
				.append(", creditLimit=").append(creditLimit)
				.append(", contact=").append(contact).append("]");
		return builder.toString();
	}
}
