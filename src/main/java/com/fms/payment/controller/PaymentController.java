package com.fms.payment.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fms.payment.DTO.PaymentDTO;
import com.fms.payment.entity.CardPayment;
import com.fms.payment.entity.Payment;
import com.fms.payment.entity.Payment.PaymentStatus;
import com.fms.payment.entity.UPIPayment;
import com.fms.payment.exception.BookingNotFoundException;
import com.fms.payment.exception.NoPaymentDoneException;
import com.fms.payment.exception.PaymentAlreadyExistsException;
import com.fms.payment.exception.PaymentNotFoundException;
import com.fms.payment.service.PaymentService;

@RestController
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
	@PostMapping("/makeCardPayment/{bookingId}")
	ResponseEntity<String> addPayment(@Valid @RequestBody CardPayment card, @PathVariable long bookingId) throws PaymentAlreadyExistsException, BookingNotFoundException{
		
		logger.info("Received request to add a card payment: {}", card);
		paymentService.makeCardPaymentForBooking(card, bookingId);
		logger.info("Card payment added to given booking: {}", card);
		return ResponseEntity.status(HttpStatus.CREATED).body("Payment done!");
	}

	
	@PostMapping("/makeUPIPayment/{bookingId}")
	ResponseEntity<String> addPayment2(@Valid @RequestBody UPIPayment upi, @PathVariable long bookingId) throws PaymentAlreadyExistsException, BookingNotFoundException{
		
		logger.info("Received request to add an upi payment: {}", upi);
		paymentService.makeUPIPaymentForBooking(upi, bookingId);
		logger.info("Card payment added to given booking: {}", upi);
		return ResponseEntity.status(HttpStatus.CREATED).body("Payment done!");
	}
	
	
	@GetMapping("/getPaymentById/{paymentId}")
	ResponseEntity<PaymentDTO> getPaymentDetails(@PathVariable long paymentId) {
		 try {
			 	logger.info("Received request to fetch details for payment with id: {}", paymentId);
			 	PaymentDTO paymentDTO = paymentService.getPaymentById(paymentId);
			 	logger.info("Payment details fetched successfully for payment with number: {}", paymentId);
			 	return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
		    } catch (PaymentNotFoundException e) {
		        logger.warn("Payment with number {} not found", paymentId);
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		    }
	}
	
	
	@GetMapping("/getPaymentByBookingId/{bookingId}")
	ResponseEntity<PaymentDTO> getPaymentDetails2(@PathVariable long bookingId){
		try {
		 	logger.info("Received request to fetch details for payment with BookingId: {}", bookingId);
		 	PaymentDTO paymentDTO = paymentService.getPaymentByBookingId(bookingId);
		 	logger.info("Payment details fetched successfully for payment with BookingId: {}", bookingId);
		 	return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
	    } catch (PaymentNotFoundException e) {
	        logger.warn("Payment with bookingId {} not found", bookingId);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	}
	
//	@GetMapping("/getPaymentByMobileNumber/{mobileNumber}")
//	ResponseEntity<PaymentDTO> getPaymentDetails3(@PathVariable String mobileNumber) throws PaymentNotFoundException {
//		PaymentDTO paymentDTO = paymentService.getPaymentByMobileNumber(mobileNumber);
//		return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
//	}
	
	@PutMapping("/updatePayment/{bookingId}")
	ResponseEntity<String> refundPayment(@PathVariable long bookingId , @RequestBody PaymentStatus status){
		
		try {
			logger.info("Received request to modify payment with bookingId: {}", bookingId);
			paymentService.refundForCancelledBooking(bookingId, status);
			logger.info("Payment with bookingId {} modified successfully", bookingId);
			return ResponseEntity.status(HttpStatus.OK).body("Updated!");
		}catch(PaymentNotFoundException | BookingNotFoundException e) {
			logger.warn("Payment with bookingId {} not found for modification", bookingId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment Not Found");
		}
	}
	
	
	@DeleteMapping("/deletePayment/{paymentId}")
	ResponseEntity<String> deletePayment(@PathVariable long paymentId){
		try {
			logger.info("Received request to remove payment with number: {}", paymentId);
			paymentService.deletePayment(paymentId);
			logger.info("Payment with number {} removed successfully", paymentId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted!");
		}catch (PaymentNotFoundException e) {
			logger.warn("Payment with number {} not found for removal", paymentId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment Not Found");
		}
	}

	
	@GetMapping("/payments")
	ResponseEntity<List<PaymentDTO>> getAllFlights() throws NoPaymentDoneException{
		
		logger.info("Received request to fetch all payments");
		List<PaymentDTO> payments = paymentService.findAllPayments();
		 logger.info("Fetched {} payments", payments.size());
		return ResponseEntity.status(HttpStatus.OK).body(payments);
	}
	
	
}
