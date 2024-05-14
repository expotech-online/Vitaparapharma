package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.response.CartItemResponse;
import org.ahmedukamel.ecommerce.dto.response.CartResponse;
import org.ahmedukamel.ecommerce.model.Cart;
import org.ahmedukamel.ecommerce.model.CartItem;
import org.ahmedukamel.ecommerce.model.Product;
import org.ahmedukamel.ecommerce.model.ProductDetail;
import org.ahmedukamel.ecommerce.util.EntityDetailsUtils;

import java.util.List;

public class CartMapper {
    public static CartResponse toResponse(Cart cart, Integer languageId) {
        CartResponse response = new CartResponse();
        response.setCartId(cart.getCartId());
        response.setCartItems(getCartItems(cart.getCartItems(), languageId));
        return response;
    }

    private static CartItemResponse toResponse(CartItem cartItem, Integer languageId) {
        ProductDetail productDetail = EntityDetailsUtils.supplyProductDetail(cartItem.getProduct(), languageId);
        Product product = cartItem.getProduct();
        CartItemResponse response = new CartItemResponse();
        response.setProductId(product.getProductId());
        response.setProductName(productDetail.getName());
        response.setProductPrice(product.getDiscount() ? product.getAfterDiscount() : product.getPrice());
        response.setQuantity(cartItem.getQuantity());
        response.setRating(product.getRating());
        response.setPictureUrl(product.getPictures().isEmpty() ? "" : product.getPictures().get(0));
        response.setStockQuantity(product.getStockQuantity());
        return response;
    }

    private static List<CartItemResponse> getCartItems(List<CartItem> items, Integer languageId) {
        return items.stream().map(item -> CartMapper.toResponse(item, languageId)).toList();
    }
}
