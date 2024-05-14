package org.ahmedukamel.ecommerce.service.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.ProductDtoV4;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.mapper.ProductDtoV4Mapper;
import org.ahmedukamel.ecommerce.model.*;
import org.ahmedukamel.ecommerce.repository.CategoryRepository;
import org.ahmedukamel.ecommerce.repository.LanguageRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.repository.TagRepository;
import org.ahmedukamel.ecommerce.util.ImageDirectoryUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

import static org.ahmedukamel.ecommerce.util.EntityDetailsUtils.supplyProductDetail;
import static org.ahmedukamel.ecommerce.util.ImageDirectoryUtils.deleteImage;
import static org.ahmedukamel.ecommerce.util.ImageDirectoryUtils.saveImage;
import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getCategory;
import static org.ahmedukamel.ecommerce.util.RepositoryUtils.getLanguage;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductManagementService implements IProductManagementService {
    final CategoryRepository categoryRepository;
    final LanguageRepository languageRepository;
    final ProductRepository productRepository;
    final TagRepository tagRepository;
    final ProductDtoV4Mapper mapper;

    @Override
    public ApiResponse addProduct(Object object, MultipartFile[] images) {
        if (images[0].isEmpty()) throw new CustomException("Minimum allowed files is 1.");
        if (images.length > 5) throw new CustomException("Maximum allowed files is 5.");

        ProductDtoV4 request = (ProductDtoV4) object;
        Product product = new Product();

        updateProduct(product, request);
        updateTags(product, request);
        uploadImages(product, images);

        return new ApiResponse(true, "Product have been added successfully.");
    }

    @Override
    public ApiResponse updateProduct(int productId, Object object) {
        ProductDtoV4 request = (ProductDtoV4) object;
        Product product = RepositoryUtils.getProduct(productRepository, productId);

        updateProduct(product, request);
        updateTags(product, request);

        return new ApiResponse(true, "Product have been updated successfully.");
    }

    @Override
    public ApiResponse getProduct(int productId) {
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        ProductDtoV4 response = mapper.apply(product);

        return new ApiResponse(true, "Product have been returned successfully.", Map.of("product", response));
    }

    @Override
    public ApiResponse getProducts(long pageSize, long pageNumber) {
        List<ProductDtoV4> responseList = productRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Product::getProductId))
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .toList();

        return new ApiResponse(true, "Product list have been returned successfully.", Map.of("products", responseList));
    }

    @Override
    public ApiResponse getProductsByActiveStatus(boolean active, long pageSize, long pageNumber) {
        List<ProductDtoV4> responseList = productRepository.findAllByActive(active)
                .stream()
                .sorted(Comparator.comparingLong(Product::getProductId))
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .toList();

        return new ApiResponse(true, "Product list have been returned successfully.", Map.of("products", responseList));
    }

    @Override
    public ApiResponse setProductActiveStatus(int productId, boolean active) {
        Product product = RepositoryUtils.getProduct(productRepository, productId);

        if (product.getActive().equals(active)) {
            throw new CustomException("Product active status is already %s.".formatted(active));
        }

        product.setActive(active);
        productRepository.save(product);

        return new ApiResponse(true, "Product active status have been set to %s successfully.".formatted(active));
    }

    @Override
    public ApiResponse uploadImages(int productId, MultipartFile[] images) {
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        int remain = 5 - product.getPictures().size();

        if (remain == 0) throw new CustomException("Product Already has 5 pictures.");
        if (images[0].isEmpty()) throw new CustomException("Minimum allowed files is 1.");
        if (images.length > remain)
            throw new CustomException("Maximum allowed files for this product is %d.".formatted(remain));

        uploadImages(product, images);

        return new ApiResponse(true, "Product images have been uploaded successfully.");
    }

    @Override
    public ApiResponse removeImage(int productId, String imageName) {
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        String imageUrl = product.getPictures()
                .stream()
                .filter(picture -> picture.contains(imageName))
                .findFirst()
                .orElseThrow(() -> new CustomException("Image name %s not found.".formatted(imageName)));

        deleteImage(imageUrl);
        product.getPictures().remove(imageUrl);

        return new ApiResponse(true, "Product image deleted successfully.");
    }

    private void updateProduct(Product product, ProductDtoV4 request) {
        Category category = getCategory(categoryRepository, request.getCategoryId());

        Language enL = getLanguage(languageRepository, "en"),
                arL = getLanguage(languageRepository, "ar"),
                frL = getLanguage(languageRepository, "fr");

        ProductDetail en = supplyProductDetail(product, enL.getLanguageId()),
                ar = supplyProductDetail(product, arL.getLanguageId()),
                fr = supplyProductDetail(product, frL.getLanguageId());

        product.setPrice(request.getPriceBeforeDiscount());
        product.setStockQuantity(request.getStockQuantity());
        product.setDiscount(request.isDiscount());
        product.setAfterDiscount(request.getPriceAfterDiscount());
        product.setCategory(category);
        product.setWeight(request.getWeight());

        en.setName(request.getEnglishName());
        en.setDescription(request.getEnglishDescription());
        en.setAbout(request.getEnglishAbout());
        en.setLanguage(enL);
        en.setProduct(product);

        ar.setName(request.getArabicName());
        ar.setDescription(request.getArabicDescription());
        ar.setAbout(request.getArabicAbout());
        ar.setLanguage(arL);
        ar.setProduct(product);

        fr.setName(request.getFrenchName());
        fr.setDescription(request.getFrenchDescription());
        fr.setAbout(request.getFrenchAbout());
        fr.setLanguage(frL);
        fr.setProduct(product);

        product.getProductDetails().add(en);
        product.getProductDetails().add(ar);
        product.getProductDetails().add(fr);

        productRepository.save(product);
    }

    private void updateTags(Product product, ProductDtoV4 request) {
        Language en = RepositoryUtils.getLanguage(languageRepository, "en"),
                ar = RepositoryUtils.getLanguage(languageRepository, "ar"),
                fr = RepositoryUtils.getLanguage(languageRepository, "fr");

        BiFunction<Collection<String>, Language, Collection<Tag>> getTags = (tags, language) -> tags
                .stream()
                .map(String::toLowerCase)
                .map(String::strip)
                .map(name -> {
                    Tag tag = tagRepository.findById(name).orElseGet(Tag::new);
                    tag.setName(name);
                    tag.setLanguage(language);
                    tag.getProducts().add(product);
                    return tagRepository.save(tag);
                })
                .toList();

        Collection<Tag> englishTags = getTags.apply(request.getEnglishTags(), en);
        Collection<Tag> arabicTags = getTags.apply(request.getArabicTags(), ar);
        Collection<Tag> frenchTags = getTags.apply(request.getFrenchTags(), fr);

        product.getTags().clear();
        product.getTags().addAll(englishTags);
        product.getTags().addAll(arabicTags);
        product.getTags().addAll(frenchTags);
        productRepository.save(product);
    }

    private void uploadImages(Product product, MultipartFile[] images) {
        List<String> pictures = Arrays.stream(images)
                .map(file -> {
                    try {
                        return saveImage(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(ImageDirectoryUtils::getImageUrl)
                .toList();

        product.getPictures().addAll(pictures);
        productRepository.save(product);
    }
}