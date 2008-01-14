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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="partner_range"
    ,schema="public"
)
public class PartnerRange  implements java.io.Serializable {


     private int id;
     private Integer partnerId;
     private Double percentageRate;
     private Double referralFee;
     private int rangeFrom;
     private int rangeTo;

    public PartnerRange() {
    }

	
    public PartnerRange(int id, int rangeFrom, int rangeTo) {
        this.id = id;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
    }
    public PartnerRange(int id, Integer partnerId, Double percentageRate, Double referralFee, int rangeFrom, int rangeTo) {
       this.id = id;
       this.partnerId = partnerId;
       this.percentageRate = percentageRate;
       this.referralFee = referralFee;
       this.rangeFrom = rangeFrom;
       this.rangeTo = rangeTo;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="partner_id")
    public Integer getPartnerId() {
        return this.partnerId;
    }
    
    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }
    
    @Column(name="percentage_rate", precision=17, scale=17)
    public Double getPercentageRate() {
        return this.percentageRate;
    }
    
    public void setPercentageRate(Double percentageRate) {
        this.percentageRate = percentageRate;
    }
    
    @Column(name="referral_fee", precision=17, scale=17)
    public Double getReferralFee() {
        return this.referralFee;
    }
    
    public void setReferralFee(Double referralFee) {
        this.referralFee = referralFee;
    }
    
    @Column(name="range_from", nullable=false)
    public int getRangeFrom() {
        return this.rangeFrom;
    }
    
    public void setRangeFrom(int rangeFrom) {
        this.rangeFrom = rangeFrom;
    }
    
    @Column(name="range_to", nullable=false)
    public int getRangeTo() {
        return this.rangeTo;
    }
    
    public void setRangeTo(int rangeTo) {
        this.rangeTo = rangeTo;
    }




}


