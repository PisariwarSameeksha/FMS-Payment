package com.fms.payment.service;

import java.util.List;
import com.fms.payment.entity.Payment;
import com.fms.payment.exception.PaymentException;


public interface PaymentService {
	
	public Payment addPayment(Payment payment)throws PaymentException;

	public Payment getPaymentById(long paymentId)throws PaymentException;
	
	public Payment getPaymentByMobileNumber(String mobileNumber)throws PaymentException;
	
	public Payment deletePayment(long paymentId)throws PaymentException;
	
	public Payment updatePayment(long paymentId, Payment payment)throws PaymentException;
	
	public List<Payment> findAllPayments() throws PaymentException;
	
//	public Payment addPaymentToBooking(Payment payment,long bookingId)throws PaymentException;
}
