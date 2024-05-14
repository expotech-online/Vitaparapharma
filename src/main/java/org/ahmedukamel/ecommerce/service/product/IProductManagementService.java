package org.ahmedukamel.ecommerce.service.product;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IProductManagementService {
    ApiResponse addProduct(Object object, MultipartFile[] images);

    ApiResponse updateProduct(int productId, Object object);

    ApiResponse getProduct(int productId);

    ApiResponse getProducts(long pageSize, long pageNumber);

    ApiResponse getProductsByActiveStatus(boolean active, long pageSize, long pageNumber);

    ApiResponse setProductActiveStatus(int productId, boolean active);

    ApiResponse uploadImages(int productId, MultipartFile[] images);

    ApiResponse removeImage(int productId, String imageName);
}