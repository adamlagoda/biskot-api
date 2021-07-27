package com.biskot.app.rest;

import com.biskot.domain.exception.generic.CartNotFound;
import com.biskot.domain.exception.business.MaximumCartValueExceeded;
import com.biskot.domain.model.Cart;
import com.biskot.domain.model.Product;
import com.biskot.domain.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.biskot.app.rest.CartControllerTest.CartControllerTestData.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    public void shouldRetrieveCart() throws Exception {
        //given
        long cartId = 1L;
        given(cartService.getCart(cartId))
                .willReturn(CART);

        //then
        mockMvc.perform(get("/carts/" + cartId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{\"id\": 1,\"items\": [" +
                                "{" +
                                "\"product_id\": 10," +
                                "\"product_label\": \"test product\"," +
                                "\"quantity\": 1," +
                                "\"unit_price\": 1.0," +
                                "\"line_price\": 1.0" +
                                "}" +
                                "]," +
                                "\"totalPrice\": 1.0" +
                                "}"
                ));
    }

    @Test
    public void shouldAddCart() throws Exception {
        //given
        given(cartService.createCart()).willReturn(1L);

        //then
        mockMvc.perform(post("/carts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\": 1}"));
    }

    @Test
    public void shouldUpdateCart() throws Exception {
        //given
        willDoNothing().given(cartService).addItemToCart(CART_ID, PRODUCT_ID, 1);

        //when
        mockMvc.perform(put("/carts/" + CART_ID + "/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"product_id\": 10, \"quantity\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn400ErrorCode() throws Exception {
        //given
        willThrow(new MaximumCartValueExceeded()).given(cartService).addItemToCart(CART_ID, PRODUCT_ID, 1);

        ///when
        mockMvc.perform(put("/carts/" + CART_ID + "/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"product_id\": 10, \"quantity\": 1}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Business rules have not been respected"));
    }

    @Test
    public void shouldReturn404ErrorCode() throws Exception {
        //given
        willThrow(new CartNotFound()).given(cartService).addItemToCart(CART_ID, PRODUCT_ID, 1);

        ///when
        mockMvc.perform(put("/carts/" + CART_ID + "/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"product_id\": 10, \"quantity\": 1}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cart or product not found"));
    }

    static class CartControllerTestData {
        static final long CART_ID = 1L;
        static final long PRODUCT_ID = 10L;
        static final String LABEL = "test product";
        static final BigDecimal PRICE = BigDecimal.ONE;
        static final Product PRODUCT = Product.of(PRODUCT_ID, LABEL, PRICE);
        static final Cart CART = createCart();

        private static Cart createCart() {
            Cart cart = Cart.of(CART_ID);
            cart.addItem(PRODUCT, 1);
            return cart;
        }

    }
}