
package com.fpoly.entity;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "payment")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private int paymentId;
	
	@ManyToOne
	@JoinColumn(name = "users_id")
	User user;

	@Column(name = "transaction_no")
	private String transactionNo;

	@Column(name = "transaction_status")
	private boolean transactionStatus;

	@Column(name = "banktran_no")
	private String banktranNo;

	@Column(name = "txn_ref")
	private String txnRef;

	@Column(name = "amount")
	private float amount;

	@Column(name = "bank_code")
	private String bankCode;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;
	
	@JsonIgnore
	@OneToMany(mappedBy = "payment")
	List<RegisteredCourse> listRegisteredCourse;

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public boolean isTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(boolean transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getBanktranNo() {
		return banktranNo;
	}

	public void setBanktranNo(String banktranNo) {
		this.banktranNo = banktranNo;
	}

	public String getTxnRef() {
		return txnRef;
	}

	public void setTxnRef(String txnRef) {
		this.txnRef = txnRef;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public List<RegisteredCourse> getListRegisteredCourse() {
		return listRegisteredCourse;
	}

	public void setListRegisteredCourse(List<RegisteredCourse> listRegisteredCourse) {
		this.listRegisteredCourse = listRegisteredCourse;
	}
	

}
