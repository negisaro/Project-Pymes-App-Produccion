package com.nelson.project.msvc_categoria.msvc_categoria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcCategoriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcCategoriaApplication.class, args);
	}

}
