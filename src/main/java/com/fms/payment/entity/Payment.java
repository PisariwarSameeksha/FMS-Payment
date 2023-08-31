package com.fms.payment.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


@Entity

public class Payment {
	@Id	
	@GeneratedValue
	private long paymentId;
	
	private String txId;
	
	@Enumerated(EnumType.STRING)
	private ModeOfPayment type = ModeOfPayment.SELECT;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus status= PaymentStatus.PENDING;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "card_fk", referencedColumnName = "cardId")
	private CardPayment card;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "upi_fk", referencedColumnName = "UPIId")
	private UPIPayment upi;
	
	private double amount;
	
	private long bookingId;
	
	
	public Payment() {
		
	}

	
	public Payment(long paymentId, String txId, ModeOfPayment type, PaymentStatus status, CardPayment card,
			UPIPayment upi, double amount, long bookingId) {
		super();
		this.paymentId = paymentId;
		this.txId = txId;
		this.type = type;
		this.status = status;
		this.card = card;
		this.upi = upi;
		this.amount = amount;
		this.bookingId = bookingId;
	}


	public long getPaymentId() {
		return paymentId;
	}


	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}


	public String getTxId() {
		return txId;
	}


	public void setTxId(String txId) {
		this.txId = txId;
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


	public CardPayment getCard() {
		return card;
	}


	public void setCard(CardPayment card) {
		this.card = card;
	}


	public UPIPayment getUpi() {
		return upi;
	}


	public void setUpi(UPIPayment upi) {
		this.upi = upi;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public long getBookingId() {
		return bookingId;
	}


	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}


	public enum ModeOfPayment{
		SELECT,CARD,UPI
	}
	public enum PaymentStatus{
		PENDING,PAID,REFUNDED
	}
	
}
