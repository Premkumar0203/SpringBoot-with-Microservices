package com.digitalbooks.entities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BookPurchase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int purchaseId;
	
	@Column(nullable=false)
	private String PaymentId ;
	@Column(nullable=false)
	private int bookId;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String emailId;

	private LocalDateTime date;
	
	public String getPaymentId() {
		return PaymentId;
	}
	public void setPaymentId(String paymentId) {
		this.PaymentId =paymentId;

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
	

}
