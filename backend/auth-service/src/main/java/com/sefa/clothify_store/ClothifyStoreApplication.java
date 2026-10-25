package com.sefa.clothify_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ClothifyStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClothifyStoreApplication.class, args);
	}

}
