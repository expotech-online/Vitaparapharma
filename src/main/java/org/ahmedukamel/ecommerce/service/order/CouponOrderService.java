package org.ahmedukamel.ecommerce.service.order;

import org.ahmedukamel.ecommerce.dto.request.OrderRequestV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;

public interface CouponOrderService {
    ApiResponse checkCartOrder(OrderRequestV2 request);

    ApiResponse checkCustomOrder(OrderRequestV2 request);

    ApiResponse createCartOrder(OrderRequestV2 request, Integer addressId);

    ApiResponse createCustomOrder(OrderRequestV2 request, Integer addressId);

    ApiResponse getOrderById(Integer orderId);

    ApiResponse getAllOrders();

    ApiResponse getOrdersByStatus(String status);
}