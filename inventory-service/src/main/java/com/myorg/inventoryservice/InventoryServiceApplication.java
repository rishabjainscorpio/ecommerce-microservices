package com.myorg.inventoryservice;

import com.myorg.inventoryservice.model.Inventory;
import com.myorg.inventoryservice.repository.InventoryRepository;
import com.myorg.inventoryservice.service.InventoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			List<Inventory> inventoryList = inventoryRepository.findAll();
			if (inventoryList.isEmpty()) {
				Inventory inventory = new Inventory();
				inventory.setSkuCode("iPhone_16_pro");
				inventory.setQuantity(100);
				inventoryRepository.save(inventory);

				inventory = new Inventory();
				inventory.setSkuCode("iPhone_16_pro_max");
				inventory.setQuantity(0);
				inventoryRepository.save(inventory);
			}
		};
	}
}
