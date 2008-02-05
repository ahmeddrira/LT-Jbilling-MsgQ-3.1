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


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="payment_authorization")
public class PaymentAuthorization  implements java.io.Serializable {


     private int id;
     private Payment payment;
     private String processor;
     private String code1;
     private String code2;
     private String code3;
     private String approvalCode;
     private String avs;
     private String transactionId;
     private String md5;
     private Date createDatetime;
     private String cardCode;
     private String responseMessage;

    public PaymentAuthorization() {
    }

	
    public PaymentAuthorization(int id, String processor, String code1, Date createDatetime) {
        this.id = id;
        this.processor = processor;
        this.code1 = code1;
        this.createDatetime = createDatetime;
    }
    public PaymentAuthorization(int id, Payment payment, String processor, String code1, String code2, String code3, String approvalCode, String avs, String transactionId, String md5, Date createDatetime, String cardCode, String responseMessage) {
       this.id = id;
       this.payment = payment;
       this.processor = processor;
       this.code1 = code1;
       this.code2 = code2;
       this.code3 = code3;
       this.approvalCode = approvalCode;
       this.avs = avs;
       this.transactionId = transactionId;
       this.md5 = md5;
       this.createDatetime = createDatetime;
       this.cardCode = cardCode;
       this.responseMessage = responseMessage;
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
    @JoinColumn(name="payment_id")
    public Payment getPayment() {
        return this.payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    @Column(name="processor", nullable=false, length=20)
    public String getProcessor() {
        return this.processor;
    }
    
    public void setProcessor(String processor) {
        this.processor = processor;
    }
    
    @Column(name="code1", nullable=false, length=20)
    public String getCode1() {
        return this.code1;
    }
    
    public void setCode1(String code1) {
        this.code1 = code1;
    }
    
    @Column(name="code2", length=20)
    public String getCode2() {
        return this.code2;
    }
    
    public void setCode2(String code2) {
        this.code2 = code2;
    }
    
    @Column(name="code3", length=20)
    public String getCode3() {
        return this.code3;
    }
    
    public void setCode3(String code3) {
        this.code3 = code3;
    }
    
    @Column(name="approval_code", length=20)
    public String getApprovalCode() {
        return this.approvalCode;
    }
    
    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }
    
    @Column(name="avs", length=20)
    public String getAvs() {
        return this.avs;
    }
    
    public void setAvs(String avs) {
        this.avs = avs;
    }
    
    @Column(name="transaction_id", length=20)
    public String getTransactionId() {
        return this.transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    @Column(name="md5", length=100)
    public String getMd5() {
        return this.md5;
    }
    
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="create_datetime", nullable=false, length=13)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    
    @Column(name="card_code", length=100)
    public String getCardCode() {
        return this.cardCode;
    }
    
    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }
    
    @Column(name="response_message", length=200)
    public String getResponseMessage() {
        return this.responseMessage;
    }
    
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }




}


