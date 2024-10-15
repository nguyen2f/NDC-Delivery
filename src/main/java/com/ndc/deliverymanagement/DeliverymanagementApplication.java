package com.ndc.deliverymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class DeliverymanagementApplication {
	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}

	public static void main(String[] args) {

		SpringApplication.run(DeliverymanagementApplication.class, args);
	}
}

