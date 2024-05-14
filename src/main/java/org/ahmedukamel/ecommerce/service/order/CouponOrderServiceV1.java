package org.ahmedukamel.ecommerce.service.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.NotificationMsgRequest;
import org.ahmedukamel.ecommerce.dto.request.OrderItemRequest;
import org.ahmedukamel.ecommerce.dto.request.OrderRequestV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.OrderResponseV2;
import org.ahmedukamel.ecommerce.dto.response.orderCheck.OrderCheckResponse;
import org.ahmedukamel.ecommerce.mapper.OrderMapper;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.CouponType;
import org.ahmedukamel.ecommerce.model.enumeration.DiscountType;
import org.ahmedukamel.ecommerce.model.enumeration.NotificationType;
import org.ahmedukamel.ecommerce.model.enumeration.OrderStatus;
import org.ahmedukamel.ecommerce.repository.*;
import org.ahmedukamel.ecommerce.service.notification.NotifyService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponOrderServiceV1 implements CouponOrderService {
    final CustomerRepository customerRepository;
    final LanguageRepository languageRepository;
    final MessageSourceUtils messageSourceUtils;
    final LocalizedEnumUtils localizedEnumUtils;
    final ProductRepository productRepository;
    final CouponRepository couponRepository;
    final NotifyService notifyService;
    final OrderRepository orderRepository;

    @Override
    public ApiResponse checkCartOrder(OrderRequestV2 request) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        List<? extends OrderItemInterface> orderItemList = customer.getCart().getCartItems();

        Pair<Double, Boolean> result = getPriceAndCoupons(orderItemList, request.getCoupon());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());

        OrderCheckResponse response = OrderMapper.toCheckResponse(customer.getCart().getCartItems(), result.getSecond(), result.getFirst(), language.getLanguageId());

        String message = "successfully";
        return new ApiResponse(true, message, Map.of("order", response));
    }

    @Override
    public ApiResponse checkCustomOrder(OrderRequestV2 request) {
        List<? extends OrderItemInterface> orderItemList = prepareOrderItems(request.getOrderItems());

        Pair<Double, Boolean> result = getPriceAndCoupons(orderItemList, request.getCoupon());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());

        OrderCheckResponse response = OrderMapper.toCheckResponse(orderItemList, result.getSecond(), result.getFirst(), language.getLanguageId());

        String message = "successfully";
        return new ApiResponse(true, message, Map.of("order", response));
    }

    @Override
    public ApiResponse createCartOrder(OrderRequestV2 request, Integer addressId) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        List<? extends OrderItemInterface> orderItemList = customer.getCart().getCartItems();

        Pair<Double, Boolean> result = getPriceAndCoupons(orderItemList, request.getCoupon());
        validateCreateOrder(addressId, customer, orderItemList, result, request.getCoupon());
        customer.getCart().getCartItems().clear();

        String message = "Successful create order !";
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse createCustomOrder(OrderRequestV2 request, Integer addressId) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        List<? extends OrderItemRequest> orderItemList = prepareOrderItems(request.getOrderItems());

        Pair<Double, Boolean> result = getPriceAndCoupons(orderItemList, request.getCoupon());
        validateCreateOrder(addressId, customer, orderItemList, result, request.getCoupon());

        String message = "Successful create order !";
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getOrderById(Integer orderId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Order order = RepositoryUtils.getOrder(customer, orderId);
        // Validating
        ValidationUtils.validateExistCustomerOrder(customer, order, messageSourceUtils);
        // Processing
        String pendingMsg = messageSourceUtils.getMessage("pending.message");
        OrderResponseV2 response = OrderMapper.toResponseV2(order, language.getLanguageId(), pendingMsg);
        localizeOrder(response);
//        OrderServiceImpl.localizeOrder(localizedEnumUtils, response);
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
        List<OrderResponseV2> orderResponseList = OrderMapper.toResponseV2(orders, language.getLanguageId(), pendingMsg)
                .stream()
                .peek(this::localizeOrder)
                .toList();
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.orders");
        return new ApiResponse(true, message, Map.of("orders", orderResponseList));
    }

    @Override
    public ApiResponse getOrdersByStatus(String status) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        OrderStatus orderStatus = EnumUtils.findEnumInsensitiveCase(OrderStatus.class, status);
        Collection<Order> orders = OrderServiceImpl.filterOrders(customer.getOrders(), orderStatus);
        String pendingMsg = messageSourceUtils.getMessage("pending.message");
        List<OrderResponseV2> orderResponseList = OrderMapper.toResponseV2(orders, language.getLanguageId(), pendingMsg)
                .stream()
                .peek(this::localizeOrder)
                .toList();
//        orderResponseList.forEach(i -> OrderServiceImpl.localizeOrder(localizedEnumUtils, i));
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.orders");
        return new ApiResponse(true, message, Map.of("orders", orderResponseList));
    }

    public void localizeOrder(OrderResponseV2 response) {
        response.setOrderStatus(localizedEnumUtils.getOrderStatus(EnumUtils.findEnumInsensitiveCase(OrderStatus.class, response.getOrderStatus())));
    }

    public Pair<Double, Boolean> getPriceAndCoupons(Collection<? extends OrderItemInterface> items, String couponCode) {
        Optional<CustomerCoupon> optionalCoupon = SecurityContextUtils.getPrinciple()
                .getCoupons()
                .stream()
                .filter(customerCoupon -> customerCoupon.getCoupon().getCode().equalsIgnoreCase(couponCode.strip()))
                .findFirst();

        double totalPrice = items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().finalPrice())
                .sum();

        Predicate<Integer> existProduct = id -> items
                .stream()
                .anyMatch(item -> item.getProduct().getProductId().equals(id));

        Function<Integer, Double> getProductPrice = id -> items
                .stream()
                .map(OrderItemInterface::getProduct)
                .filter(product -> product.getProductId().equals(id))
                .findFirst()
                .orElseGet(Product::new)
                .finalPrice();


        boolean appliedCoupon = optionalCoupon.isPresent() &&
                !optionalCoupon.get().getCoupon().isRevoked() &&
                optionalCoupon.get().getCoupon().getExpiration().isAfter(LocalDateTime.now()) &&
                !optionalCoupon.get().isUsed() &&
                (optionalCoupon.get().getCoupon().getCouponType().equals(CouponType.ORDER) ||
                        optionalCoupon.get().getCoupon().getCouponType().equals(CouponType.PRODUCT) &&
                                existProduct.test(optionalCoupon.get().getCoupon().getProductId()));

        if (appliedCoupon) {
            Coupon coupon = optionalCoupon.get().getCoupon();
            if (coupon.getDiscountType().equals(DiscountType.PRICE)) {
                totalPrice -= coupon.getDiscount();
            } else {
                double price = totalPrice;
                if (coupon.getCouponType().equals(CouponType.PRODUCT)) {
                    price = getProductPrice.apply(coupon.getProductId());
                }
                totalPrice -= (price * coupon.getDiscount() / 100.0);
            }
        }

        return Pair.of(totalPrice, appliedCoupon);
    }

    public void validateCreateOrder(Integer addressId, Customer customer,
                                    List<? extends OrderItemInterface> orderItemList,
                                    Pair<Double, Boolean> result,
                                    String couponCode) {
        ValidationUtils.validateNotNullCustomerPhone(customer, messageSourceUtils);
        Address address = ValidationUtils.validateGetCustomerAddress(customer, addressId, messageSourceUtils);
        ValidationUtils.validateActiveAddress(address);
        ValidationUtils.validateOrderItems(orderItemList, messageSourceUtils);

        Order order = new Order();
        Shipment shipment = new Shipment();
        shipment.setOrder(order);

        Set<OrderItem> orderItems = orderItemList.stream()
                .map(orderItem -> {
                    orderItem.getProduct().decrementStockQuantity(orderItem.getQuantity());
                    return new OrderItem(order, orderItem.getProduct(), orderItem.getQuantity());
                })
                .collect(Collectors.toSet());

        if (result.getSecond()) {
            CustomerCoupon coupon = customer.getCoupons()
                    .stream()
                    .filter(c -> c.getCoupon().getCode().equalsIgnoreCase(couponCode.strip()))
                    .findFirst()
                    .orElseThrow();
            coupon.setOrder(order);
            coupon.setUsed(true);
            order.setCoupon(coupon);
        }

        order.setCustomer(customer);
        order.setShipment(shipment);
        order.setAddress(address);
        order.setStatus(OrderStatus.PREPARED);
        order.setTotalAmount(result.getFirst());

        order.setOrderItems(orderItems);

        customer.getOrders().add(order);
        orderRepository.save(order);
        customerRepository.save(customer);

        NotificationMsgRequest messages = new NotificationMsgRequest();
        String code = "notification.message.order.status";

        Locale locale = new Locale("en");
        messages.setEnglishMessage(messageSourceUtils.getMessageByLocale(code, locale,
                order.getOrderId(), localizedEnumUtils.getOrderStatus(order.getStatus().name(), locale)));

        locale = new Locale("ar");
        messages.setArabicMessage(messageSourceUtils.getMessageByLocale(code, locale,
                order.getOrderId(), localizedEnumUtils.getOrderStatus(order.getStatus().name(), locale)));

        locale = new Locale("fr");
        messages.setFrenchMessage(messageSourceUtils.getMessageByLocale(code, locale,
                order.getOrderId(), localizedEnumUtils.getOrderStatus(order.getStatus().name(), locale)));

        notifyService.notify(messages, List.of(customer), NotificationType.ORDERS, order.getOrderId().toString());
    }

    public List<? extends OrderItemRequest> prepareOrderItems(List<? extends OrderItemRequest> items) {
        return items.stream()
                .filter(i -> productRepository.existsById(i.getProductId()))
                .peek(i -> i.setProduct(productRepository.findById(i.getProductId()).orElseThrow()))
                .toList();
    }
}