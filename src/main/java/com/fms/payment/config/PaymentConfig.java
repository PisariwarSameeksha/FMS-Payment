package com.fms.payment.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PaymentConfig {
	@Bean
	public ModelMapper modelMapper() {
		
		return new ModelMapper();
	}
}
