package com.arka_proyect.msvc.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
public class MsvcProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcProductsApplication.class, args);
	}
}
