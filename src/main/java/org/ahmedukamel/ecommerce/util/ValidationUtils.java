package org.ahmedukamel.ecommerce.util;

import org.ahmedukamel.ecommerce.dto.CategoryDto;
import org.ahmedukamel.ecommerce.dto.ProductDto;
import org.ahmedukamel.ecommerce.dto.request.OrderRequest;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.exception.EntityNotFoundException;
import org.ahmedukamel.ecommerce.exception.InsufficientAmountException;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.CouponType;
import org.ahmedukamel.ecommerce.model.enumeration.OrderStatus;
import org.ahmedukamel.ecommerce.model.enumeration.TokenType;
import org.ahmedukamel.ecommerce.repository.CategoryRepository;
import org.ahmedukamel.ecommerce.repository.MainCategoryRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public class ValidationUtils {
    private static final int MAX_ADDRESSES_NUMBER = 5;

    public static void validCustomerReport(Customer customer, Integer reviewId, MessageSourceUtils messageSourceUtils) {
        if (customer.getReports().stream().anyMatch(report -> report.getReview().getReviewId().equals(reviewId))) {
            String message = messageSourceUtils.getMessage("operation.failed.exist.review.report");
            throw new CustomException(message);
        }
    }

    public static void nonRevokedCoupon(Coupon coupon) {
        if (coupon.isRevoked()) throw new CustomException("Already revoked coupon.");
    }

    public static void revokedCoupon(Coupon coupon) {
        if (coupon.isRevoked()) return;
        throw new CustomException("Already non revoked coupon.");
    }

    public static void validateCouponType(Coupon coupon, ProductRepository repository, MessageSourceUtils messageSourceUtils) {
        if (coupon.getCouponType().equals(CouponType.PRODUCT) && !repository.existsById(coupon.getProductId())) {
            String message = "Product id not found!";
//            String message = messageSourceUtils.getMessage("operation.failed.exist.review.report");
            throw new CustomException(message);
        }
    }

    public static void validateItem(Product product, Integer quantity, List<String> insufficientList, MessageSourceUtils messageSourceUtils) {
        if (product.getStockQuantity().compareTo(quantity) < 0) {
            String message = messageSourceUtils.getMessage("operation.failed.insufficient.order.item.quantity", product.getProductId(), quantity);
            insufficientList.add(message);
        }
    }

    public static Review validateGetCustomerReview(Customer customer, Integer reviewId, MessageSourceUtils messageSourceUtils) {
        Optional<Review> optionalReview = customer.getReviews().stream().filter(i -> i.getReviewId().equals(reviewId)).findFirst();
        if (optionalReview.isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.not.exist.customer.review");
            throw new CustomException(message);
        }
        return optionalReview.get();
    }

    public static void validateOrderItems(Collection<? extends OrderItemInterface> items, MessageSourceUtils messageSourceUtils) {
        if (items.isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.empty.order.items");
            throw new CustomException(message);
        }
        List<String> insufficientQuantityList = new ArrayList<>();
        items.forEach(ci -> validateItem(ci.getProduct(), ci.getQuantity(), insufficientQuantityList, messageSourceUtils));
        if (!insufficientQuantityList.isEmpty()) {
            throw new InsufficientAmountException(insufficientQuantityList, Product.class);
        }
    }

    public static void validateCancelOrder(Order order, MessageSourceUtils messageSourceUtils) {
        if (order.getStatus().equals(OrderStatus.CANCELLED)) {
            String message = messageSourceUtils.getMessage("operation.failed.canceled.order");
            throw new CustomException(message);
        }
        if (order.getStatus().equals(OrderStatus.DELIVERED)) {
            String message = messageSourceUtils.getMessage("operation.failed.delivered.order");
            throw new CustomException(message);
        }
        if (!order.getStatus().equals(OrderStatus.PREPARED)) {
            String message = messageSourceUtils.getMessage("operation.failed.not.prepared.order");
            throw new CustomException(message);
        }
    }

    public static void validateExistCustomerOrder(Customer customer, Order order, MessageSourceUtils messageSourceUtils) {
        if (!customer.getOrders().contains(order)) {
            String message = messageSourceUtils.getMessage("operation.failed.not.exist.customer.order");
            throw new CustomException(message);
        }
    }


    public static void validateEmptyCategory(Collection<Product> products, MessageSourceUtils messageSourceUtils) {
        if (!products.isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.not.empty.category");
            throw new CustomException(message);
        }
    }

    public static void validateExistCustomerDemand(Customer customer, Integer productId, MessageSourceUtils messageSourceUtils) {
        if (customer.getDemands().stream().anyMatch(item -> item.getProduct().getProductId().equals(productId))) {
            String message = messageSourceUtils.getMessage("operation.failed.exist.demand");
            throw new CustomException(message);
        }
    }

    public static MainCategory validateGetMainCategory(MainCategoryRepository repository, CategoryDto request) {
        if (Validator.NOT_NULL_NOT_NEGATIVE(request.getMainCategoryId())) {
            return RepositoryUtils.getMainCategory(repository, request.getMainCategoryId());
        }
        return null;
    }

    public static void validateNotificationMessage(String notificationMessage, MessageSourceUtils messageSourceUtils) {
        if (!Validator.NOT_NULL_NOT_BLANK(notificationMessage)) {
            String message = messageSourceUtils.getMessage("operation.failed.invalid.notification.message");
            throw new CustomException(message);
        }
    }

    public static Address validateGetCustomerAddress(Customer customer, Integer addressId, MessageSourceUtils messageSourceUtils) {
        Optional<Address> optionalAddress = customer.getAddresses().stream().filter(a -> a.getAddressId().equals(addressId)).findFirst();
        if (optionalAddress.isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.invalid.customer.address");
            throw new CustomException(message);
        }
        return optionalAddress.get();
    }

    public static void validateAddressesNumber(Customer customer, MessageSourceUtils messageSourceUtils) {
        long addresses = customer.getAddresses().stream().filter(Address::getActive).count();
        if (addresses >= MAX_ADDRESSES_NUMBER) {
            String message = messageSourceUtils.getMessage("operation.failed.max.customer.addresses");
            throw new CustomException(message);
        }
    }

    public static void validatePrepareRequestItems(ProductRepository productRepository, OrderRequest request) {
        request.getOrderItems().forEach(item -> item.setProduct(RepositoryUtils.getProduct(productRepository, item.getProductId())));
    }

    private static Optional<WishlistItem> getOptionalWishlistItem(List<WishlistItem> items, Integer productId) {
        return items.stream().filter(wi -> wi.getProduct().getProductId().equals(productId)).findFirst();
    }

    public static void validateExistWishlistItem(Customer customer, Integer productId, MessageSourceUtils messageSourceUtils) {
        if (getOptionalWishlistItem(customer.getWishlist().getWishlistItems(), productId).isPresent()) {
            String message = messageSourceUtils.getMessage("operation.failed.exist.wishlist.item");
            throw new CustomException(message);
        }
    }

    public static WishlistItem validaGetWishlistItem(List<WishlistItem> items, Integer productId, MessageSourceUtils messageSourceUtils) {
        Optional<WishlistItem> optionalWishlistItem = getOptionalWishlistItem(items, productId);
        if (optionalWishlistItem.isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.not.exist.wishlist.item");
            throw new CustomException(message);
        }
        return optionalWishlistItem.get();
    }

    public static Like validateGetCustomerLike(Customer customer, Integer postId, MessageSourceUtils messageSourceUtils) {
        Optional<Like> likeOptional = getLike(customer, postId);
        if (likeOptional.isPresent() && likeOptional.get().getLike()) {
            String message = messageSourceUtils.getMessage("operation.failed.exist.post.like");
            throw new CustomException(message);
        }
        return likeOptional.orElse(new Like());
    }

    public static Like validateGetCustomerUnlike(Customer customer, Integer postId, MessageSourceUtils messageSourceUtils) {
        Optional<Like> optionalLike = getLike(customer, postId);
        if (optionalLike.isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.not.exist.post.like");
            throw new CustomException(message);
        }
        return optionalLike.get();
    }

    public static Like validateGetCustomerDislike(Customer customer, Integer postId, MessageSourceUtils messageSourceUtils) {
        Optional<Like> likeOptional = getLike(customer, postId);
        if (likeOptional.isPresent() && !likeOptional.get().getLike()) {
            String message = messageSourceUtils.getMessage("operation.failed.exist.post.dislike");
            throw new CustomException(message);
        }
        return likeOptional.orElse(new Like());
    }

    private static Optional<Like> getLike(Customer customer, Integer postId) {
        return customer.getLikes().stream().filter(like -> like.getBlogPost().getBlogPostId().equals(postId)).findFirst();
    }

    public static void validateLockedCustomer(Customer customer, MessageSourceUtils messageSourceUtils) {
        if (customer.isAccountNonLocked()) {
            String message = messageSourceUtils.getMessage("operation.failed.enabled.user");
            throw new CustomException(message);
        }
    }

    public static void validateNonLockedCustomer(Customer customer, MessageSourceUtils messageSourceUtils) {
        if (customer.isAccountNonLocked()) {
            return;
        }
        String message = messageSourceUtils.getMessage("operation.failed.disabled.user");
        throw new CustomException(message);
    }

    public static void validateMainCategoryCategories(MainCategory mainCategory, MessageSourceUtils messageSourceUtils) {
        if (!mainCategory.getCategories().isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.delete.main.category");
            throw new CustomException(message);
        }
    }

    public static void validateActiveAddress(Address address) {
        if (!address.getActive()) {
            throw new EntityNotFoundException(address.getAddressId().toString(), Address.class);
        }
    }

    public static void validateProductPictures(Product product, MessageSourceUtils messageSourceUtils) {
        if (product.getPictures().size() >= 5) {
            String message = messageSourceUtils.getMessage("operation.failed.max.product.pictures");
            throw new CustomException(message);
        }
    }

    public static Category validateGetCategory(CategoryRepository repository, ProductDto request) {
        if (Validator.NOT_NULL_NOT_NEGATIVE(request.getCategoryId())) {
            return RepositoryUtils.getCategory(repository, request.getCategoryId());
        }
        return null;
    }

    public static String validateGetProductPicture(Product product, String imageName, MessageSourceUtils messageSourceUtils) {
        Optional<String> target = product.getPictures().stream().filter(i -> i.contains(imageName)).findFirst();
        if (target.isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.not.product.picture");
            throw new CustomException(message);
        }
        return target.get();
    }

    public static void validateNotExistAdvertisementPicture(Advertisement advertisement, MessageSourceUtils messageSourceUtils) {
        if (advertisement.getImgUrl() != null && !advertisement.getImgUrl().isBlank()) {
            String message = messageSourceUtils.getMessage("operation.failed.not.empty.advertisement.picture");
            throw new CustomException(message);
        }
    }

    public static void validateExistAdvertisementPicture(Advertisement advertisement, MessageSourceUtils messageSourceUtils) {
        if (advertisement.getImgUrl() == null || advertisement.getImgUrl().isBlank()) {
            String message = messageSourceUtils.getMessage("operation.failed.empty.advertisement.picture");
            throw new CustomException(message);
        }
    }

    public static void validateImage(MultipartFile image, MessageSourceUtils messageSourceUtils) {
        if (image == null) {
            String message = messageSourceUtils.getMessage("operation.failed.empty.picture");
            throw new CustomException(message);
        }
    }

    public static void validateNotExistPostPicture(BlogPost post, MessageSourceUtils messageSourceUtils) {
        if (post.getPictureUrl() != null && !post.getPictureUrl().isBlank()) {
            String message = messageSourceUtils.getMessage("operation.failed.not.empty.post.picture");
            throw new CustomException(message);
        }
    }

    public static void validateExistPostPicture(BlogPost post, MessageSourceUtils messageSourceUtils) {
        if (post.getPictureUrl() == null || post.getPictureUrl().isBlank()) {
            String message = messageSourceUtils.getMessage("operation.failed.empty.post.picture");
            throw new CustomException(message);
        }
    }

    public static void validateNotNullCustomerPhone(Customer customer, MessageSourceUtils messageSourceUtils) {
        if (customer.getPhone() == null || customer.getPhone().isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.order.null.user.phone");
            throw new CustomException(message);
        }
    }

    public static void validateInactiveProduct(Product product, MessageSourceUtils messageSourceUtils) {
        if (product.getActive()) {
            String message = messageSourceUtils.getMessage("operation.failed.product.active");
            throw new CustomException(message);
        }
    }

    public static void validateActiveProduct(Product product, MessageSourceUtils messageSourceUtils) {
        if (!product.getActive()) {
            String message = messageSourceUtils.getMessage("operation.failed.product.inactive");
            throw new CustomException(message);
        }
    }

    public static void validateForgetPasswordToken(Token token, MessageSourceUtils messageSourceUtils) {
        if (!token.getTokenType().equals(TokenType.FORGET_PASSWORD)) {
            String message = messageSourceUtils.getMessage("operation.failed.invalid.token.type");
            throw new CustomException(message);
        }

        validateToken(token, messageSourceUtils);
    }

    public static void validateActivateAccountToken(Token token, MessageSourceUtils messageSourceUtils) {
        if (!token.getTokenType().equals(TokenType.ACTIVATE_ACCOUNT)) {
            String message = messageSourceUtils.getMessage("operation.failed.invalid.token.type");
            throw new CustomException(message);
        }

        validateToken(token, messageSourceUtils);
    }

    public static void validateToken(Token token, MessageSourceUtils messageSourceUtils) {
        if (token.getUsed()) {
            String message = messageSourceUtils.getMessage("operation.failed.used.token");
            throw new CustomException(message);
        }
        if (token.getRevoked()) {
            String message = messageSourceUtils.getMessage("operation.failed.revoked.token");
            throw new CustomException(message);
        }
        if (token.getExpiration().before(new Date())) {
            String message = messageSourceUtils.getMessage("operation.failed.expired.token");
            throw new CustomException(message);
        }
    }

    public static void validateCustomerToken(Optional<Token> optionalToken, Integer customerId, MessageSourceUtils messageSourceUtils) {
        if (optionalToken.isEmpty()) {
            String message = messageSourceUtils.getMessage("operation.failed.not.found.token");
            throw new CustomException(message);
        }

        Token token = optionalToken.get();
        if (!token.getUserId().equals(customerId)) {
            String message = messageSourceUtils.getMessage("operation.failed.not.user.token");
            throw new CustomException(message);
        }
    }
}
