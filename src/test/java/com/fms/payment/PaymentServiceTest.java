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

import com.fms.booking.entity.Booking;
import com.fms.booking.entity.Booking.BookingStatus;
import com.fms.payment.DTO.PaymentDTO;
import com.fms.payment.dao.PaymentRepository;
import com.fms.payment.entity.Payment;
import com.fms.payment.entity.Payment.ModeOfPayment;
import com.fms.payment.entity.Payment.PaymentStatus;
import com.fms.payment.exception.BookingNotFoundException;
import com.fms.payment.exception.NoPaymentDoneException;
import com.fms.payment.exception.PaymentAlreadyExistsException;
import com.fms.payment.exception.PaymentNotFoundException;
import com.fms.payment.service.PaymentServiceImpl;

public class PaymentServiceTest {

	@Mock
	private RestTemplate restTemplate;

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
//	public void testMakeCardPaymentForBooking_Success() throws PaymentAlreadyExistsException, BookingNotFoundException {
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
//		Payment payment = new Payment();
//
//		String result = paymentService.makeCardPaymentForBooking(payment, bookingId);
//
//		Mockito.verify(paymentRepository, Mockito.times(1)).save(Mockito.any(Payment.class));
//		assertEquals("Payment done successfully!", result);
//		assertEquals(ModeOfPayment.CARD, payment.getType());
//		assertEquals(PaymentStatus.PAID, payment.getStatus());
//		assertNotNull(payment.getTxId());
//	}
	
//	@Test
//    public void testMakeCardPaymentForBooking_BookingNotFound() throws BookingNotFoundException {
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
//            paymentService.makeCardPaymentForBooking(new Payment(), bookingId);
//        });
//    }
//
//	@Test
//	public void testMakeUPIPaymentForBooking_Success() throws PaymentAlreadyExistsException, BookingNotFoundException {
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
//		Payment payment = new Payment();
//
//		String result = paymentService.makeUPIPaymentForBooking(payment, bookingId);
//
//		Mockito.verify(paymentRepository, Mockito.times(1)).save(Mockito.any(Payment.class));
//		assertEquals("Payment done successfully!", result);
//		assertEquals(ModeOfPayment.UPI, payment.getType());
//		assertEquals(PaymentStatus.PAID, payment.getStatus());
//		assertNotNull(payment.getTxId());
//	}
//	
//	@Test
//    public void testMakeUPIPaymentForBooking_BookingNotFound() throws BookingNotFoundException {
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
//            paymentService.makeUPIPaymentForBooking(new Payment(), bookingId);
//        });
//    }
    
	@Test
	public void testDeletePayment_Success() throws PaymentNotFoundException {
		long paymentId = 123L;
		Payment existingPayment = new Payment();
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(existingPayment));
		String result = paymentService.deletePayment(paymentId);
		assertEquals("Payment deleted Successfully", result);
		Mockito.verify(paymentRepository, Mockito.times(1)).deleteById(paymentId);
	}
	
	@Test
	public void testDeletePayment_PaymentNotFoundException() {
		long paymentId = 123L;
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());
		assertThrows(PaymentNotFoundException.class, () -> paymentService.deletePayment(paymentId));
		Mockito.verify(paymentRepository, never()).deleteById(paymentId);
	}
	
	@Test
	public void testFindAllPayments_Success() throws NoPaymentDoneException {
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
    public void testFindAllPayments_NoPaymentDoneException() {
        when(paymentRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoPaymentDoneException.class,
               () -> paymentService.findAllPayments());
    }
	
	@Test
	public void testGetPaymentById_Success() throws PaymentNotFoundException {
		long paymentId = 1L;
		Payment payment = new Payment();
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
		PaymentDTO expectedPaymentDTO = new PaymentDTO();
		when(modelMapper.map(payment, PaymentDTO.class)).thenReturn(expectedPaymentDTO);
		PaymentDTO result = paymentService.getPaymentById(paymentId);
		assertEquals(expectedPaymentDTO, result);
	}
	
	@Test
	public void testGetPaymentById_PaymentNotFoundException() {
		long paymentId = 12L;
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());
		assertThrows(PaymentNotFoundException.class, () -> paymentService.getPaymentById(paymentId));
		Mockito.verify(modelMapper, never()).map(any(), any());
	}
	
	
}
