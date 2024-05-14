package org.ahmedukamel.ecommerce.util;

import org.ahmedukamel.ecommerce.dto.request.NotificationMsgRequest;
import org.ahmedukamel.ecommerce.dto.request.OrderItemRequest;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.CouponType;
import org.ahmedukamel.ecommerce.model.enumeration.DiscountType;
import org.ahmedukamel.ecommerce.model.enumeration.NotificationType;
import org.ahmedukamel.ecommerce.model.enumeration.OrderStatus;
import org.ahmedukamel.ecommerce.repository.CouponRepository;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.OrderRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.service.notification.NotifyService;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrderUtils {
    public static Set<String> validateCouponCodes(Collection<String> couponCodes) {
        return couponCodes.stream().map(String::toUpperCase).map(String::strip).collect(Collectors.toSet());
    }

    public static Pair<Double, Boolean> getPriceAndCoupons(Collection<? extends OrderItemInterface> items, String couponCode) {
        Optional<CustomerCoupon> optionalCoupon = SecurityContextUtils.getPrinciple()
                .getCoupons()
                .stream()
                .filter(customerCoupon -> customerCoupon.getCoupon().getCode().equalsIgnoreCase(couponCode.strip()))
                .findFirst();

        double totalPrice = items.stream()
                .map(OrderItemInterface::getProduct)
                .mapToDouble(Product::finalPrice)
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
                !optionalCoupon.get().isUsed() &&
                existProduct.test(optionalCoupon.get().getCoupon().getProductId());

        if (appliedCoupon) {
            Coupon coupon = optionalCoupon.get().getCoupon();
            if (coupon.getDiscountType().equals(DiscountType.PRICE)) {
                totalPrice -= coupon.getDiscount();
            } else {
                double price = totalPrice;
                if (coupon.getCouponType().equals(CouponType.PRODUCT)) {
                    price = getProductPrice.apply(coupon.getProductId());
                }
                totalPrice -= (price * coupon.getDiscount());
            }
        }

        return Pair.of(totalPrice, appliedCoupon);
    }

    public static void validateCreateOrder(Integer addressId, Customer customer,
                                           List<? extends OrderItemInterface> orderItemList,
                                           Pair<Double, Boolean> result,
                                           MessageSourceUtils messageSourceUtils,
                                           CustomerRepository customerRepository,
                                           LocalizedEnumUtils localizedEnumUtils,
                                           NotifyService notifyService,
                                           OrderRepository orderRepository,
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

    public static List<? extends OrderItemRequest> prepareOrderItems(List<? extends OrderItemRequest> items, ProductRepository repository) {
        return items.stream()
                .filter(i -> repository.existsById(i.getProductId()))
                .peek(i -> i.setProduct(repository.findById(i.getProductId()).orElseThrow()))
                .toList();
    }
}