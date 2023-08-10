package com.fms.payment.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class CardPayment {
	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long cardId;
	@NotNull
	@Pattern(regexp = "^[A-Za-z\s]{3,12}$", message= "name should contain only characters")
	private String name;
	@NotNull
	@Pattern(regexp = "^[0-9]{10,14}$", message = "cardNo must be a number of 12 to 14 digits")
	private String cardNo;
	@NotNull
	@Pattern(regexp = "^[0-9]{3}$", message = "cvv must be a number of length 3")
	private String cvv;

	
	public CardPayment() {
		
	}


	public CardPayment(long cardId,
			@NotNull @Pattern(regexp = "^[A-Za-z ]{3,12}$", message = "name should contain only characters") String name,
			@NotNull @Pattern(regexp = "^[0-9]{10,14}$", message = "cardNo must be a number of 12 to 14 digits") String cardNo,
			@NotNull @Pattern(regexp = "^[0-9]{3}$", message = "cvv must be a number of length 3") String cvv) {
		super();
		this.cardId = cardId;
		this.name = name;
		this.cardNo = cardNo;
		this.cvv = cvv;
	}


	public long getCardId() {
		return cardId;
	}


	public void setCardId(long cardId) {
		this.cardId = cardId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCardNo() {
		return cardNo;
	}


	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}


	public String getCvv() {
		return cvv;
	}


	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	
}
