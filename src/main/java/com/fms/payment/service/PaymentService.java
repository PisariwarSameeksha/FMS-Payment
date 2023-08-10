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
	
	String makeCardPaymentForBooking(CardPayment card,long bookingId)throws PaymentAlreadyExistsException, BookingNotFoundException;
	
	String makeUPIPaymentForBooking(UPIPayment upi,long bookingId)throws PaymentAlreadyExistsException, BookingNotFoundException;

	PaymentDTO getPaymentById(long paymentId)throws PaymentNotFoundException;
	
	PaymentDTO getPaymentByBookingId(long bookingId) throws PaymentNotFoundException;
	
//	String modifyPaymentByBookingId(long bookingId,PaymentDTO pay) throws PaymentNotFoundException, BookingNotFoundException;
	
	String deletePayment(long paymentId)throws PaymentNotFoundException;
	
	List<PaymentDTO> findAllPayments() throws NoPaymentDoneException;
	
}
