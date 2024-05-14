package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.CartRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.CartResponse;
import org.ahmedukamel.ecommerce.mapper.CartMapper;
import org.ahmedukamel.ecommerce.model.CartItem;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.service.CartService;
import org.ahmedukamel.ecommerce.util.*;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private final CustomerRepository customerRepository;
    private final LanguageRepository languageRepository;
    private final MessageSourceUtils messageSourceUtils;
    private final ProductRepository productRepository;

    @Override
    public ApiResponse getCart() {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Processing
        CartResponse cart = CartMapper.toResponse(customer.getCart(), language.getLanguageId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.cart");
        return new ApiResponse(true, message, Map.of("cart", cart));
    }

    @Override
    public ApiResponse updateCart(CartRequest request) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Product product = RepositoryUtils.getProduct(productRepository, request.getProductId());
        // Processing
        CartItem cartItem = EntityDetailsUtils.supplyCartItem(customer, product.getProductId());
        cartItem.setCart(customer.getCart());
        cartItem.setProduct(product);
        cartItem.setQuantity(request.getQuantity());
        customer.getCart().getCartItems().add(cartItem);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.update.cart");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse removeFromCart(Integer productId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Validating
        RepositoryService.checkExistProduct(productRepository, productId);
        // Processing
        CartItem cartItem = UpdateUtils.validateGetCartItem(customer, productId, messageSourceUtils);
        customer.getCart().getCartItems().remove(cartItem);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.remove.cart.item");
        return new ApiResponse(true, message);
    }
}
