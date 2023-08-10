package com.fms.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FmsPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(FmsPaymentApplication.class, args);
	}

}
