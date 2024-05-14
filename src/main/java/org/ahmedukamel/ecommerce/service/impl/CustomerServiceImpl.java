package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.CustomerCouponResponse;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.repository.*;
import org.ahmedukamel.ecommerce.service.CustomerService;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.util.SecurityContextUtils;
import org.ahmedukamel.ecommerce.util.ValidationUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
@AllArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final ProductRepository productRepository;

    @Override
    public ApiResponse demandProduct(Integer productId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        // Validating
        ValidationUtils.validateExistCustomerDemand(customer, productId, messageSourceUtils);
        // Processing
        Demand demand = new Demand();
        demand.setCustomer(customer);
        demand.setProduct(product);
        customer.getDemands().add(demand);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.add.product.demand");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getCoupons() {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        List<CustomerCouponResponse> response = customer.getCoupons()
                .stream()
                .map(this::toResponse)
                .toList();
        return new ApiResponse(true, "All Coupons", response);
    }

    public CustomerCouponResponse toResponse(CustomerCoupon customerCoupon) {
        CustomerCouponResponse customerCouponResponse = new CustomerCouponResponse();
        customerCouponResponse.setCouponCode(customerCoupon.getCoupon().getCode());
        customerCouponResponse.setUsed(customerCoupon.isUsed());
        customerCouponResponse.setRevoked(customerCoupon.getCoupon().isRevoked());
        customerCouponResponse.setExpired(customerCoupon.getCoupon().getExpiration().isBefore(LocalDateTime.now()));
        customerCouponResponse.setDiscount(customerCoupon.getCoupon().getDiscount());
        customerCouponResponse.setProductId(customerCoupon.getCoupon().getProductId());
        customerCouponResponse.setCouponType(customerCoupon.getCoupon().getCouponType());
        customerCouponResponse.setDiscountType(customerCoupon.getCoupon().getDiscountType());
        int orderId = customerCoupon.getOrder() != null ? customerCoupon.getOrder().getOrderId() : -1;
        customerCouponResponse.setOrderId(orderId);
        return customerCouponResponse;
    }
}