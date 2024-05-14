package org.ahmedukamel.ecommerce.service.product;

import jakarta.transaction.Transactional;
import org.ahmedukamel.ecommerce.dto.ProductDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.ProductMapper;
import org.ahmedukamel.ecommerce.model.Category;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.model.ProductDetail;
import org.ahmedukamel.ecommerce.repository.CategoryRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.service.product.ProductService;
import org.ahmedukamel.ecommerce.service.product.ProductServiceV2;
import org.ahmedukamel.ecommerce.util.EntityDetailsUtils;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
public class
ProductServiceV2Impl implements ProductServiceV2 {
    private final MessageSourceUtils messageSourceUtils;
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductServiceV2Impl(MessageSourceUtils messageSourceUtils,
                                CategoryRepository categoryRepository,
                                LanguageRepository languageRepository,
                                ProductRepository productRepository,
                                @Qualifier("productServiceImpl") ProductService productService) {
        this.messageSourceUtils = messageSourceUtils;
        this.categoryRepository = categoryRepository;
        this.languageRepository = languageRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @Override
    public ApiResponse addProduct(String englishName,
                                  String arabicName,
                                  String frenchName,
                                  String englishDescription,
                                  String arabicDescription,
                                  String frenchDescription,
                                  String englishAbout,
                                  String frenchAbout,
                                  String arabicAbout,
                                  Integer categoryId,
                                  Double price,
                                  Double weight,
                                  Double priceAfterDiscount,
                                  Boolean isDiscount,
                                  Integer stockQuantity,
                                  MultipartFile image1,
                                  MultipartFile image2,
                                  MultipartFile image3,
                                  MultipartFile image4,
                                  MultipartFile image5) throws IOException {
        Category category = RepositoryUtils.getCategory(categoryRepository, categoryId);

        Product product = new Product();
        ProductDetail englishProduct = new ProductDetail();
        ProductDetail arabicProduct = new ProductDetail();
        ProductDetail frenchProduct = new ProductDetail();

        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setDiscount(isDiscount);
        product.setAfterDiscount(priceAfterDiscount);
        product.setRating(0D);
        product.setActive(true);
        product.setCategory(category);
        product.setWeight(weight);

        englishProduct.setName(englishName);
        englishProduct.setDescription(englishDescription);
        englishProduct.setAbout(englishAbout);
        englishProduct.setLanguage(RepositoryUtils.getLanguage(languageRepository, "en"));
        englishProduct.setProduct(product);

        arabicProduct.setName(arabicName);
        arabicProduct.setDescription(arabicDescription);
        arabicProduct.setAbout(arabicAbout);
        arabicProduct.setLanguage(RepositoryUtils.getLanguage(languageRepository, "ar"));
        arabicProduct.setProduct(product);

        frenchProduct.setName(frenchName);
        frenchProduct.setDescription(frenchDescription);
        frenchProduct.setAbout(frenchAbout);
        frenchProduct.setLanguage(RepositoryUtils.getLanguage(languageRepository, "fr"));
        frenchProduct.setProduct(product);

        product.getProductDetails().add(englishProduct);
        product.getProductDetails().add(arabicProduct);
        product.getProductDetails().add(frenchProduct);

        Product addedProduct = productRepository.save(product);

        if (image1 != null && !image1.isEmpty()) productService.uploadProductImage(addedProduct.getProductId(), image1);
        if (image2 != null && !image2.isEmpty()) productService.uploadProductImage(addedProduct.getProductId(), image2);
        if (image3 != null && !image3.isEmpty()) productService.uploadProductImage(addedProduct.getProductId(), image3);
        if (image4 != null && !image4.isEmpty()) productService.uploadProductImage(addedProduct.getProductId(), image4);
        if (image5 != null && !image5.isEmpty()) productService.uploadProductImage(addedProduct.getProductId(), image5);

        String message = messageSourceUtils.getMessage("operation.successful.add.product");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse updateProduct(ProductDtoV2 request, Integer productId) {
        Category category = RepositoryUtils.getCategory(categoryRepository, request.getCategoryId());
        Product product = RepositoryUtils.getProduct(productRepository, productId);

        Language englishLanguage = RepositoryUtils.getLanguage(languageRepository, "en");
        Language arabicLanguage = RepositoryUtils.getLanguage(languageRepository, "ar");
        Language frenchLanguage = RepositoryUtils.getLanguage(languageRepository, "fr");

        ProductDetail englishProduct = EntityDetailsUtils.supplyProductDetail(product, englishLanguage.getLanguageId());
        ProductDetail arabicProduct = EntityDetailsUtils.supplyProductDetail(product, arabicLanguage.getLanguageId());
        ProductDetail frenchProduct = EntityDetailsUtils.supplyProductDetail(product, frenchLanguage.getLanguageId());

        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        Boolean isDiscount = request.getDiscount() != null && request.getDiscount();
        product.setDiscount(isDiscount);
        product.setAfterDiscount(request.getAfterDiscount());
        product.setCategory(category);
        product.setWeight(request.getWeight());

        englishProduct.setName(request.getName());
        englishProduct.setDescription(request.getDescription());
        englishProduct.setAbout(request.getAbout());
        englishProduct.setLanguage(englishLanguage);
        englishProduct.setProduct(product);

        arabicProduct.setName(request.getArabicName());
        arabicProduct.setDescription(request.getArabicDescription());
        arabicProduct.setAbout(request.getArabicAbout());
        arabicProduct.setLanguage(arabicLanguage);
        arabicProduct.setProduct(product);

        frenchProduct.setName(request.getFrenchName());
        frenchProduct.setDescription(request.getFrenchDescription());
        frenchProduct.setAbout(request.getFrenchAbout());
        frenchProduct.setLanguage(frenchLanguage);
        frenchProduct.setProduct(product);

        product.getProductDetails().add(englishProduct);
        product.getProductDetails().add(arabicProduct);
        product.getProductDetails().add(frenchProduct);

        productRepository.save(product);

        String message = messageSourceUtils.getMessage("operation.successful.update.product");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getProduct(Integer productId) {
        // Querying
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        // Processing
        ProductDtoV2 productDto = ProductMapper.toResponseV2(product, languageRepository);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.product");
        return new ApiResponse(true, message, Map.of("product", productDto));
    }
}
