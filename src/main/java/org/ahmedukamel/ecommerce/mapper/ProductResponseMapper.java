package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.response.ProductResponse;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.model.ProductDetail;
import org.ahmedukamel.ecommerce.model.Tag;
import org.ahmedukamel.ecommerce.util.LocaleContextUtils;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class ProductResponseMapper implements Function<Product, ProductResponse> {
    @Override
    public ProductResponse apply(Product product) {
        String languageCode = LocaleContextUtils.getLanguage();

        ProductDetail details = product.getProductDetails()
                .stream()
                .filter(productDetail -> productDetail.getLanguage().getCode().equalsIgnoreCase(languageCode))
                .findFirst()
                .orElseGet(ProductDetail::new);

        Predicate<Tag> tagFilter = tag -> tag.getLanguage().getCode().equalsIgnoreCase(languageCode);

        ProductResponse response = new ProductResponse();

        response.setName(details.getName());
        response.setDescription(details.getDescription());
        response.setAbout(details.getAbout());

        response.setProductId(product.getProductId());
        response.setCategoryId(product.getCategory().getCategoryId());
        response.setMainCategoryId(product.getCategory().getMainCategory().getCategoryId());
        response.setStockQuantity(product.getStockQuantity());
        response.setReviews(product.getReviews().size());

        response.setPriceBeforeDiscount(product.getPrice());
        response.setPriceAfterDiscount(product.getAfterDiscount());
        response.setRating(product.getRating());
        response.setWeight(product.getWeight());

        response.setDiscount(product.getDiscount());

        response.setPictures(product.getPictures());
        response.setTags(product.getTags()
                .stream()
                .filter(tagFilter)
                .map(Tag::getName)
                .toList()
        );

        return response;
    }
}