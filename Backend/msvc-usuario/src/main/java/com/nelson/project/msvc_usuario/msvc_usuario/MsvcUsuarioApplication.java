package com.nelson.project.msvc_usuario.msvc_usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcUsuarioApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsvcUsuarioApplication.class, args);
  }
}
