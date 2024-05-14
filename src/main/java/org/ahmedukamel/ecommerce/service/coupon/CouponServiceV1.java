package org.ahmedukamel.ecommerce.service.coupon;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.CouponRequest;
import org.ahmedukamel.ecommerce.dto.request.NotificationMsgRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.CouponResponse;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.exception.EntityNotFoundException;
import org.ahmedukamel.ecommerce.mapper.CouponMapper;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.CouponType;
import org.ahmedukamel.ecommerce.model.enumeration.DiscountType;
import org.ahmedukamel.ecommerce.model.enumeration.NotificationType;
import org.ahmedukamel.ecommerce.repository.CouponRepository;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.service.notification.NotifyService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceV1 implements CouponService {
    final CouponRepository repository;
    final CustomerRepository customerRepository;
    final MessageSourceUtils messageSourceUtils;
    final ProductRepository productRepository;
    final CouponRepository couponRepository;
    final NotifyService notifyService;

    @Override
    public ApiResponse addCoupon(CouponRequest request) {
        if (request.getDiscountType().equals(DiscountType.PERCENTAGE)) {
            if (request.getDiscount() < 1 || request.getDiscount() > 100) {
                throw new CustomException("Coupon discount must be between 1 and 100");
            }
        }

        if (request.getCouponType().equals(CouponType.PRODUCT)) {
            if (!productRepository.existsById(request.getProductId())) {
                throw new EntityNotFoundException(request.getProductId() + "", Product.class);
            }
        }

        final List<Customer> customers;
        if (request.getCustomers().isEmpty()) {
            customers = customerRepository.findAll();
        } else {
            customers = request.getCustomers()
                    .stream()
                    .map(customerRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }

        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(request, coupon);
        if (coupon.getCouponType().equals(CouponType.ORDER)) {
            coupon.setProductId(-1);
        }
        coupon.setExpiration(DateTimeUtils.getDateTime(request.getExpiration()));

        final Set<CustomerCoupon> customerCoupons = customers
                .stream()
                .map(customer -> {
                    CustomerCoupon customerCoupon = new CustomerCoupon();
                    customerCoupon.setCustomer(customer);
                    customerCoupon.setCoupon(coupon);
                    return customerCoupon;
                })
                .collect(Collectors.toSet());

        String code = Generator.couponCode();
        while (couponRepository.existsById(code)) {
            code = Generator.couponCode();
        }

        coupon.setCustomers(customerCoupons);
        coupon.setCode(code);
        Coupon saved = couponRepository.save(coupon);

        NotificationMsgRequest notificationRequest = new NotificationMsgRequest();
        BeanUtils.copyProperties(request, notificationRequest);

        notifyService.notify(notificationRequest, customers, NotificationType.COUPONS, code);
        return new ApiResponse(true, "Coupon created", saved);
    }

    @Override
    public ApiResponse getAllCoupons() {
        List<CouponResponse> couponResponseList = CouponMapper.toResponse(repository.findAll());
        String message = "all coupons";
        return new ApiResponse(true, message, Map.of("coupons", couponResponseList));
    }

    @Override
    public ApiResponse revokeCoupon(String code) {
        Coupon coupon = RepositoryUtils.getCoupon(repository, code);
        ValidationUtils.nonRevokedCoupon(coupon);
        coupon.setRevoked(true);
        repository.save(coupon);
        return new ApiResponse(true, "Coupon have been revoked successfully!");
    }

    @Override
    public ApiResponse invokeCoupon(String code) {
        Coupon coupon = RepositoryUtils.getCoupon(repository, code);
        ValidationUtils.revokedCoupon(coupon);
        coupon.setRevoked(false);
        repository.save(coupon);
        return new ApiResponse(true, "Coupon have been invoked successfully!");
    }
}
