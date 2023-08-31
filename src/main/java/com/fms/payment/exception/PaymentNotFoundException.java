package com.fms.payment.exception;

@SuppressWarnings("serial")
public class PaymentNotFoundException extends Exception{
	public PaymentNotFoundException(String msg) {
		super(msg);
	}
}
