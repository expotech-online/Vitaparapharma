package org.ahmedukamel.ecommerce.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.OrderRequestV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.enumeration.OrderStatus;
import org.ahmedukamel.ecommerce.service.order.CouponOrderService;
import org.ahmedukamel.ecommerce.service.order.OrderService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.ahmedukamel.ecommerce.validation.validators.ValidEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v2/user/order")
@Transactional
public class OrderControllerV2 {
    private final OrderService service;
    private final CouponOrderService orderService;

    @PostMapping(value = "check/cart")
    public ResponseEntity<ApiResponse> checkCartOrder(@RequestBody OrderRequestV2 request) {
        ApiResponse response = orderService.checkCartOrder(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "check/order")
    public ResponseEntity<ApiResponse> checkCustomOrder(@Valid @RequestBody OrderRequestV2 request) {
        ApiResponse response = orderService.checkCustomOrder(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "cart/on/{addressId}")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequestV2 request, @PathVariable(value = "addressId") Integer addressId) {
        ApiResponse response = orderService.createCartOrder(request, addressId);
        return ResponseUtils.acceptedResponse(response);
    }

    @PostMapping(value = "new/on/{addressId}")
    public ResponseEntity<ApiResponse> createCustomOrder(@Valid @RequestBody OrderRequestV2 request, @PathVariable(value = "addressId") Integer addressId) {
        ApiResponse response = orderService.createCustomOrder(request, addressId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "{orderId}")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable(value = "orderId") Integer orderId) {
        ApiResponse response = orderService.getOrderById(orderId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse> getAllOrders() {
        ApiResponse response = orderService.getAllOrders();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getPreparedOrder(@ValidEnum(enumClass = OrderStatus.class) @RequestParam(value = "status") String status) {
        ApiResponse response = orderService.getOrdersByStatus(status);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "prepared")
    public ResponseEntity<ApiResponse> getPreparedOrders() {
        ApiResponse response = service.getPreparedOrders();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "delivered")
    public ResponseEntity<ApiResponse> getDeliveredOrders() {
        ApiResponse response = service.getDeliveredOrders();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "canceled")
    public ResponseEntity<ApiResponse> getCanceledOrders() {
        ApiResponse response = service.getCanceledOrders();
        return ResponseUtils.acceptedResponse(response);
    }
}
