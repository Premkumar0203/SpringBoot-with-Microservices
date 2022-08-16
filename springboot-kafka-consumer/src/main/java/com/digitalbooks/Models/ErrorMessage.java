package com.digitalbooks.Models;

import java.time.LocalDateTime;

public class ErrorMessage {

	private String message;
	private String type;
	
	private LocalDateTime now = LocalDateTime.now();
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LocalDateTime getNow() {
		return now;
	}
	public void setNow(LocalDateTime now) {
		this.now = now;
	}
	@Override
	public String toString() {
		return "ErrorMessage [message=" + message + ", type=" + type + ", now=" + now + "]";
	}
	public ErrorMessage(String message, String type) {
		super();
		this.message = message;
		this.type = type;
	}
	public ErrorMessage(String message, String type, LocalDateTime now) {
		super();
		this.message = message;
		this.type = type;
		this.now = now;
	}
	
	
	
	
}
