package com.digitalbooks.Exception;

public class DigitalBookException extends Exception {

	public DigitalBookException() {
	}

	public DigitalBookException(String m) {
		super(m);
	}

	public DigitalBookException(Exception e) {
		super(e);
	}

	public DigitalBookException(String m, Exception e) {
		super(m, e);
	}

}
