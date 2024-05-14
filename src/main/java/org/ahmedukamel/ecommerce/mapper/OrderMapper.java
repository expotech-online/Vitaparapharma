package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.response.OrderItemResponse;
import org.ahmedukamel.ecommerce.dto.response.OrderResponse;
import org.ahmedukamel.ecommerce.dto.response.OrderResponseV2;
import org.ahmedukamel.ecommerce.dto.response.orderCheck.CouponOrderCheckResponse;
import org.ahmedukamel.ecommerce.dto.response.orderCheck.OrderCheckResponse;
import org.ahmedukamel.ecommerce.dto.response.orderCheck.ProductOrderCheckResponse;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.util.EntityDetailsUtils;
import org.ahmedukamel.ecommerce.util.OrderItemInterface;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class OrderMapper {
    public static List<OrderResponse> toResponse(Collection<Order> items, Integer languageId, String pendingMsg) {
        return items.stream().sorted(Comparator.comparing(Order::getOrderDate).reversed()).map(item -> toResponse(item, languageId, pendingMsg)).toList();
    }

    public static OrderResponse toResponse(Order order, Integer languageId, String pendingMsg) {
        String shipmentDate = order.getShipment().getShipmentDate() != null ? order.getShipment().getShipmentDate().toString() : pendingMsg;
        String trackingNumber = order.getShipment().getTrackingNumber() != null ? order.getShipment().getTrackingNumber() : pendingMsg;
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderStatus(order.getStatus().name());
        response.setShipmentDate(shipmentDate);
        response.setOrderDate(order.getOrderDate().toString());
        response.setTotalAmount(order.getTotalAmount());
        response.setTrackingNumber(trackingNumber);
        response.setCountry(AddressMapper.getCountryName(order.getAddress()));
        response.setCity(AddressMapper.getCityName(order.getAddress()));
        response.setRegion(AddressMapper.getRegionName(order.getAddress()));
        response.setDescription(order.getAddress().getDescription());
        response.setZipCode(order.getAddress().getZipCode());
        response.setCustomerPhone(order.getCustomer().getPhone());
        response.setOrderItems(getItemsResponse(order.getOrderItems(), languageId));
        return response;
    }

    public static List<OrderItemResponse> getItemsResponse(Collection<OrderItem> items, Integer languageId) {
        return items.stream().map(item -> toResponse(item, languageId)).toList();
    }

    public static OrderItemResponse toResponse(OrderItem item, Integer languageId) {
        ProductDetail productDetail = EntityDetailsUtils.supplyProductDetail(item.getProduct(), languageId);
        String productPicture = item.getProduct().getPictures().isEmpty() ? "" : item.getProduct().getPictures().get(0);
        OrderItemResponse response = new OrderItemResponse();
        response.setProductId(item.getProduct().getProductId());
        response.setProductName(productDetail.getName());
        response.setUnitPrice(item.getUnitPrice());
        response.setTotalPrice(item.getTotalPrice());
        response.setQuantity(item.getQuantity());
        response.setPictureUrl(productPicture);
        return response;
    }

    public static OrderCheckResponse toCheckResponse(Collection<? extends OrderItemInterface> items, boolean couponApplied, double price, Integer languageId) {
        OrderCheckResponse response = new OrderCheckResponse();
        response.setPrice(price);
        response.setProducts(items.stream().map(item -> toProductResponse(item.getProduct(), item.getQuantity(), languageId)).toList());
        response.setCouponApplied(couponApplied);
        return response;
    }

    public static CouponOrderCheckResponse toCouponResponse(Coupon coupon) {
        CouponOrderCheckResponse response = new CouponOrderCheckResponse();
        response.setCode(coupon.getCode());
        response.setType(coupon.getCouponType().name());
        response.setDiscountType(coupon.getDiscountType().name());
        response.setDiscount(coupon.getDiscount());
        response.setProductId(coupon.getProductId());
        return response;
    }

    public static ProductOrderCheckResponse toProductResponse(Product product, int quantity, Integer languageId) {
        ProductDetail detail = EntityDetailsUtils.supplyProductDetail(product, languageId);
        ProductOrderCheckResponse response = new ProductOrderCheckResponse();
        response.setId(product.getProductId());
        response.setName(detail.getName());
        response.setPrice(product.getDiscount() ? product.getAfterDiscount() : product.getPrice());
        response.setDiscount(product.getDiscount());
        response.setQuantity(quantity);
        return response;
    }

    public static OrderResponseV2 toResponseV2(Order order, Integer languageId, String pendingMsg) {
        OrderResponse responseV1 = toResponse(order, languageId, pendingMsg);
        OrderResponseV2 responseV2 = new OrderResponseV2();
        BeanUtils.copyProperties(responseV1, responseV2);
//        responseV2.setCoupons(order.getCoupons().stream().map(Coupon::getCode).toList());
        responseV2.setShipCost(order.getShipCost());
        return responseV2;
    }

    public static List<OrderResponseV2> toResponseV2(Collection<Order> orders, Integer languageId, String pendingMsg) {
        return orders.stream().map(order -> toResponseV2(order, languageId, pendingMsg)).toList();
    }
}
