package com.biskot.domain.service;

import com.biskot.domain.exception.generic.CartNotFound;
import com.biskot.domain.exception.generic.ProductNotFound;
import com.biskot.domain.factory.CartFactory;
import com.biskot.domain.model.Cart;
import com.biskot.domain.model.Product;
import com.biskot.domain.spi.CartPersistencePort;
import com.biskot.domain.spi.ProductPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.biskot.domain.service.CartServiceImplTest.CartServiceTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CartServiceImpl.class)
class CartServiceImplTest {

    @MockBean
    private CartPersistencePort cartRepository;

    @MockBean
    private ProductPort productGateway;

    @MockBean
    private CartFactory cartFactory;

    @Autowired
    private CartService service;

    @Test
    public void shouldRetrieveCart() {
        //given
        given(cartRepository.getCart(CART_ID)).willReturn(Optional.of(CART));

        //when
        Cart cart = service.getCart(CART_ID);

        //then
        assertThat(cart).isEqualTo(CART);
    }

    @Test
    public void shouldThrowCartNotFound() {
        //given
        given(cartRepository.getCart(CART_ID)).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> service.getCart(CART_ID))
                .isExactlyInstanceOf(CartNotFound.class);
    }

    @Test
    public void shouldCreateCart() {
        //given
        given(cartFactory.create()).willReturn(CART);
        willDoNothing().given(cartRepository).saveCart(CART);

        //when
        long cartId = service.createCart();

        //then
        assertThat(cartId).isEqualTo(CART_ID);
    }

    @Test
    public void shouldAddItemToCart() {
        //given
        Cart mockedCart = new Cart(CART_ID);
        given(productGateway.getProduct(PRODUCT_ID)).willReturn(Optional.of(PRODUCT));
        given(cartRepository.getCart(CART_ID)).willReturn(Optional.of(mockedCart));
        willDoNothing().given(cartRepository).saveCart(CART);

        //when
        service.addItemToCart(CART_ID, PRODUCT_ID, 1);

        //then
        assertThat(mockedCart.getItems())
                .hasSize(1)
                .element(0)
                .matches(item -> item.getQuantity() == 1
                        && item.getProduct().equals(PRODUCT));
    }

    @Test
    public void shouldThrowProductNotFoundException() {
        //given
        given(productGateway.getProduct(PRODUCT_ID)).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> service.addItemToCart(CART_ID, PRODUCT_ID, 1))
                .isExactlyInstanceOf(ProductNotFound.class);
    }


    static class CartServiceTestData {
        static final long CART_ID = 1L;
        static final long PRODUCT_ID = 10L;
        static final String LABEL = "test product";
        static final BigDecimal PRICE = BigDecimal.ONE;
        static final Product PRODUCT = new Product(PRODUCT_ID, LABEL, PRICE);
        static final Cart CART = createCart();

        private static Cart createCart() {
            Cart cart = new Cart(CART_ID);
            cart.addItem(PRODUCT, 1);
            return cart;
        }

    }
}