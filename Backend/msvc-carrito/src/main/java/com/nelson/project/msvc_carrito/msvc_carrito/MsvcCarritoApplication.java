package com.nelson.project.msvc_carrito.msvc_carrito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcCarritoApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsvcCarritoApplication.class, args);
  }
}
