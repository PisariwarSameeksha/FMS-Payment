package com.fms.payment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fms.payment.dao.PaymentRepository;
import com.fms.payment.entity.Payment;
import com.fms.payment.exception.PaymentException;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Override
	public Payment addPayment(Payment payment) throws PaymentException {
		
		return paymentRepository.save(payment);
	}
	
	@Override
	public Payment getPaymentById(long paymentId) throws PaymentException {
		Optional<Payment> existingPayment = paymentRepository.findById(paymentId);
		if(existingPayment.isPresent()) {
			return existingPayment.get();
		}else {
			throw new PaymentException("Payment with id " +paymentId+ " was not found");
		}
	}

	@Override
	public Payment getPaymentByMobileNumber(String mobileNumber) throws PaymentException {
		Optional<Payment> existingPayment = Optional.ofNullable(paymentRepository.findByMobileNumber(mobileNumber));
		if(existingPayment.isPresent()) {
			return existingPayment.get();
		}else {
			throw new PaymentException("Payment with id " +mobileNumber+ " was not found");
		}
	}

	@Override
	public Payment deletePayment(long paymentId) throws PaymentException {
		Optional<Payment> PaymentToBeDeleted = paymentRepository.findById(paymentId);
		paymentRepository.deleteById(paymentId);

		if (PaymentToBeDeleted.isPresent()) {
			return PaymentToBeDeleted.get();
		} else {
			throw new PaymentException("Payment with id " +paymentId+" is not found");
		}
	}

	@Override
	public Payment updatePayment(long paymentId, Payment payment) throws PaymentException {
		if (paymentRepository.existsById(paymentId)) {
			Payment paymentToBeUpdated = paymentRepository.findById(paymentId).get();
			paymentToBeUpdated.setStatus(payment.getStatus());
			
			paymentRepository.save(paymentToBeUpdated);
			return paymentToBeUpdated;
		}
		else {
			throw new PaymentException("Payment with id "+paymentId+" is not found,so can't update");
		}
	}
	
	
	@Override
	public List<Payment> findAllPayments() throws PaymentException {
		List<Payment> list=paymentRepository.findAll();
		if(list.isEmpty()) {
			throw new PaymentException("No Payments found in database");
		}
		return list;
	}

//	@Override
//	public Payment addPaymentToBooking(Payment payment, long bookingId) throws PaymentException {
//		Optional<Booking> existingBooking=BookingRepository.findById(bookingId);
//		if(existingBooking.isEmpty()) {
//			throw new PaymentException("Booking with id "+bookingId+" is not found");
//		}
//		Booking foundBooking=existingBooking.get();
//		Payment newPayment=addPayment(payment);
//		foundBooking.setPayment(newPayment);
//		bookingRepository.save(foundBooking);
//		return payment;
//	}

}
