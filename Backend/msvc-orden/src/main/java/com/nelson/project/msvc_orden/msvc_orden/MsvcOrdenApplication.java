package com.nelson.project.msvc_orden.msvc_orden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcOrdenApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcOrdenApplication.class, args);
	}

}
