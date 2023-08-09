package com.fms.payment.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fms.booking.entity.Booking;
import com.fms.booking.entity.Booking.BookingStatus;
import com.fms.payment.DTO.PaymentDTO;
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

@Service
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	private String bookingMicroserviceUrl = "http://localhost:8091";
	
	@Override
	public String makeCardPaymentForBooking(CardPayment card, long bookingId) throws PaymentAlreadyExistsException, BookingNotFoundException {
		if(paymentRepository.findByBookingId(bookingId)!=null) {
			throw new PaymentAlreadyExistsException("Payment already done for the given booking");
		}
		ResponseEntity<Booking> response = restTemplate.exchange(
				bookingMicroserviceUrl + "/validateBooking/{bookingId}",
	            HttpMethod.GET,
	            null,
	            Booking.class,
	            bookingId
	        );
		if (response.getStatusCode().is2xxSuccessful()) {
            Booking bookingDetails = response.getBody();
            if((bookingDetails!=null) && (bookingDetails.getBookingStatus()!= BookingStatus.CANCELLED)){
            	Payment payment = new Payment();
        		payment.setTxId(UUID.randomUUID().toString());
            	payment.setType(ModeOfPayment.CARD);
        		payment.setCard(card);
        		payment.setUpi(null);
        		payment.setAmount(bookingDetails.getTicketCost());
        		payment.setStatus(PaymentStatus.PAID);
        		payment.setBookingId(bookingDetails.getBookingId());
        		paymentRepository.save(payment);
        	}
            else {
            	throw new PaymentAlreadyExistsException("Payment can't be done for cancelled/null booking");
            }
		}
		else {
			throw new BookingNotFoundException("Booking not found for bookingId"+bookingId);
		}
		return "Payment done successfully!";
	}

	@Override
	public String makeUPIPaymentForBooking(UPIPayment upi, long bookingId) throws PaymentAlreadyExistsException, BookingNotFoundException {
		if(paymentRepository.findByBookingId(bookingId)!=null) {
			throw new PaymentAlreadyExistsException("Payment already done for the given booking");
		}
		ResponseEntity<Booking> response = restTemplate.exchange(
				bookingMicroserviceUrl + "/validateBooking/{bookingId}",
	            HttpMethod.GET,
	            null,
	            Booking.class,
	            bookingId
	        );
		if (response.getStatusCode().is2xxSuccessful()) {
            Booking bookingDetails = response.getBody();
            if((bookingDetails!=null) && (bookingDetails.getBookingStatus()!= BookingStatus.CANCELLED)) {
            	Payment payment = new Payment();
        		payment.setTxId(UUID.randomUUID().toString());
            	payment.setType(ModeOfPayment.UPI);
        		payment.setCard(null);
        		payment.setUpi(upi);
        		payment.setAmount(bookingDetails.getTicketCost());
        		payment.setStatus(PaymentStatus.PAID);
        		payment.setBookingId(bookingDetails.getBookingId());
        		paymentRepository.save(payment);
            }
            else {
            	throw new PaymentAlreadyExistsException("Payment can't be done for cancelled/null booking");
            }
		}
		else {
			throw new BookingNotFoundException("Booking not found for bookingId"+bookingId);
		}
		return "Payment done successfully!";
	}

	@Override
	public PaymentDTO getPaymentById(long paymentId) throws PaymentNotFoundException {
		Optional<Payment> optPayment = this.paymentRepository.findById(paymentId);
		if(optPayment.isEmpty()) {
			throw new PaymentNotFoundException("Payment does not exist.");
		}
		Payment payment = optPayment.get();
		PaymentDTO paymentDTO = modelMapper.map(payment,PaymentDTO.class);
		return paymentDTO;
	}

//	@Override
//	public PaymentDTO getPaymentByMobileNumber(String mobileNumber) throws PaymentNotFoundException {
//		Optional<Payment> optPayment = Optional.ofNullable(paymentRepository.findByMobileNumber(mobileNumber));
//		if(optPayment.isEmpty()) {
//			throw new PaymentNotFoundException("Payment does not exist.");
//		}
//		Payment payment = optPayment.get();
//		PaymentDTO paymentDTO = modelMapper.map(payment, PaymentDTO.class);
//		return paymentDTO;
//	}
	
	@Override
	public PaymentDTO getPaymentByBookingId(long bookingId) throws PaymentNotFoundException {
		Optional<Payment> optPayment = Optional.ofNullable(paymentRepository.findByBookingId(bookingId));
		if(optPayment.isEmpty()) {
			throw new PaymentNotFoundException("Payment does not exist.");
		}
		Payment payment = optPayment.get();
		PaymentDTO paymentDTO = modelMapper.map(payment, PaymentDTO.class);
		return paymentDTO;
	}

	@Override
	public String refundForCancelledBooking(long bookingId, PaymentStatus status) throws PaymentNotFoundException, BookingNotFoundException {
		ResponseEntity<Booking> response = restTemplate.exchange(
				bookingMicroserviceUrl + "/validateBooking/{bookingId}",
	            HttpMethod.GET,
	            null,
	            Booking.class,
	            bookingId
	        );
		if (response.getStatusCode().is2xxSuccessful()) {
            Booking bookingDetails = response.getBody();
            if((bookingDetails!=null) && (bookingDetails.getBookingStatus()== BookingStatus.CANCELLED)) {
            	Payment payment = paymentRepository.findByBookingId(bookingId);
            	payment.setStatus(status);
            	paymentRepository.save(payment);
            }
            else {
            	throw new PaymentNotFoundException("payment not done for booking/ booking is not cancelled");
            }
		}
		else {
			throw new BookingNotFoundException("booking not done for given bookingId" +bookingId);
		}
		return "Amount refunded successfully";
	}

	@Override
	public String deletePayment(long paymentId) throws PaymentNotFoundException {
		Optional<Payment> PaymentToBeDeleted = paymentRepository.findById(paymentId);
		if (PaymentToBeDeleted.isPresent()) {
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
}
