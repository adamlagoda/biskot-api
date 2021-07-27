package com.biskot.domain.service;

import com.biskot.domain.factory.CartFactory;
import com.biskot.domain.model.Cart;
import com.biskot.domain.spi.CartPersistencePort;
import com.biskot.domain.spi.ProductPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.biskot.domain.service.CartServiceImplTest.CartServiceTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CartServiceImpl.class)
class CartServiceSynchronizationTest {

    @MockBean
    private CartPersistencePort cartRepository;

    @MockBean
    private ProductPort productGateway;

    @SpyBean
    private CartFactory cartFactory;

    @Autowired
    private CartService service;

    private static final int NUM_THREADS = 10;
    private static final int NUM_ITERATIONS = 1000;

    @Test
    public void shouldSynchronizeAccessToCart() throws InterruptedException {
        //given
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        Cart mockedCart = new Cart(CART_ID);
        given(productGateway.getProduct(PRODUCT_ID)).willReturn(Optional.of(PRODUCT));
        given(cartRepository.getCart(CART_ID)).willReturn(Optional.of(mockedCart));
        willDoNothing().given(cartRepository).saveCart(CART);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < NUM_ITERATIONS; j++) {
                    service.addItemToCart(CART_ID, PRODUCT_ID, 1);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        assertThat(mockedCart.getItems())
                .hasSize(1)
                .element(0)
                .matches(item -> item.getQuantity() == NUM_ITERATIONS * NUM_THREADS
                        && item.getProduct().equals(PRODUCT));
    }

    @Test
    public void shouldNotSynchronizeAccessToCart() throws InterruptedException {
        //given
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        Cart mockedCart = new Cart(CART_ID);
        given(productGateway.getProduct(PRODUCT_ID)).willReturn(Optional.of(PRODUCT));
        given(cartRepository.getCart(CART_ID)).willReturn(Optional.of(mockedCart));
        willDoNothing().given(cartRepository).saveCart(CART);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < NUM_ITERATIONS; j++) {
                    ((CartServiceImpl) service).addItemToCartNotSynchronized(CART_ID, PRODUCT_ID, 1);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        assertThat(mockedCart.getItems())
                .hasSize(1)
                .element(0)
                .matches(item -> item.getQuantity() != NUM_ITERATIONS * NUM_THREADS
                        && item.getProduct().equals(PRODUCT));
    }
}
