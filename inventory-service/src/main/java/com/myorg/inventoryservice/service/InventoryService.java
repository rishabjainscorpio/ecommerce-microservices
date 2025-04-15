package com.myorg.inventoryservice.service;

import com.myorg.inventoryservice.dto.InventoryRequest;
import com.myorg.inventoryservice.dto.InventoryResponse;
import com.myorg.inventoryservice.model.Inventory;
import com.myorg.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(List<InventoryRequest> skuCodes) {
        List<Inventory> inventoryList = inventoryRepository.findBySkuCodeIn(skuCodes
                .stream().map(InventoryRequest::getSkuCode).collect(Collectors.toList()));

        return skuCodes.stream().allMatch(request -> {
             Optional<Inventory> inventoryObj = inventoryList.stream().filter(inventory -> inventory.getSkuCode().equals(request.getSkuCode())).findFirst();
            return inventoryObj.filter(inventory -> inventory.getQuantity() > request.getQuantity()).isPresent();
        });
    }
}
