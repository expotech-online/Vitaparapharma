package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.ProductDto;
import org.ahmedukamel.ecommerce.dto.ProductDtoV2;
import org.ahmedukamel.ecommerce.dto.ProductDtoV3;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.model.ProductDetail;
import org.ahmedukamel.ecommerce.model.Tag;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.util.EntityDetailsUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ProductMapper {
    public static ProductDto toResponse(Product product, Integer languageId) {
        ProductDetail productDetail = EntityDetailsUtils.supplyProductDetail(product, languageId);
        ProductDto response = new ProductDto();
        response.setProductId(product.getProductId());
        response.setName(productDetail.getName());
        response.setRating(product.getRating());
        response.setDescription(productDetail.getDescription());
        response.setAbout(productDetail.getAbout());
        response.setPrice(product.getPrice());
        response.setAfterDiscount(product.getAfterDiscount());
        response.setDiscount(product.getDiscount());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategoryId(product.getCategory().getCategoryId());
        response.setPictures(product.getPictures());
        response.setReviews(product.getReviews().size());
        response.setInCart(false);
        response.setInWishlist(false);
        return response;
    }

    public static ProductDto toResponse(Product product, Integer languageId, List<Integer> cartProductIds, List<Integer> wishlistProductId) {
        ProductDto response = toResponse(product, languageId);
        response.setInCart(cartProductIds.stream().anyMatch(i -> i.equals(product.getProductId())));
        response.setInWishlist(wishlistProductId.stream().anyMatch(i -> i.equals(product.getProductId())));
        return response;
    }

    public static ProductDto toResponse(Product product, Integer languageId, boolean inCart, boolean inWishlist) {
        ProductDto response = toResponse(product, languageId);
        response.setInCart(inCart);
        response.setInWishlist(inWishlist);
        return response;
    }

    public static List<ProductDto> toResponse(Collection<Product> products, Integer languageId, List<Integer> cartProductIds, List<Integer> wishlistProductIds, Boolean active) {
        return products.stream()
                .filter(product -> product.getActive().equals(active))
                .sorted(Comparator.comparing(Product::getRating).reversed())
                .map(product -> ProductMapper.toResponse(product, languageId, cartProductIds, wishlistProductIds))
                .toList();
    }

    public static List<ProductDto> toResponse(Collection<Product> items, Integer languageId, Boolean active) {
        return items.stream()
                .filter(product -> product.getActive().equals(active))
                .sorted(Comparator.comparing(Product::getRating).reversed())
                .map(product -> ProductMapper.toResponse(product, languageId))
                .toList();
    }

    public static ProductDtoV2 toResponseV2(Product product, LanguageRepository repository) {
        ProductDetail englishProductDetail = EntityDetailsUtils.supplyProductDetail(product, RepositoryUtils.getLanguage(repository, "en").getLanguageId());
        ProductDetail arabicProductDetail = EntityDetailsUtils.supplyProductDetail(product, RepositoryUtils.getLanguage(repository, "ar").getLanguageId());
        ProductDetail frenchProductDetail = EntityDetailsUtils.supplyProductDetail(product, RepositoryUtils.getLanguage(repository, "fr").getLanguageId());
        ProductDtoV2 response = new ProductDtoV2();
        response.setProductId(product.getProductId());
        response.setName(englishProductDetail.getName());
        response.setRating(product.getRating());
        response.setDescription(englishProductDetail.getDescription());
        response.setAbout(englishProductDetail.getAbout());
        response.setPrice(product.getPrice());
        response.setAfterDiscount(product.getAfterDiscount());
        response.setDiscount(product.getDiscount());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategoryId(product.getCategory().getCategoryId());
        response.setPictures(product.getPictures());
        response.setReviews(product.getReviews().size());
        response.setInCart(false);
        response.setInWishlist(false);
        response.setArabicName(arabicProductDetail.getName());
        response.setArabicAbout(arabicProductDetail.getAbout());
        response.setArabicDescription(arabicProductDetail.getDescription());
        response.setFrenchName(frenchProductDetail.getName());
        response.setFrenchDescription(frenchProductDetail.getDescription());
        response.setFrenchAbout(frenchProductDetail.getAbout());
        return response;
    }

    public static ProductDtoV3 toResponseV3(Product product, Integer languageId) throws CloneNotSupportedException {
        ProductDtoV3 productDtoV3 = new ProductDtoV3();
        BeanUtils.copyProperties(toResponse(product, languageId), productDtoV3);
        return toResponseV3(product, productDtoV3);
    }

    public static ProductDtoV3 toResponseV3(Product product, Integer languageId, List<Integer> cartProductIds, List<Integer> wishlistProductId) throws CloneNotSupportedException {
        ProductDtoV3 productDtoV3 = new ProductDtoV3();
        BeanUtils.copyProperties(toResponse(product, languageId, cartProductIds, wishlistProductId), productDtoV3);
        return toResponseV3(product, productDtoV3);
    }

    public static ProductDtoV3 toResponseV3(Product product, Integer languageId, boolean inCart, boolean inWishlist) {
        ProductDtoV3 productDtoV3 = new ProductDtoV3();
        BeanUtils.copyProperties(toResponse(product, languageId, inCart, inWishlist), productDtoV3);
        return toResponseV3(product, productDtoV3);
    }

    public static List<ProductDtoV3> toResponseV3(Collection<Product> products, Integer languageId, List<Integer> cartProductIds, List<Integer> wishlistProductIds, Boolean active) {
        return products.stream()
                .filter(product -> product.getActive().equals(active))
                .sorted(Comparator.comparing(Product::getRating).reversed())
                .map(product -> {
                    try {
                        return ProductMapper.toResponseV3(product, languageId, cartProductIds, wishlistProductIds);
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    public static List<ProductDtoV3> toResponseV3(Collection<Product> items, Integer languageId, Boolean active) {
        return items.stream()
                .filter(product -> product.getActive().equals(active))
                .sorted(Comparator.comparing(Product::getRating).reversed())
                .map(product -> {
                    try {
                        return ProductMapper.toResponseV3(product, languageId);
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    private static ProductDtoV3 toResponseV3(Product product, ProductDtoV3 productDtoV3) {
        productDtoV3.setMainCategoryId(product.getCategory().getMainCategory().getCategoryId());
        productDtoV3.setTags(product.getTags().stream().map(Tag::getName).toList());
        return productDtoV3;
    }
}