package com.fms.payment.exception;

@SuppressWarnings("serial")
public class NoPaymentDoneException extends Exception{
	public NoPaymentDoneException(String msg) {
		super(msg);
	}

}
