package com.fms.payment.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Entity
public class Payment {
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;	
	@Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile number is invalid")
	private String mobileNumber;
	
	private ModeOfPayment type;
	private PaymentStatus status;
	
	private String cardno;
	private String expiryDetails;
	private double amount;
	
	public Payment() {
		super();
	}
	
	public Payment(Long paymentId, ModeOfPayment type, PaymentStatus status, String cardno, String expiryDetails,
			double amount) {
		super();
		this.paymentId = paymentId;
		this.type = type;
		this.status = status;
		this.cardno = cardno;
		this.expiryDetails = expiryDetails;
		this.amount = amount;
	}

	
	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public ModeOfPayment getType() {
		return type;
	}

	public void setType(ModeOfPayment type) {
		this.type = type;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getExpiryDetails() {
		return expiryDetails;
	}

	public void setExpiryDetails(String expiryDetails) {
		this.expiryDetails = expiryDetails;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public enum ModeOfPayment{
		CASH,CARD
	}
	public enum PaymentStatus{
		PENDING,PAID,REFUNDED
	}
}
