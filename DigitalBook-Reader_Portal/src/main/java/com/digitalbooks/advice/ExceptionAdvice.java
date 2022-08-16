package com.digitalbooks.advice;

import java.util.logging.ErrorManager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.digitalbooks.Exception.DigitalBookException;
import com.digitalbooks.Models.ErrorMessage;

@ControllerAdvice
public class ExceptionAdvice {
	
	@ExceptionHandler(DigitalBookException.class)
	public ResponseEntity<?> handleEx(DigitalBookException e) {
		System.out.println(e.getMessage());
		return new ResponseEntity<ErrorMessage>(
			new ErrorMessage(e.getMessage(), e.getClass().toString()), 
			HttpStatus.OK
		);
	}
	
}
