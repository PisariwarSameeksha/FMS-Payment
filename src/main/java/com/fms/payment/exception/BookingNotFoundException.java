package com.fms.payment.exception;

@SuppressWarnings("serial")
public class BookingNotFoundException extends Exception {
	public BookingNotFoundException(String msg) {
		super(msg);
	}
}
