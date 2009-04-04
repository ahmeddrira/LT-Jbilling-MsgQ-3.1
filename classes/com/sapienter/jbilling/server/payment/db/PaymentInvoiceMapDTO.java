package com.sapienter.jbilling.server.payment.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.invoice.db.InvoiceDTO;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @author abimael
 *
 */
@Entity
@TableGenerator(
        name = "payment_invoice_GEN", 
        table = "jbilling_table", 
        pkColumnName = "name", 
        valueColumnName = "next_id", 
        pkColumnValue = "payment_invoice", 
        allocationSize = 100)
@Table(name = "payment_invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaymentInvoiceMapDTO implements Serializable {

	private int id;
	private PaymentDTO payment;
	private InvoiceDTO invoiceEntity;
	private Float amount;
	private Date createDatetime;
	private int versionNum;
	private static final Logger log = Logger.getLogger(PaymentInvoiceMapDTO.class);
	

	public PaymentInvoiceMapDTO(Integer id2, Float amount2, Date create) {
		
		this.id = id2;
		this.amount = amount2;
		this.createDatetime = create;
	}

	public PaymentInvoiceMapDTO() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "payment_invoice_GEN")
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id")
	public PaymentDTO getPayment() {
		return payment;
	}
	
	public void setPayment(PaymentDTO payment) {
		this.payment = payment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_id")
	public InvoiceDTO getInvoiceEntity() {
		return invoiceEntity;
	}
	
	public void setInvoiceEntity(InvoiceDTO invoiceId) {
		this.invoiceEntity = invoiceId;
	}

	@Column(name = "amount", precision = 17, scale = 17)
	public Float getAmount() {
		return amount;
	}
	
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
	@Column(name = "create_datetime", nullable = false, length = 29)
	public Date getCreateDatetime() {
		return this.createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	@Version
    @Column(name="OPTLOCK")
    public int getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(int versionNum) {
        this.versionNum = versionNum;
    }
}
