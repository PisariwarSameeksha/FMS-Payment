package com.fms.payment.service;

import java.util.List;

import com.fms.payment.entity.CardPayment;
import com.fms.payment.entity.Payment;
import com.fms.payment.entity.UPIPayment;
import com.fms.payment.exception.BookingNotFoundException;
import com.fms.payment.exception.NoPaymentDoneException;
import com.fms.payment.exception.PaymentAlreadyExistsException;
import com.fms.payment.exception.PaymentNotFoundException;
import com.fms.payment.simplePojo.PaymentDTO;


public interface PaymentService {
	
	String makeCardPaymentForBooking(CardPayment card,Long bookingId)throws PaymentAlreadyExistsException, BookingNotFoundException;
	
	String makeUPIPaymentForBooking(UPIPayment upi,Long bookingId)throws PaymentAlreadyExistsException, BookingNotFoundException;

	PaymentDTO getPaymentById(Long paymentId)throws PaymentNotFoundException;
	
	PaymentDTO getPaymentByBookingId(Long bookingId) throws PaymentNotFoundException;
	
	String modifyPaymentByBookingId(Long bookingId,Payment pay) throws PaymentNotFoundException, BookingNotFoundException;

	String refundForCancelledBooking(Long bookingId)throws BookingNotFoundException,PaymentNotFoundException;
	
	String deletePayment(Long paymentId)throws PaymentNotFoundException;
	
	List<PaymentDTO> findAllPayments() throws NoPaymentDoneException;
	
}
