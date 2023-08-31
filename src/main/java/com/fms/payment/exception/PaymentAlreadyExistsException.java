package com.fms.payment.exception;

@SuppressWarnings("serial")
public class PaymentAlreadyExistsException extends Exception{
	public PaymentAlreadyExistsException(String msg) {
		super(msg);
	}
}
