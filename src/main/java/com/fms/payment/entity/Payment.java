package com.fms.payment.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;


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
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "card_fk", referencedColumnName = "cardId")
	private CardPayment card;
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "upi_fk", referencedColumnName = "UPIId")
	private UPIPayment upi;
	
	private double amount;
	
	private long bookingId;
	
	
	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Payment(long paymentId, @NotNull String txId, @NotNull ModeOfPayment type, @NotNull PaymentStatus status,
			CardPayment card, UPIPayment upi, @NotNull double amount) {
		super();
		this.paymentId = paymentId;
		this.txId = txId;
		this.type = type;
		this.status = status;
		this.card = card;
		this.upi = upi;
		this.amount = amount;
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
