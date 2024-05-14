package org.ahmedukamel.ecommerce.service.product;

import org.ahmedukamel.ecommerce.dto.ProductDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductServiceV2 {
    ApiResponse addProduct(String englishName, String arabicName, String frenchName,
                           String englishDescription, String arabicDescription, String frenchDescription,
                           String englishAbout, String frenchAbout, String arabicAbout,
                           Integer categoryId, Double price, Double weight, Double priceAfterDiscount, Boolean isDiscount,
                           Integer stockQuantity, MultipartFile image1, MultipartFile image2,
                           MultipartFile image3, MultipartFile image4, MultipartFile image5) throws IOException;

    ApiResponse updateProduct(ProductDtoV2 request, Integer productId);

    ApiResponse getProduct(Integer productId);
}
