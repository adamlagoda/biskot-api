package com.biskot.app.rest;

import com.biskot.app.rest.request.UpdateCartRequest;
import com.biskot.app.rest.response.CartApiResponse;
import com.biskot.app.rest.response.CreateCartApiResponse;
import com.biskot.domain.model.Cart;
import com.biskot.domain.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/carts/{cartId}")
    public CartApiResponse retrieveCart(@PathVariable("cartId") long cartId) {
        Cart cart = cartService.getCart(cartId);
        return CartApiResponse.fromDomain(cart);
    }

    @PostMapping("/carts")
    public CreateCartApiResponse createCart() {
        long id = cartService.createCart();
        return CreateCartApiResponse.from(id);
    }

    @PutMapping("/carts/{cartId}/items")
    public void updateCart(@PathVariable("cartId") Long cartId, @RequestBody UpdateCartRequest request) {
        cartService.addItemToCart(cartId, request.getProductId(), request.getQuantity());
    }
}
