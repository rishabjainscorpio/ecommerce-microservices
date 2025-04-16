package com.myorg.productcatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ShoppingKartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingKartApplication.class, args);
	}

}
