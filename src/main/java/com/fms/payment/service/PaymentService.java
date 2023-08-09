package com.fms.payment.service;

import java.util.List;

import com.fms.payment.DTO.PaymentDTO;
import com.fms.payment.entity.CardPayment;
import com.fms.payment.entity.Payment.PaymentStatus;
import com.fms.payment.entity.UPIPayment;
import com.fms.payment.exception.BookingNotFoundException;
import com.fms.payment.exception.NoPaymentDoneException;
import com.fms.payment.exception.PaymentAlreadyExistsException;
import com.fms.payment.exception.PaymentNotFoundException;


public interface PaymentService {
	
	public String makeCardPaymentForBooking(CardPayment card,long bookingId)throws PaymentAlreadyExistsException, BookingNotFoundException;
	
	public String makeUPIPaymentForBooking(UPIPayment upi,long bookingId)throws PaymentAlreadyExistsException, BookingNotFoundException;

	public PaymentDTO getPaymentById(long paymentId)throws PaymentNotFoundException;
	
//	public PaymentDTO getPaymentByMobileNumber(String mobileNumber)throws PaymentNotFoundException;
	
	public PaymentDTO getPaymentByBookingId(long bookingId) throws PaymentNotFoundException;
	
	public String refundForCancelledBooking(long bookingId,PaymentStatus status) throws PaymentNotFoundException, BookingNotFoundException;
	
	public String deletePayment(long paymentId)throws PaymentNotFoundException;
	
	public List<PaymentDTO> findAllPayments() throws NoPaymentDoneException;
	
}
