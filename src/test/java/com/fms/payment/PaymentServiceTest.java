package com.fms.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
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
import com.fms.payment.service.PaymentServiceImpl;

class PaymentServiceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@InjectMocks
	private PaymentServiceImpl paymentService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	@Mock
	private ModelMapper modelMapper;
	
	@AfterEach
	void tearDown() {
		Mockito.reset(paymentRepository, modelMapper);
	}
	
//	@Test
//	void testMakeCardPaymentForBooking_Success() throws PaymentAlreadyExistsException, BookingNotFoundException {
//		long bookingId = 12345;
//		Booking validBooking = new Booking();
//		validBooking.setBookingId(bookingId);
//		validBooking.setBookingStatus(BookingStatus.BOOKED);
//		validBooking.setTicketCost(100.0);
//
//		ResponseEntity<Booking> validBookingResponse = new ResponseEntity<>(validBooking, HttpStatus.OK);
//		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.isNull(),
//				Mockito.eq(Booking.class), Mockito.eq(bookingId))).thenReturn(validBookingResponse);
//
//		CardPayment card = new CardPayment();
//		String result = paymentService.makeCardPaymentForBooking(card, bookingId);
//
//		Mockito.verify(paymentRepository, Mockito.times(1)).save(Mockito.any(Payment.class));
//		assertEquals("Payment done successfully!", result);
//	
//	}
//	
//	@Test
//    void testMakeCardPaymentForBooking_BookingNotFound() throws BookingNotFoundException {
//        long bookingId = 1L;
//
//        when(restTemplate.exchange(
//                anyString(),
//                any(),
//                any(),
//                eq(Booking.class),
//                eq(bookingId)
//        )).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//
//        assertThrows(BookingNotFoundException.class, () -> {
//            paymentService.makeCardPaymentForBooking(new CardPayment(), bookingId);
//        });
//    }
//
//	@Test
//	void testMakeUPIPaymentForBooking_Success() throws PaymentAlreadyExistsException, BookingNotFoundException {
//		long bookingId = 12345;
//		Booking validBooking = new Booking();
//		validBooking.setBookingId(bookingId);
//		validBooking.setBookingStatus(BookingStatus.BOOKED);
//		validBooking.setTicketCost(100.0);
//
//		ResponseEntity<Booking> validBookingResponse = new ResponseEntity<>(validBooking, HttpStatus.OK);
//		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.isNull(),
//				Mockito.eq(Booking.class), Mockito.eq(bookingId))).thenReturn(validBookingResponse);
//
//		UPIPayment upi = new UPIPayment();
//		String result = paymentService.makeUPIPaymentForBooking(upi, bookingId);
//
//		Mockito.verify(paymentRepository, Mockito.times(1)).save(Mockito.any(Payment.class));
//		assertEquals("Payment done successfully!", result);
//		
//	}
//	
//	@Test
//    void testMakeUPIPaymentForBooking_BookingNotFound() throws BookingNotFoundException {
//        long bookingId = 1L;
//
//        when(restTemplate.exchange(
//                anyString(),
//                any(),
//                any(),
//                eq(Booking.class),
//                eq(bookingId)
//        )).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//
//        assertThrows(BookingNotFoundException.class, () -> {
//            paymentService.makeUPIPaymentForBooking(new UPIPayment(), bookingId);
//        });
//    }
//    
	@Test
	void testDeletePayment_Success() throws PaymentNotFoundException {
		long paymentId = 123L;
		Payment existingPayment = new Payment();
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(existingPayment));
		String result = paymentService.deletePayment(paymentId);
		assertEquals("Payment deleted Successfully", result);
		Mockito.verify(paymentRepository, Mockito.times(1)).deleteById(paymentId);
	}
	
	@Test
	void testDeletePayment_PaymentNotFoundException() {
		long paymentId = 123L;
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());
		assertThrows(PaymentNotFoundException.class, () -> paymentService.deletePayment(paymentId));
		Mockito.verify(paymentRepository, never()).deleteById(paymentId);
	}
	
	@Test
	void testFindAllPayments_Success() throws NoPaymentDoneException {
		List<Payment> paymentList = new ArrayList<>();
		paymentList.add(new Payment());
		paymentList.add(new Payment());
		when(paymentRepository.findAll()).thenReturn(paymentList);
		PaymentDTO paymentDTO1 = new PaymentDTO();
		PaymentDTO paymentDTO2 = new PaymentDTO();
		when(modelMapper.map(paymentList.get(0), PaymentDTO.class)).thenReturn(paymentDTO1);
		when(modelMapper.map(paymentList.get(1), PaymentDTO.class)).thenReturn(paymentDTO2);
		List<PaymentDTO> result = paymentService.findAllPayments();
		assertEquals(2, result.size());
		try {
			assertEquals(paymentDTO1, result.get(0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(paymentDTO2, result.get(1));
	}
	
	@Test
    void testFindAllPayments_NoPaymentDoneException() {
        when(paymentRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoPaymentDoneException.class,
               () -> paymentService.findAllPayments());
    }
	
	@Test
	void testGetPaymentById_Success() throws PaymentNotFoundException {
		long paymentId = 1L;
		Payment payment = new Payment();
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
		PaymentDTO expectedPaymentDTO = new PaymentDTO();
		when(modelMapper.map(payment, PaymentDTO.class)).thenReturn(expectedPaymentDTO);
		PaymentDTO result = paymentService.getPaymentById(paymentId);
		assertEquals(expectedPaymentDTO, result);
	}
	
	@Test
	void testGetPaymentById_PaymentNotFoundException() {
		long paymentId = 12L;
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());
		assertThrows(PaymentNotFoundException.class, () -> paymentService.getPaymentById(paymentId));
		Mockito.verify(modelMapper, never()).map(any(), any());
	}
	
	
}
