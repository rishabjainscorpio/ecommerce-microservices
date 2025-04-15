package com.myorg.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.orderservice.dto.InventoryRequest;
import com.myorg.orderservice.dto.OrderLineItemsRequest;
import com.myorg.orderservice.dto.OrderRequest;
import com.myorg.orderservice.model.Order;
import com.myorg.orderservice.model.OrderLineItems;
import com.myorg.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsRequestList()
                .stream().map(orderLineItem -> this.mapToOrderLineItems(orderLineItem, order)).toList();
        order.setOrderLineItems(orderLineItems);
        List<InventoryRequest> inventoryRequests = orderRequest.getOrderLineItemsRequestList()
                        .stream().map(orderLineItem -> InventoryRequest.builder()
                        .skuCode(orderLineItem.getSkuCode())
                        .quantity(orderLineItem.getQuantity())
                        .build()).toList();

        boolean isInStock = false;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String inventoryRequestStr = objectMapper.writeValueAsString(inventoryRequests);
            String encodedJson = URLEncoder.encode(inventoryRequestStr, StandardCharsets.UTF_8);
            isInStock = Boolean.TRUE.equals(webClient.get()
                    .uri("http://localhost:8082/api/inventory", uriBuilder -> uriBuilder
                            .queryParam("skuCodes", encodedJson)
                            .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());
        } catch (Exception e) {
            log.error("Error while checking product stock in inventory. Message: " + e.getMessage());
        }

        if (isInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock. Please try again");
        }
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsRequest orderLineItemsRequest, Order order) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsRequest.getSkuCode())
                .price(orderLineItemsRequest.getPrice())
                .quantity(orderLineItemsRequest.getQuantity())
                .order(order)
                .build();
    }
}
