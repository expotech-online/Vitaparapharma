package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.BlogPostDto;
import org.ahmedukamel.ecommerce.dto.ProductDto;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.BlogPostMapper;
import org.ahmedukamel.ecommerce.mapper.ProductMapper;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.repository.*;
import org.ahmedukamel.ecommerce.service.CustomerViewService;
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
@RequiredArgsConstructor
@Transactional
public class CustomerViewServiceImpl implements CustomerViewService {
    final MainCategoryRepository mainCategoryRepository;
    final WishlistItemRepository wishlistItemRepository;
    final CustomerRepository customerRepository;
    final LanguageRepository languageRepository;
    final BlogPostRepository blogPostRepository;
    final MessageSourceUtils messageSourceUtils;
    final CategoryRepository categoryRepository;
    final CartItemRepository cartItemRepository;
    final ProductRepository productRepository;

    @Override
    public ApiResponse getPostForCustomer(Integer postId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        BlogPost post = RepositoryUtils.getPost(blogPostRepository, postId);
        // Processing
        BlogPostDto response = BlogPostMapper.toResponse(post, language.getLanguageId(), customer.getCustomerId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.post");
        return new ApiResponse(true, message, Map.of("post", response));
    }

    @Override
    public ApiResponse getAllPostsForCustomer() {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        List<BlogPost> posts = blogPostRepository.findAll();
        List<BlogPostDto> blogPostDtoList = BlogPostMapper.toResponse(posts, language.getLanguageId(), customer.getCustomerId());
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.posts");
        return new ApiResponse(true, message, Map.of("posts", blogPostDtoList));
    }

    @Override
    public ApiResponse getProductForCustomer(Integer productId) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        List<Integer> cartProductIds = cartItemRepository.getProductsIds(customer.getCustomerId());
        List<Integer> wishlistProductIds = wishlistItemRepository.getProductsIds(customer.getCustomerId());
        // Processing
        ProductDto productDto = getProduct(product, language, cartProductIds, wishlistProductIds);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.product");
        return new ApiResponse(true, message, Map.of("product", productDto));
    }

    @Override
    public ApiResponse getAllProductsForCustomer() {
        List<Product> products = productRepository.findAll();
        return getProducts(products);
    }

    @Override
    public ApiResponse getCategoryProductsForCustomer(Integer categoryId) {
        Category category = RepositoryUtils.getCategory(categoryRepository, categoryId);
        Collection<Product> products = category.getProducts();
        return getProducts(products);
    }

    @Override
    public ApiResponse getMainCategoryProductsForCustomer(Integer mainCategoryId) {
        RepositoryUtils.getMainCategory(mainCategoryRepository, mainCategoryId);
        Collection<Product> products = productRepository.findAllByCategory_MainCategory_CategoryId(mainCategoryId);
        return getProducts(products);
    }

    ApiResponse getProducts(Collection<Product> products) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Processing
        List<Integer> cartProductIds = cartItemRepository.getProductsIds(customer.getCustomerId());
        List<Integer> wishlistProductIds = wishlistItemRepository.getProductsIds(customer.getCustomerId());
        List<ProductDto> productDtoList = ProductMapper.toResponse(products, language.getLanguageId(), cartProductIds, wishlistProductIds, true);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.products");
        return new ApiResponse(true, message, Map.of("products", productDtoList));
    }

    ProductDto getProduct(Product product, Language language, List<Integer> cartProductIds, List<Integer> wishlistProductIds) {
        return ProductMapper.toResponse(product, language.getLanguageId(), cartProductIds, wishlistProductIds);
    }
}