package org.ahmedukamel.ecommerce.service.tag;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.response.BlogPostResponse;
import org.ahmedukamel.ecommerce.dto.response.ProductResponse;
import org.ahmedukamel.ecommerce.mapper.BlogPostResponseMapper;
import org.ahmedukamel.ecommerce.mapper.ProductResponseMapper;
import org.ahmedukamel.ecommerce.model.BlogPost;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.model.Tag;
import org.ahmedukamel.ecommerce.repository.TagRepository;
import org.ahmedukamel.ecommerce.util.LocaleContextUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {
    final ProductResponseMapper productMapper;
    final BlogPostResponseMapper blogPostMapper;
    final TagRepository tagRepository;

    @Override
    public ApiResponse getAllTags() {
        List<String> tagList = tagRepository.findAll()
                .stream()
                .map(Tag::getName)
                .toList();

        return new ApiResponse(true, "tags", tagList);
    }

    @Override
    public ApiResponse getProducts(List<String> tagNames, int number) {
        List<Tag> tags = tagNames
                .stream()
                .map(tagRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        Map<Product, Integer> productPeriorityMap = new HashMap<>();
        tags.forEach(tag -> tag.getProducts().forEach(product -> productPeriorityMap.merge(product, 1, Integer::sum)));

        List<Product> productList = productPeriorityMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(number)
                .map(Map.Entry::getKey)
                .toList();

        List<ProductResponse> responseList = productList
                .stream()
                .map(productMapper)
                .toList();

        return new ApiResponse(true, "The most recommended products.", Map.of("products", responseList));
    }

    @Override
    public ApiResponse getPosts(List<String> tagNames, int number) {
        List<Tag> tags = tagNames
                .stream()
                .map(tagRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        Map<BlogPost, Integer> hashMap = new HashMap<>();
        tags.forEach(tag -> tag.getPosts().forEach(post -> hashMap.merge(post, 1, Integer::sum)));

        List<BlogPost> postList = hashMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(number)
                .map(Map.Entry::getKey)
                .toList();

        List<BlogPostResponse> responseList = postList
                .stream()
                .map(post -> blogPostMapper.apply(post, LocaleContextUtils.getLanguage()))
                .toList();

        return new ApiResponse(true, "The most recommended products.", Map.of("products", responseList));
    }
}