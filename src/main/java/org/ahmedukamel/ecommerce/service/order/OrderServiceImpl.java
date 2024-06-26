package org.ahmedukamel.ecommerce.service.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.model.enumeration.OrderStatus;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.util.*;
import org.ahmedukamel.ecommerce.dto.request.OrderRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.OrderResponse;
import org.ahmedukamel.ecommerce.mapper.OrderMapper;
import org.ahmedukamel.ecommerce.model.*;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderNotificationService orderNotificationService;
    private final CustomerRepository customerRepository;
    private final LanguageRepository languageRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final LocalizedEnumUtils localizedEnumUtils;
    private final ProductRepository productRepository;

    @Override
    public ApiResponse createOrder(Integer addressId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Processing
        Order order = createOrder(customer, customer.getCart().getCartItems(), addressId);
        customer.getCart().getCartItems().clear();
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.add.order");
        Map<String, Map<String, Integer>> data = Map.of("order", Map.of("orderId", order.getOrderId()));
        return new ApiResponse(true, message, data);
    }

    @Override
    public ApiResponse createOrder(OrderRequest request, Integer addressId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Processing
        ValidationUtils.validatePrepareRequestItems(productRepository, request);
        Order order = createOrder(customer, request.getOrderItems(), addressId);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.add.order");
        Map<String, Map<String, Integer>> data = Map.of("order", Map.of("orderId", order.getOrderId()));
        return new ApiResponse(true, message, data);
    }

    @Override
    public ApiResponse cancelOrder(Integer orderId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Order order = RepositoryUtils.getOrder(customer, orderId);
        // Validating
        ValidationUtils.validateExistCustomerOrder(customer, order, messageSourceUtils);
        ValidationUtils.validateCancelOrder(order, messageSourceUtils);
        // Processing
        returnOrderItems(order);
        order.setStatus(OrderStatus.CANCELLED);
        customerRepository.save(customer);
        orderNotificationService.notify(order, OrderStatus.CANCELLED);

        // Response
        String message = messageSourceUtils.getMessage("operation.successful.cancel.order");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getOrder(Integer orderId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Order order = RepositoryUtils.getOrder(customer, orderId);
        // Validating
        ValidationUtils.validateExistCustomerOrder(customer, order, messageSourceUtils);
        // Processing
        String pendingMsg = messageSourceUtils.getMessage("pending.message");
        OrderResponse response = OrderMapper.toResponse(order, language.getLanguageId(), pendingMsg);
        localizeOrder(localizedEnumUtils, response);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.order");
        return new ApiResponse(true, message, Map.of("order", response));
    }

    @Override
    public ApiResponse getAllOrders() {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        Collection<Order> orders = customer.getOrders();
        String pendingMsg = messageSourceUtils.getMessage("pending.message");
        List<OrderResponse> orderResponseList = OrderMapper.toResponse(orders, language.getLanguageId(), pendingMsg);
        orderResponseList.forEach(i -> localizeOrder(localizedEnumUtils, i));
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.orders");
        return new ApiResponse(true, message, Map.of("orders", orderResponseList));
    }

    @Override
    public ApiResponse getPreparedOrders() {
        return getOrders(OrderStatus.PREPARED);
    }

    @Override
    public ApiResponse getDeliveredOrders() {
        return getOrders(OrderStatus.DELIVERED);
    }

    @Override
    public ApiResponse getCanceledOrders() {
        return getOrders(OrderStatus.CANCELLED);
    }

    private Order createOrder(Customer customer, Collection<? extends OrderItemInterface> items, Integer addressId) {
        // Validating
        ValidationUtils.validateNotNullCustomerPhone(customer, messageSourceUtils);
        Address address = ValidationUtils.validateGetCustomerAddress(customer, addressId, messageSourceUtils);
        ValidationUtils.validateActiveAddress(address);
        ValidationUtils.validateOrderItems(items, messageSourceUtils);
        // Processing
        Order order = new Order();
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        // Decrement products in stock and create order item list
        List<OrderItem> orderItems = prepareGetOrderItems(items, order);
        order.setCustomer(customer);
        order.setShipment(shipment);
        order.setAddress(address);
        order.setStatus(OrderStatus.PREPARED);
        order.getOrderItems().addAll(orderItems);
        order.setTotalAmount(orderItems.stream().mapToDouble(OrderItem::getTotalPrice).sum());
        customer.getOrders().add(order);
        customerRepository.save(customer);
        orderNotificationService.notify(order, OrderStatus.PREPARED);
        return order;
    }

    private ApiResponse getOrders(OrderStatus status) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        Collection<Order> orders = filterOrders(customer.getOrders(), status);
        String pendingMsg = messageSourceUtils.getMessage("pending.message");
        List<OrderResponse> orderResponseList = OrderMapper.toResponse(orders, language.getLanguageId(), pendingMsg);
        orderResponseList.forEach(i -> localizeOrder(localizedEnumUtils, i));
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.orders");
        return new ApiResponse(true, message, Map.of("orders", orderResponseList));
    }

    private List<OrderItem> prepareGetOrderItems(Collection<? extends OrderItemInterface> items, Order order) {
        return items.stream().peek(item -> item.getProduct().decrementStockQuantity(item.getQuantity())).map(ci -> new OrderItem(order, ci.getProduct(), ci.getQuantity())).toList();
    }

    public static List<Order> filterOrders(Collection<Order> items, OrderStatus status) {
        return items.stream().filter(item -> item.getStatus().equals(status)).toList();
    }

    private void returnOrderItems(Order order) {
        order.getOrderItems().forEach(item -> item.getProduct().incrementStockQuantity(item.getQuantity()));
    }

    public static void localizeOrder(LocalizedEnumUtils localizedEnumUtils, OrderResponse response) {
        response.setOrderStatus(localizedEnumUtils.getOrderStatus(EnumUtils.findEnumInsensitiveCase(OrderStatus.class, response.getOrderStatus())));
        response.setCountry(localizedEnumUtils.getCountry(response.getCountry()));
    }
}