package com.CPT202.cpt202;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@SpringBootApplication
public class Cpt202Application {
	@RequestMapping("/")
	String home(){
		return "Hello World!";
	}
	public static void main(String[] args) {
		SpringApplication.run(Cpt202Application.class, args);
	}
}
