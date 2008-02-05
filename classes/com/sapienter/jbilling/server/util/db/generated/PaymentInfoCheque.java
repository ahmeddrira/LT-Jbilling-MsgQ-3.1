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
@Table(name="payment_info_cheque")
public class PaymentInfoCheque  implements java.io.Serializable {


     private int id;
     private Payment payment;
     private String bank;
     private String chequeNumber;
     private Date chequeDate;

    public PaymentInfoCheque() {
    }

	
    public PaymentInfoCheque(int id) {
        this.id = id;
    }
    public PaymentInfoCheque(int id, Payment payment, String bank, String chequeNumber, Date chequeDate) {
       this.id = id;
       this.payment = payment;
       this.bank = bank;
       this.chequeNumber = chequeNumber;
       this.chequeDate = chequeDate;
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
    
    @Column(name="bank", length=50)
    public String getBank() {
        return this.bank;
    }
    
    public void setBank(String bank) {
        this.bank = bank;
    }
    
    @Column(name="cheque_number", length=50)
    public String getChequeNumber() {
        return this.chequeNumber;
    }
    
    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="cheque_date", length=13)
    public Date getChequeDate() {
        return this.chequeDate;
    }
    
    public void setChequeDate(Date chequeDate) {
        this.chequeDate = chequeDate;
    }




}


