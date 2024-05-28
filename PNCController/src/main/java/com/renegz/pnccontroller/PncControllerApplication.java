package com.renegz.pnccontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PncControllerApplication {
	public static void main(String[] args) throws Throwable {
		SpringApplication.run(PncControllerApplication.class, args);
	}
}
