package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private Integer orderId;
    private String orderStatus;
    private String orderDate;
    private String shipmentDate;
    private Double totalAmount;
    private String trackingNumber;
    private String country;
    private String city;
    private String region;
    private String description;
    private Integer zipCode;
    private String customerPhone;
    private List<OrderItemResponse> orderItems;
}
