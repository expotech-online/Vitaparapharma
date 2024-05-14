package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.MessageRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.OrderResponse;
import org.ahmedukamel.ecommerce.dto.response.OrderResponseV2;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.mapper.OrderMapper;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.model.Order;
import org.ahmedukamel.ecommerce.model.enumeration.OrderStatus;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.OrderRepository;
import org.ahmedukamel.ecommerce.service.DeliveryService;
import org.ahmedukamel.ecommerce.service.order.OrderNotificationService;
import org.ahmedukamel.ecommerce.util.LocaleContextUtils;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
    private final OrderNotificationService orderNotificationService;
    private final LanguageRepository languageRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final OrderRepository orderRepository;

    @Override
    public ApiResponse getPreparedOrders() {
        return getOrdersByStatus(OrderStatus.PREPARED);
    }

    @Override
    public ApiResponse getShippedOrders() {
        return getOrdersByStatus(OrderStatus.SHIPPED);
    }

    @Override
    public ApiResponse getOutForDeliverOrders() {
        return getOrdersByStatus(OrderStatus.OUT_FOR_DELIVERY);
    }

    @Override
    public ApiResponse setOrderShipped(Integer orderId, MessageRequest request) {
        // Querying
        Order order = RepositoryUtils.getOrder(orderRepository, orderId);
        // Processing
        order.setStatus(OrderStatus.SHIPPED);
        order.getShipment().setShipmentDate(LocalDateTime.now());
        order.getShipment().setTrackingNumber(request.getValue());
        orderRepository.save(order);
        orderNotificationService.notify(order, OrderStatus.SHIPPED);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.set.order.status");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse setOrderOutForDeliver(Integer orderId) {
        return setOrderStatus(orderId, OrderStatus.OUT_FOR_DELIVERY);
    }

    @Override
    public ApiResponse setOrderDelivered(Integer orderId) {
        return setOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    @Override
    public ApiResponse getOrderById(Integer orderId) {
        Order order = RepositoryUtils.getOrder(orderRepository, orderId);
        if (order.getStatus().equals(OrderStatus.PREPARED) ||
                order.getStatus().equals(OrderStatus.SHIPPED) ||
                order.getStatus().equals(OrderStatus.OUT_FOR_DELIVERY)) {
            Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
            OrderResponseV2 response = OrderMapper.toResponseV2(order, language.getLanguageId(), "Pending");
            return new ApiResponse(true, "Success", Map.of("order", response));
        }
        throw new CustomException("Order with is %d not found.".formatted(orderId));
    }

    private ApiResponse getOrdersByStatus(OrderStatus status) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        List<Order> orders = orderRepository.findAllByStatus(status);
        // Processing
        String pendingMsg = messageSourceUtils.getMessage("pending.message");
        List<OrderResponse> orderResponseList = OrderMapper.toResponse(orders, language.getLanguageId(), pendingMsg);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.orders");
        return new ApiResponse(true, message, Map.of("orders", orderResponseList));
    }

    private ApiResponse setOrderStatus(Integer orderId, OrderStatus status) {
        // Querying
        Order order = RepositoryUtils.getOrder(orderRepository, orderId);
        // Processing
        order.setStatus(status);
        orderRepository.save(order);
        orderNotificationService.notify(order, status);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.set.order.status");
        return new ApiResponse(true, message);
    }
}
