package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.ProductDtoV4;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.model.ProductDetail;
import org.ahmedukamel.ecommerce.model.Tag;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Component
public class ProductDtoV4Mapper implements Function<Product, ProductDtoV4> {
    @Override
    public ProductDtoV4 apply(Product product) {
        Function<String, ProductDetail> getDetails = languageCode -> product.getProductDetails()
                .stream()
                .filter(productDetail -> productDetail.getLanguage().getCode().equalsIgnoreCase(languageCode))
                .findFirst().orElseGet(ProductDetail::new);

        Function<String, List<String>> getTags = languageCode -> product.getTags()
                .stream()
                .filter(tag -> tag.getLanguage().getCode().equalsIgnoreCase(languageCode))
                .map(Tag::getName)
                .toList();

        ProductDtoV4 dto = new ProductDtoV4();

        dto.setPriceBeforeDiscount(product.getPrice());
        dto.setPriceAfterDiscount(product.getAfterDiscount());
        dto.setWeight(product.getWeight());

        dto.setActive(product.getActive());
        dto.setDiscount(product.getDiscount());

        dto.setStockQuantity(product.getStockQuantity());
        dto.setProductId(product.getProductId());
        dto.setCategoryId(product.getCategory().getCategoryId());

        dto.setPictures(product.getPictures());
        dto.setEnglishTags(getTags.apply("en"));
        dto.setArabicTags(getTags.apply("ar"));
        dto.setFrenchTags(getTags.apply("fr"));

        ProductDetail details = getDetails.apply("en");
        dto.setEnglishName(details.getName());
        dto.setEnglishDescription(details.getDescription());
        dto.setEnglishAbout(details.getAbout());

        details = getDetails.apply("ar");
        dto.setArabicName(details.getName());
        dto.setArabicDescription(details.getDescription());
        dto.setArabicAbout(details.getAbout());

        details = getDetails.apply("fr");
        dto.setFrenchName(details.getName());
        dto.setFrenchDescription(details.getDescription());
        dto.setFrenchAbout(details.getAbout());

        return dto;
    }
}