package com.ndc.deliverymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@Controller
public class DeliverymanagementApplication {

	public static void main(String[] args) {

		SpringApplication.run(DeliverymanagementApplication.class, args);
	}
}

