package com.project.nelson.msvc_user_auth.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class UsuarioApplication {

  public static void main(String[] args) {
    SpringApplication.run(UsuarioApplication.class, args);
  }
}
