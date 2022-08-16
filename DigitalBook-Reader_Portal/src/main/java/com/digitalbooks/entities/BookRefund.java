package com.digitalbooks.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BookRefund
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int refundId;
	
	@Column(nullable=false)
	private String PaymentId ;
	
	@Column(nullable=false)
	private int bookId;
	
	@Column(nullable=false)
	private String name;
	
	public int getRefundId() {
		return refundId;
	}

	public void setRefundId(int refundId) {
		this.refundId = refundId;
	}

	public String getPaymentId() {
		return PaymentId;
	}

	public void setPaymentId(String paymentId) {
		PaymentId = paymentId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Column(nullable=false)
	private String emailId;

	private LocalDateTime date;
	
}
