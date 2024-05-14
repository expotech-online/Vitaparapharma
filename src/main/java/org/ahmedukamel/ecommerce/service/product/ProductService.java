package org.ahmedukamel.ecommerce.service.product;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ApiResponse addProduct(ProductDto request);

    ApiResponse updateProduct(ProductDto request, Integer productId);

    ApiResponse uploadProductImage(Integer productId, MultipartFile file) throws IOException;

    ApiResponse deleteProductImage(Integer productId, String pictureName);

    ApiResponse getProduct(Integer productId);

    ApiResponse activateProduct(Integer productId);

    ApiResponse deactivateProduct(Integer productId);

    ApiResponse getInactiveProducts();

    ApiResponse getActiveProducts();

    ApiResponse getCategoryProducts(Integer categoryId);

    ApiResponse getMainCategoryProducts(Integer mainCategoryId);
}