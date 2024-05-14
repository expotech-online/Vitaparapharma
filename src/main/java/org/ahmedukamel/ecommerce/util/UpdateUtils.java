package org.ahmedukamel.ecommerce.util;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.*;
import org.ahmedukamel.ecommerce.dto.request.UpdatePasswordRequest;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.service.impl.RepositoryService;
import org.ahmedukamel.ecommerce.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateUtils {
    public static CartItem validateGetCartItem(Customer customer, Integer productId, MessageSourceUtils messageSourceUtils) {
        Optional<CartItem> optionalCartItem = customer.getCart().getCartItems().stream().filter(item -> item.getProduct().getProductId().equals(productId)).findFirst();
        if (optionalCartItem.isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.not.exist.cart.item");
            throw new CustomException(message);
        }
        return optionalCartItem.get();
    }

    public static void validateUpdatePassword(PasswordEncoder passwordEncoder, UpdatePasswordRequest request, Customer customer, MessageSourceUtils messageSourceUtils) {
        if (!passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
            String message = messageSourceUtils.getMessage("operation.failed.wrong.password");
            throw new CustomException(message);
        }
        if (request.getOldPassword().equals(request.getNewPassword())) {
            String message = messageSourceUtils.getMessage("operation.failed.same.password");
            throw new CustomException(message);
        }
        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }

    public static void updateMainCategoryDetails(MainCategoryDto request, MainCategoryDetail mainCategoryDetail) {
        if (Validator.NOT_NULL_NOT_BLANK(request.getName())) {
            mainCategoryDetail.setName(request.getName());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getDescription())) {
            mainCategoryDetail.setDescription(request.getDescription());
        }
    }

    public static void updateMainCategory(Category category, MainCategory mainCategory) {
        if (mainCategory != null) {
            category.setMainCategory(mainCategory);
        }
    }

    public static void updateReview(ReviewDto request, Review review) {
        if (Validator.NOT_NULL_NOT_BLANK(request.getComment())) {
            review.setComment(request.getComment());
        }
        if (Validator.RATE(request.getRating())) {
            review.setRating(request.getRating());
        }
    }

    public static void validateUpdateCustomer(CustomerRepository repository, CustomerDto request, Customer customer) {
        if (Validator.EMAIL(request.getEmail())) {
            RepositoryService.checkNotExistEmail(repository, request.getEmail(), customer.getProvider());
            customer.setEmail(request.getEmail().toLowerCase().strip());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getPhone())) {
            RepositoryService.checkNotExistPhone(repository, request.getPhone());
            customer.setPhone(request.getPhone().strip());
        }
    }

    public static void updateCustomerDetail(CustomerDto request, CustomerDetail customerDetail) {
        if (Validator.NOT_NULL_NOT_BLANK(request.getFirstName())) {
            customerDetail.setFirstName(request.getFirstName().strip());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getLastName())) {
            customerDetail.setLastName(request.getLastName().strip());
        }
    }

    public static void updateProductDetail(ProductDto request, ProductDetail productDetail) {
        if (Validator.NOT_NULL_NOT_BLANK(request.getName())) {
            productDetail.setName(request.getName().strip());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getDescription())) {
            productDetail.setDescription(request.getDescription().strip());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getAbout())) {
            productDetail.setAbout(request.getAbout().strip());
        }
    }

    public static void updateProduct(ProductDto request, Product product, Category category) {
        if (Validator.NOT_NULL_NOT_NEGATIVE(request.getStockQuantity())) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if (Validator.NOT_NULL_NOT_NEGATIVE(request.getPrice())) {
            product.setPrice(request.getPrice());
        }
        if (category != null) {
            product.setCategory(category);
        }
        if (Validator.NOT_NULL_NEGATIVE_ONE(request.getAfterDiscount())) {
            if (request.getAfterDiscount() == -1) {
                product.setAfterDiscount(0D);
                product.setDiscount(false);
            } else {
                product.setAfterDiscount(request.getAfterDiscount());
                product.setDiscount(true);
            }
        }
    }

    public static void updateCategoryDetail(CategoryDto request, CategoryDetail categoryDetail) {
        if (Validator.NOT_NULL_NOT_BLANK(request.getName())) {
            categoryDetail.setName(request.getName().strip());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getDescription())) {
            categoryDetail.setDescription(request.getDescription().strip());
        }
    }

    public static void updatePostDetails(BlogPostDto request, BlogPostDetail postDetail) {
        if (Validator.NOT_NULL_NOT_BLANK(request.getTitle())) {
            postDetail.setTitle(request.getTitle());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getContent())) {
            postDetail.setContent(request.getContent());
        }
    }

    public static void updateAdvertisementDetail(AdvertisementDto request, AdvertisementDetail adDetail) {
        if (Validator.NOT_NULL_NOT_BLANK(request.getTitle())) {
            adDetail.setTitle(request.getTitle());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getDescription())) {
            adDetail.setDescription(request.getDescription());
        }
    }

    public static void updateAdvertisement(AdvertisementDto request, Advertisement advertisement) {
        if (Validator.URL(request.getTargetUrl())) {
            advertisement.setTargetUrl(request.getTargetUrl());
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getStartDate())) {
            advertisement.setStartDate(DateTimeUtils.getDateTime(request.getStartDate()));
        }
        if (Validator.NOT_NULL_NOT_BLANK(request.getEndDate())) {
            advertisement.setEndDate(DateTimeUtils.getDateTime(request.getEndDate()));
        }
    }

    public static void updateProductRate(Product product) {
        Double ratingAverage = product.getReviews().stream().mapToDouble(Review::getRating).average().orElse(0);
        product.setRating(ratingAverage);
    }

    public static void updateNotificationMessage(String message, NotificationDetail notificationDetail) {
        if (Validator.NOT_NULL_NOT_BLANK(message)) {
            notificationDetail.setMessage(message);
        }
    }
}
