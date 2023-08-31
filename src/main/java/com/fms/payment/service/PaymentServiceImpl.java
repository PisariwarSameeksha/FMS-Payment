package com.fms.payment.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fms.payment.dao.PaymentRepository;
import com.fms.payment.entity.CardPayment;
import com.fms.payment.entity.Payment;
import com.fms.payment.entity.Payment.ModeOfPayment;
import com.fms.payment.entity.Payment.PaymentStatus;
import com.fms.payment.entity.UPIPayment;
import com.fms.payment.exception.BookingNotFoundException;
import com.fms.payment.exception.NoPaymentDoneException;
import com.fms.payment.exception.PaymentAlreadyExistsException;
import com.fms.payment.exception.PaymentNotFoundException;
import com.fms.payment.simplePojo.BookingDTO;
import com.fms.payment.simplePojo.PaymentDTO;
import com.fms.payment.simplePojo.BookingDTO.BookingStatus;

import reactor.core.publisher.Mono;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private WebClient webclient;
	
	@Override
	public String makeCardPaymentForBooking(CardPayment card, Long bookingId) throws PaymentAlreadyExistsException, BookingNotFoundException {
		if(paymentRepository.findByBookingId(bookingId)!=null) {
			throw new PaymentAlreadyExistsException("Payment already done for the given booking");
		}
		Mono<BookingDTO> response = webclient.get()
				.uri("http://localhost:8091/api/booking/getBooking/{bookingId}", bookingId)
				.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		. bodyToMono(BookingDTO.class).log();
		
		BookingDTO bookingDetails = response.block();
	
		if(bookingDetails == null) {
			throw new BookingNotFoundException("Booking not found for bookingId"+bookingId);
		}
		if(bookingDetails.getBookingStatus()== BookingStatus.CANCELLED) {
	        throw new BookingNotFoundException("Payment cannot be done for cancelled booking.");
	    }
        Payment payment = new Payment();
       
        payment.setTxId(UUID.randomUUID().toString());
        payment.setType(ModeOfPayment.CARD);
        payment.setCard(card);
        payment.setUpi(null);
        payment.setAmount(bookingDetails.getTicketCost());
        payment.setStatus(PaymentStatus.PAID);
        payment.setBookingId(bookingDetails.getBookingId());
        paymentRepository.save(payment);
		return "Payment done successfully!";
	}
	
	@Override
	public String makeUPIPaymentForBooking(UPIPayment upi, Long bookingId) throws PaymentAlreadyExistsException, BookingNotFoundException {
		if(paymentRepository.findByBookingId(bookingId)!=null) {
			throw new PaymentAlreadyExistsException("Payment already done for the given booking");
		}
		Mono<BookingDTO> response = webclient.get()
				.uri("http://localhost:8091/api/booking/getBooking/{bookingId}",bookingId)
				.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		. bodyToMono(BookingDTO.class).log();
		
		BookingDTO bookingDetails = response.block();
		if(bookingDetails == null) {
			throw new BookingNotFoundException("Booking not found for bookingId"+bookingId);
		}
        if(bookingDetails.getBookingStatus()== BookingStatus.CANCELLED) {
        	throw new BookingNotFoundException("Payment cannot be done for cancelled booking.");
        }
        Payment payment = new Payment();
        payment.setTxId(UUID.randomUUID().toString());
        payment.setType(ModeOfPayment.UPI);
        payment.setCard(null);
        payment.setUpi(upi);
        payment.setAmount(bookingDetails.getTicketCost());
        payment.setStatus(PaymentStatus.PAID);
        payment.setBookingId(bookingDetails.getBookingId());
        paymentRepository.save(payment);
		return "Payment done successfully!";
	}

	@Override
	public PaymentDTO getPaymentById(Long paymentId) throws PaymentNotFoundException {
		Optional<Payment> optPayment = this.paymentRepository.findById(paymentId);
		if(optPayment.isEmpty()) {
			throw new PaymentNotFoundException("Payment does not exist.");
		}
		Payment payment = optPayment.get();
		PaymentDTO paymentDTO = modelMapper.map(payment,PaymentDTO.class);
		return paymentDTO;
	}
	
	@Override
	public PaymentDTO getPaymentByBookingId(Long bookingId) throws PaymentNotFoundException {
		Optional<Payment> optPayment = Optional.ofNullable(paymentRepository.findByBookingId(bookingId));
		if(optPayment.isEmpty()) {
			throw new PaymentNotFoundException("Payment does not exist.");
		}
		Payment payment = optPayment.get();
		PaymentDTO paymentDTO = modelMapper.map(payment, PaymentDTO.class);
		return paymentDTO;
	}

	@Override
	public String modifyPaymentByBookingId(Long bookingId, Payment pay) throws PaymentNotFoundException, BookingNotFoundException {
		Mono<BookingDTO> response = webclient.get()
				.uri("http://localhost:8091/api/booking/getBooking/{bookingId}",bookingId)
				.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		. bodyToMono(BookingDTO.class).log();
        BookingDTO bookingDetails = response.block();
        if(bookingDetails == null) {
        	throw new BookingNotFoundException("Booking not found for given BookingId" +bookingId);
        }
        Payment paymentToBeUpdated = this.paymentRepository.findByBookingId(bookingId);
        if(paymentToBeUpdated == null) {
        	throw new PaymentNotFoundException("Payment not found");
        }
        paymentToBeUpdated.setType(pay.getType());
        if(pay.getType()== ModeOfPayment.CARD) {
        	paymentToBeUpdated.setCard(pay.getCard());
        }
        if(pay.getType()== ModeOfPayment.UPI) {
        	paymentToBeUpdated.setUpi(pay.getUpi());
        }
        paymentToBeUpdated.setAmount(bookingDetails.getTicketCost());
        paymentToBeUpdated.setStatus(pay.getStatus());
        this.paymentRepository.save(paymentToBeUpdated);
        return "Payment updated successfully!";
	}

	@Override
	public String deletePayment(Long paymentId) throws PaymentNotFoundException {
		Optional<Payment> paymentToBeDeleted = paymentRepository.findById(paymentId);
		if (paymentToBeDeleted.isPresent()) {
			paymentRepository.deleteById(paymentId);
			return "Payment deleted Successfully";
		} else {
			throw new PaymentNotFoundException("Payment with id " +paymentId+" is not found");
		}
	}
	
	@Override
	public List<PaymentDTO> findAllPayments()throws NoPaymentDoneException{
		List<Payment> payments= paymentRepository.findAll();
		if(payments.isEmpty()) {
			throw new NoPaymentDoneException("No Payments Found");
		}
		List<PaymentDTO> paymentDTO = payments.stream().map(payment->modelMapper.map(payment,PaymentDTO.class)).collect(Collectors.toList());
		return paymentDTO;
	}

	@Override
	public String refundForCancelledBooking(Long bookingId) throws BookingNotFoundException, PaymentNotFoundException {
		Payment paymentToBeUpdated = this.paymentRepository.findByBookingId(bookingId);
        if(paymentToBeUpdated == null) {
        	throw new PaymentNotFoundException("Payment not found");
        }
		Mono<BookingDTO> response = webclient.get()
				.uri("http://localhost:8091/api/booking/getBooking/{bookingId}",bookingId)
				.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		. bodyToMono(BookingDTO.class).log();
        BookingDTO bookingDetails = response.block();
        if(bookingDetails == null) {
			throw new BookingNotFoundException("Booking not found for bookingId"+bookingId);
		}
        if(bookingDetails.getBookingStatus() != BookingStatus.CANCELLED) {
        	throw new BookingNotFoundException("Refund can be done only for cancelled booking.");
        }
        paymentToBeUpdated.setStatus(PaymentStatus.REFUNDED);
        this.paymentRepository.save(paymentToBeUpdated);
        return "Amount Refunded Successfully!";
	}
}