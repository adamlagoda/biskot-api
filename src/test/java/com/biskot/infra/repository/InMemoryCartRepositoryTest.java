package com.biskot.infra.repository;

import com.biskot.domain.model.Cart;
import com.biskot.domain.model.Item;
import com.biskot.domain.model.Product;
import com.biskot.domain.spi.CartPersistencePort;
import com.biskot.infra.repository.entity.CartEntity;
import com.biskot.infra.repository.entity.ItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.biskot.infra.repository.InMemoryCartRepositoryTest.InMemoryCartRepositoryTestData.*;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryCartRepositoryTest {

    private CartPersistencePort repository;

    @BeforeEach
    void setUp() {
        Map<Long, CartEntity> carts = new HashMap<>();
        carts.put(CART_ID, CART_ENTITY);
        repository = new InMemoryCartRepository(carts);
    }

    @Test
    public void shouldGetCart() {
        //when
        Optional<Cart> cart = repository.getCart(CART_ID);

        //then
        assertThat(cart)
                .isNotEmpty()
                .get()
                .isEqualTo(CART);
    }

    @Test
    public void shouldGetEmptyOptional() {
        //when
        Optional<Cart> cart = repository.getCart(10l);

        //then
        assertThat(cart).isEmpty();
    }

    @Test
    public void shouldSaveCart() {
        //given
        long cartId = 2l;
        long productId = 20l;
        Product product = Product.of(productId, "test product 2", BigDecimal.valueOf(2.0));
        Map<Long, Item> items = Map.of(productId, Item.of(product));
        Cart cartToSave = Cart.of(cartId, items);

        //when
        repository.saveCart(cartToSave);

        //then
        Optional<Cart> result = repository.getCart(cartId);
        assertThat(result)
                .isNotEmpty()
                .get()
                .isEqualTo(cartToSave);
    }

    @Test
    public void shouldUpdateExistingCart() {
        //given
        Cart beforeUpdate = repository.getCart(CART_ID).get();
        long productId = 20l;
        Product product = Product.of(productId, "test product 2", BigDecimal.valueOf(2.0));
        Map<Long, Item> items = Map.of(productId, Item.of(product));
        Cart cartToSave = Cart.of(CART_ID, items);

        //when
        repository.saveCart(cartToSave);

        //then
        Optional<Cart> result = repository.getCart(CART_ID);
        assertThat(result)
                .isNotEmpty()
                .get()
                .isNotEqualTo(beforeUpdate)
                .isEqualTo(cartToSave);
    }

    static class InMemoryCartRepositoryTestData {
        private static final long PRODUCT_ID = 10l;
        private static final String LABEL = "test product";
        private static final BigDecimal UNIT_PRICE = BigDecimal.valueOf(3.0);
        private static final int QUANTITY = 1;
        private static final Product PRODUCT = Product.of(PRODUCT_ID, LABEL, UNIT_PRICE);
        private static final Item ITEM = Item.of(PRODUCT);
        private static final ItemEntity ITEM_ENTITY = new ItemEntity(PRODUCT_ID, LABEL, UNIT_PRICE, QUANTITY);

        static final long CART_ID = 1l;
        static final CartEntity CART_ENTITY = new CartEntity(CART_ID, singletonList(ITEM_ENTITY));
        static final Cart CART = Cart.of(CART_ID, singletonMap(PRODUCT_ID, ITEM));
    }
}
