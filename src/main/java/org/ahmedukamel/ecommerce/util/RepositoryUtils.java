package org.ahmedukamel.ecommerce.util;

import org.ahmedukamel.ecommerce.exception.EntityNotFoundException;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.ahmedukamel.ecommerce.repository.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RepositoryUtils {
    public static Coupon getCoupon(CouponRepository repository, String code) {
        return repository.findById(code.strip())
                .orElseThrow(() -> new EntityNotFoundException(code, Customer.class));
    }

    public static Customer getCustomer(CustomerRepository repository, Integer customerId) {
        return repository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(customerId.toString(), Customer.class));
    }

    public static Customer getCustomer(CustomerRepository repository, String email, Provider provider) {
        return repository.findByEmailIgnoreCaseAndProvider(email, provider)
                .orElseThrow(() -> new EntityNotFoundException(email, Customer.class));
    }

    public static Region getRegion(RegionRepository repository, Integer regionId) {
        return repository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException(regionId.toString(), Customer.class));
    }

    public static Token getToken(UUID tokenId, TokenRepository repository) {
        return repository.findById(tokenId)
                .orElseThrow(() -> new EntityNotFoundException(tokenId.toString(), Token.class));
    }

    public static Customer getCustomerByEmailOrPhone(CustomerRepository repository, String email) {
        return repository.findByEmailIgnoreCaseAndProviderOrPhone(email.toLowerCase(), Provider.NONE, email)
                .orElseThrow(() -> new EntityNotFoundException(email, Customer.class));
    }

    public static Product getProduct(ProductRepository repository, Integer productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(productId.toString(), Product.class));
    }

    public static Language getLanguage(LanguageRepository repository, String languageCode) {
        return repository.findByCodeIgnoreCase(languageCode.strip())
                .orElseThrow(() -> new EntityNotFoundException(languageCode, Language.class));
    }

    public static Notification getNotification(NotificationRepository repository, Integer notificationId) {
        return repository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException(notificationId.toString(), Notification.class));
    }

    public static Advertisement getAdvertisement(AdvertisementRepository repository, Integer advertisementId) {
        return repository.findById(advertisementId)
                .orElseThrow(() -> new EntityNotFoundException(advertisementId.toString(), Advertisement.class));
    }

    public static Order getOrder(OrderRepository repository, Integer orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(orderId.toString(), Order.class));
    }

    public static Order getOrder(Customer customer, Integer orderId) {
        return customer.getOrders().stream().filter(order -> order.getOrderId().equals(orderId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(orderId.toString(), Order.class));
    }

    public static Review getReview(ReviewRepository repository, Integer reviewId) {
        return repository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(reviewId.toString(), Review.class));
    }

    public static MainCategory getMainCategory(MainCategoryRepository repository, Integer mainCategoryId) {
        return repository.findById(mainCategoryId)
                .orElseThrow(() -> new EntityNotFoundException(mainCategoryId.toString(), MainCategory.class));
    }

    public static BlogPost getPost(BlogPostRepository repository, Integer postId) {
        return repository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(postId.toString(), BlogPost.class));
    }

    public static Category getCategory(CategoryRepository repository, Integer categoryId) {
        return repository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(categoryId.toString(), Category.class));
    }

    public static List<Customer> getCustomers(CustomerRepository repository, Collection<Integer> customerIdList) {
        return customerIdList.stream().map(repository::findById).filter(Optional::isPresent).map(Optional::get).toList();
    }
}