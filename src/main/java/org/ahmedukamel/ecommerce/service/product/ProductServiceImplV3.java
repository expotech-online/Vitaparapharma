package org.ahmedukamel.ecommerce.service.product;

import org.ahmedukamel.ecommerce.dto.ProductDtoV3;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.ProductMapper;
import org.ahmedukamel.ecommerce.model.Language;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.repository.CategoryRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.MainCategoryRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.util.LocaleContextUtils;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImplV3 extends ProductServiceImpl {
    public ProductServiceImplV3(MainCategoryRepository mainCategoryRepository, CategoryRepository categoryRepository, LanguageRepository languageRepository, MessageSourceUtils messageSourceUtils, ProductRepository productRepository) {
        super(mainCategoryRepository, categoryRepository, languageRepository, messageSourceUtils, productRepository);
    }

    @Override
    ProductDtoV3 getProduct(Product product, Language language) {
        try {
            return ProductMapper.toResponseV3(product, language.getLanguageId());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    ApiResponse getProducts(Collection<Product> products, Boolean active) {
        // Querying
        Language language = RepositoryUtils.getLanguage(languageRepository, LocaleContextUtils.getLanguage());
        // Processing
        List<ProductDtoV3> productDtoList = ProductMapper.toResponseV3(products, language.getLanguageId(), active);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.products");
        return new ApiResponse(true, message, Map.of("products", productDtoList));
    }
}
