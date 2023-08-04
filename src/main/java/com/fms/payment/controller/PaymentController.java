package com.fms.payment.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fms.payment.entity.Payment;
import com.fms.payment.exception.PaymentException;
import com.fms.payment.service.PaymentService;

@RestController
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/payments")
	public List<Payment> findAllPayments() throws PaymentException {
		return paymentService.findAllPayments();
	}
	
	@GetMapping("/payment/{id}")
	public Payment findPaymentById(@PathVariable long id)throws PaymentException {
		return paymentService.getPaymentById(id);
	}
	
	@GetMapping("/payment/{mobileNumber}")
	public Payment findPaymentByNumber(@PathVariable String mobileNumber)throws PaymentException{
		return paymentService.getPaymentByMobileNumber(mobileNumber);
	}
	
	
}
