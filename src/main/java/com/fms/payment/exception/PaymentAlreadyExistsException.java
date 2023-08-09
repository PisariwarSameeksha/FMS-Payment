package com.fms.payment.exception;

public class PaymentAlreadyExistsException extends Exception{
	public PaymentAlreadyExistsException(String msg) {
		super(msg);
	}
}
