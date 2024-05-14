package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.MessageRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.DeliveryService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/delivery")
public class DeliveryController {
    private final DeliveryService service;

    @GetMapping(value = "order/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable(value = "orderId") int orderId) {
        ApiResponse response = service.getOrderById(orderId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "prepared")
    public ResponseEntity<ApiResponse> getPreparedOrders() {
        ApiResponse response = service.getPreparedOrders();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "shipped")
    public ResponseEntity<ApiResponse> getShippedOrders() {
        ApiResponse response = service.getShippedOrders();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "out")
    public ResponseEntity<ApiResponse> getOutForDeliverOrders() {
        ApiResponse response = service.getOutForDeliverOrders();
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "shipped/{orderId}")
    public ResponseEntity<ApiResponse> setOrderShipped(@PathVariable(value = "orderId") Integer orderId, @Valid @RequestBody MessageRequest request) {
        ApiResponse response = service.setOrderShipped(orderId, request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "out/{orderId}")
    public ResponseEntity<ApiResponse> setOrderOutForDeliver(@PathVariable(value = "orderId") Integer orderId) {
        ApiResponse response = service.setOrderOutForDeliver(orderId);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "delivered/{orderId}")
    public ResponseEntity<ApiResponse> setOrderDelivered(@PathVariable(value = "orderId") Integer orderId) {
        ApiResponse response = service.setOrderDelivered(orderId);
        return ResponseUtils.acceptedResponse(response);
    }
}