package com.fms.payment.simplePojo;

import com.fms.payment.entity.Payment.ModeOfPayment;
import com.fms.payment.entity.Payment.PaymentStatus;

public class PaymentDTO {
	private long paymentId;
	private String txId;
	private ModeOfPayment type;
	private PaymentStatus status;
	private double amount;
	private long bookingId;
	
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
	
}
