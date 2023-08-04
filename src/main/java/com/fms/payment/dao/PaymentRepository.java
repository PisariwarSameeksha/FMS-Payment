package com.fms.payment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
	
	@Query("SELECT p FROM Payment p WHERE p.mobileNumber = :mobileNumber")
    Payment findByMobileNumber(String mobileNumber);
}
