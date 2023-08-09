package com.fms.payment.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class UPIPayment {
	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long UPIId;
	@NotNull
	@Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile number is invalid")
	private String mobileNumber;
	@NotNull
	@Pattern(regexp = "^\\d{3}$", message = "pin must be a number of length 3")
	private String pin;
	
	
	public UPIPayment(String mobileNumber,String pin) {
		super();
		this.mobileNumber = mobileNumber;
		this.pin = pin;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	
	
}
