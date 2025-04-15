package com.myorg.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.inventoryservice.dto.InventoryRequest;
import com.myorg.inventoryservice.dto.InventoryResponse;
import com.myorg.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCodes) {
        ObjectMapper objectMapper = new ObjectMapper();
        String decodedSkus = URLDecoder.decode(skuCodes, StandardCharsets.UTF_8);
        try {
            List<InventoryRequest> inventoryRequests = objectMapper.readValue(decodedSkus, new TypeReference<List<InventoryRequest>>() {});
            return inventoryService.isInStock(inventoryRequests);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
