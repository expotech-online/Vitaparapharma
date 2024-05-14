package org.ahmedukamel.ecommerce.service.impl;

import org.ahmedukamel.ecommerce.dto.ProductDtoV3;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.ProductMapper;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.repository.*;
import org.ahmedukamel.ecommerce.util.LocaleContextUtils;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.util.SecurityContextUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
public class CustomerViewServiceImplV3 extends CustomerViewServiceImpl {
    public CustomerViewServiceImplV3(MainCategoryRepository mainCategoryRepository, WishlistItemRepository wishlistItemRepository, CustomerRepository customerRepository, LanguageRepository languageRepository, BlogPostRepository blogPostRepository, MessageSourceUtils messageSourceUtils, CategoryRepository categoryRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        super(mainCategoryRepository, wishlistItemRepository, customerRepository, languageRepository, blogPostRepository, messageSourceUtils, categoryRepository, cartItemRepository, productRepository);
    }

    @Override
    ProductDtoV3 getProduct(Product product, Language language, List<Integer> cartProductIds, List<Integer> wishlistProductIds) {
        try {
            return ProductMapper.toResponseV3(product, language.getLanguageId(), cartProductIds, wishlistProductIds);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    ApiResponse getProducts(Collection<Product> products) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Processing
        List<Integer> cartProductIds = cartItemRepository.getProductsIds(customer.getCustomerId());
        List<Integer> wishlistProductIds = wishlistItemRepository.getProductsIds(customer.getCustomerId());
        List<ProductDtoV3> productDtoList = ProductMapper.toResponseV3(products, language.getLanguageId(), cartProductIds, wishlistProductIds, true);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.products");
        return new ApiResponse(true, message, Map.of("products", productDtoList));
    }
}
