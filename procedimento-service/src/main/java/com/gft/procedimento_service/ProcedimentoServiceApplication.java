package com.gft.procedimento_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ProcedimentoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcedimentoServiceApplication.class, args);
	}

}
