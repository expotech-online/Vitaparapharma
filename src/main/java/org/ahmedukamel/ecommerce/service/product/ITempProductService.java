package org.ahmedukamel.ecommerce.service.product;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.enumeration.FilterType;

import java.util.List;

public interface ITempProductService {
    ApiResponse getProductById(int productId);

    ApiResponse getAllProducts(long pageSize, long pageNumber);

    ApiResponse getAllProductsPriceFilter(double value, FilterType type, long pageSize, long pageNumber);

    ApiResponse getAllProductsRateFilter(double value, FilterType type, long pageSize, long pageNumber);

    ApiResponse getAllOfferProducts(long pageSize, long pageNumber);

    ApiResponse getAllProductsFiltered(Double priceValue, Double maxPriceValue, FilterType priceFilter, Double ratingValue, FilterType ratingFilter, List<Integer> categoryIdList, long pageSize, long pageNumber);

    ApiResponse getProductsByCategory(int categoryId, long pageSize, long pageNumber);

    ApiResponse getProductsByMainCategory(int mainCategoryId, long pageSize, long pageNumber);

    ApiResponse getProductByIdForUser(int productId);

    ApiResponse getAllProductsForUser(long pageSize, long pageNumber);

    ApiResponse getAllProductsFilteredForUser(Double priceValue, Double maxPriceValue, FilterType priceFilter, Double ratingValue, FilterType ratingFilter, List<Integer> categoryIdList, long pageSize, long pageNumber);

    ApiResponse getAllProductsPriceFilterForUser(double value, FilterType type, long pageSize, long pageNumber);

    ApiResponse getAllProductsRateFilterForUser(double value, FilterType type, long pageSize, long pageNumber);

    ApiResponse getAllOfferProductsForUser(long pageSize, long pageNumber);

    ApiResponse getProductsByCategoryForUser(int categoryId, long pageSize, long pageNumber);

    ApiResponse getProductsByMainCategoryForUser(int mainCategoryId, long pageSize, long pageNumber);

    ApiResponse getMinMaxPrice();
}