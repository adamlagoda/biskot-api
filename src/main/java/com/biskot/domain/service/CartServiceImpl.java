package com.biskot.domain.service;

import com.biskot.domain.exception.generic.CartNotFound;
import com.biskot.domain.exception.generic.ProductNotFound;
import com.biskot.domain.factory.CartFactory;
import com.biskot.domain.model.Cart;
import com.biskot.domain.model.Product;
import com.biskot.domain.spi.CartPersistencePort;
import com.biskot.domain.spi.ProductPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartPersistencePort cartRepository;

    @Autowired
    private ProductPort productGateway;

    @Autowired
    private CartFactory cartFactory;

    @Override
    public long createCart() {
        Cart cart = cartFactory.create();
        cartRepository.saveCart(cart);
        return cart.getId();
    }

    @Override
    public Cart getCart(long cartId) {
        return cartRepository.getCart(cartId).orElseThrow(CartNotFound::new);
    }

    @Override
    public void addItemToCart(long cartId, long productId, int quantityToAdd) {
        Product product = productGateway.getProduct(productId).orElseThrow(ProductNotFound::new);
        synchronized (this) {
            Cart cart = getCart(cartId);
            cart.addItem(product, quantityToAdd);
            cartRepository.saveCart(cart);
        }
    }

    /*
    Added for test purposes only
     */
    public void addItemToCartNotSynchronized(long cartId, long productId, int quantityToAdd) {
        Product product = productGateway.getProduct(productId).orElseThrow(ProductNotFound::new);
        Cart cart = getCart(cartId);
        cart.addItem(product, quantityToAdd);
        cartRepository.saveCart(cart);
    }
}
