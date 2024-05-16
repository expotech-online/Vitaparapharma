package org.ahmedukamel.ecommerce.service.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.ProductResponse;
import org.ahmedukamel.ecommerce.exception.CustomException;
import org.ahmedukamel.ecommerce.mapper.ProductResponseMapper;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.model.enumeration.FilterType;
import org.ahmedukamel.ecommerce.repository.CartItemRepository;
import org.ahmedukamel.ecommerce.repository.ProductRepository;
import org.ahmedukamel.ecommerce.repository.WishlistItemRepository;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.util.SecurityContextUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class TempProductService implements ITempProductService {
    final WishlistItemRepository wishlistItemRepository;
    final CartItemRepository cartItemRepository;
    final ProductRepository productRepository;
    final ProductResponseMapper mapper;

    @Override
    public ApiResponse getProductById(int productId) {
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        if (!product.getActive()) {
            throw new CustomException("Product with id %s not found.".formatted(productId));
        }

        ProductResponse response = mapper.apply(product);
        return new ApiResponse(true, "Product have been returned successfully.", Map.of("product", response));
    }

    @Override
    public ApiResponse getAllProducts(long pageSize, long pageNumber) {
        List<Product> products = productRepository.findAllByActive(true)
                .stream()
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getAllProductsPriceFilter(double value, FilterType type, long pageSize, long pageNumber) {
        Predicate<Product> filter = product -> switch (type) {
            case EQUAL -> product.finalPrice() == value;
            case GREATER_THAN -> product.finalPrice() >= value;
            case SMALLER_THAN -> product.finalPrice() <= value;
        };

        List<Product> products = productRepository.findAllByActive(true)
                .stream()
                .filter(filter)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getAllProductsRateFilter(double value, FilterType type, long pageSize, long pageNumber) {
        Predicate<Product> filter = product -> switch (type) {
            case EQUAL -> product.getRating().equals(value);
            case GREATER_THAN -> product.getRating() >= value;
            case SMALLER_THAN -> product.getRating() <= value;
        };

        List<Product> products = productRepository.findAllByActive(true)
                .stream()
                .filter(filter)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getAllOfferProducts(long pageSize, long pageNumber) {
        List<Product> products = productRepository.findAllByActive(true)
                .stream()
                .filter(Product::getDiscount)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getAllProductsFiltered(Double priceValue, Double maxPriceValue, FilterType priceFilter, Double ratingValue, FilterType ratingFilter, List<Integer> categoryIdList, long pageSize, long pageNumber) {
        List<Product> products = getFilteredProducts(priceValue, maxPriceValue, priceFilter, ratingValue, ratingFilter, categoryIdList)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> response = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", response, "count", products.size()));
    }

    private Stream<Product> getFilteredProducts(Double priceValue, Double maxPriceValue, FilterType priceFilter, Double ratingValue, FilterType ratingFilter, List<Integer> categoryIdList) {
        final Predicate<Product> pricePredicate;
        if (Objects.nonNull(priceValue) && Objects.nonNull(maxPriceValue)) {
            pricePredicate = product -> product.finalPrice() > priceValue && product.finalPrice() < maxPriceValue;
        } else if (Objects.nonNull(priceValue) && Objects.nonNull(priceFilter)) {
            pricePredicate = product -> switch (priceFilter) {
                case EQUAL -> product.finalPrice() == priceValue;
                case GREATER_THAN -> product.finalPrice() >= priceValue;
                case SMALLER_THAN -> product.finalPrice() <= priceValue;
            };
        } else {
            pricePredicate = product -> true;
        }

        final Predicate<Product> ratingPredicate;
        if (Objects.nonNull(ratingValue) && Objects.nonNull(ratingFilter)) {
            ratingPredicate = product -> switch (ratingFilter) {
                case EQUAL -> product.getRating().equals(ratingValue);
                case GREATER_THAN -> product.getRating() >= ratingValue;
                case SMALLER_THAN -> product.getRating() <= ratingValue;
            };
        } else {
            ratingPredicate = product -> true;
        }

        final Predicate<Product> categoryPredicate;
        if (Objects.nonNull(categoryIdList) && !categoryIdList.isEmpty()) {
            categoryPredicate = product -> categoryIdList.contains(product.getCategory().getCategoryId());
        } else {
            categoryPredicate = product -> true;
        }

        return productRepository.findAllByActive(true)
                .stream()
                .filter(pricePredicate)
                .filter(ratingPredicate)
                .filter(categoryPredicate)
                .sorted(Comparator.comparingInt(Product::getProductId));
    }

    @Override
    public ApiResponse getProductsByCategory(int categoryId, long pageSize, long pageNumber) {
        List<Product> products = productRepository.findAllByCategory_CategoryId(categoryId)
                .stream()
                .filter(Product::getActive)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getProductsByMainCategory(int mainCategoryId, long pageSize, long pageNumber) {
        List<Product> products = productRepository.findAllByCategory_MainCategory_CategoryId(mainCategoryId)
                .stream()
                .filter(Product::getActive)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getProductByIdForUser(int productId) {
        Product product = RepositoryUtils.getProduct(productRepository, productId);
        if (!product.getActive()) {
            throw new CustomException("Product with id %s not found.".formatted(productId));
        }

        ProductResponse response = mapper.apply(product);
        userResponse(response);
        return new ApiResponse(true, "Product have been returned successfully.", Map.of("product", response));
    }

    @Override
    public ApiResponse getAllProductsForUser(long pageSize, long pageNumber) {
        List<Product> products = productRepository.findAllByActive(true)
                .stream()
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .peek(this::userResponse)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getAllProductsFilteredForUser(Double priceValue, Double maxPriceValue, FilterType priceFilter, Double ratingValue, FilterType ratingFilter, List<Integer> categoryIdList, long pageSize, long pageNumber) {
        List<Product> products = getFilteredProducts(priceValue, maxPriceValue, priceFilter, ratingValue, ratingFilter, categoryIdList)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> response = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .peek(this::userResponse)
                .toList();
        return new ApiResponse(true, "Filtered", Map.of("products", response, "count", products.size()));
    }

    @Override
    public ApiResponse getAllProductsPriceFilterForUser(double value, FilterType type, long pageSize, long pageNumber) {
        Predicate<Product> filter = product -> switch (type) {
            case EQUAL -> product.finalPrice() == value;
            case GREATER_THAN -> product.finalPrice() >= value;
            case SMALLER_THAN -> product.finalPrice() <= value;
        };

        List<Product> products = productRepository.findAllByActive(true)
                .stream()
                .filter(filter)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .peek(this::userResponse)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getAllProductsRateFilterForUser(double value, FilterType type, long pageSize, long pageNumber) {
        Predicate<Product> filter = product -> switch (type) {
            case EQUAL -> product.getRating().equals(value);
            case GREATER_THAN -> product.getRating() >= value;
            case SMALLER_THAN -> product.getRating() <= value;
        };

        List<Product> products = productRepository.findAllByActive(true)
                .stream()
                .filter(filter)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .peek(this::userResponse)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getAllOfferProductsForUser(long pageSize, long pageNumber) {
        List<Product> products = productRepository.findAllByActive(true)
                .stream()
                .filter(Product::getDiscount)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .peek(this::userResponse)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getProductsByCategoryForUser(int categoryId, long pageSize, long pageNumber) {
        List<Product> products = productRepository.findAllByCategory_CategoryId(categoryId)
                .stream()
                .filter(Product::getActive)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .peek(this::userResponse)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));

    }

    @Override
    public ApiResponse getProductsByMainCategoryForUser(int mainCategoryId, long pageSize, long pageNumber) {
        List<Product> products = productRepository.findAllByCategory_MainCategory_CategoryId(mainCategoryId)
                .stream()
                .filter(Product::getActive)
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
        List<ProductResponse> responseList = products.stream()
                .skip(pageSize * (pageNumber - 1))
                .limit(pageSize)
                .map(mapper)
                .peek(this::userResponse)
                .toList();
        return new ApiResponse(true, "Products have been returned successfully.", Map.of("products", responseList, "count", products.size()));
    }

    @Override
    public ApiResponse getMinMaxPrice() {
        Object object = productRepository.getMinMax();
        Object[] range = (Object[]) object;
        return new ApiResponse(true, "Get minimum and maximum products price",
                Map.of("min", range[0], "max", range[1]));
    }

    public void userResponse(ProductResponse response) {
        response.setInCart(cartItemRepository.existsByCart_Customer_CustomerIdAndProduct_ProductId(SecurityContextUtils.getPrinciple().getCustomerId(), response.getProductId()));
        response.setInWishlist(wishlistItemRepository.existsByWishlist_Customer_CustomerIdAndProduct_ProductId(SecurityContextUtils.getPrinciple().getCustomerId(), response.getProductId()));
    }
}