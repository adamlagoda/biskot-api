package com.biskot.infra.gateway;

import com.biskot.domain.model.Product;
import com.biskot.domain.spi.ProductPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static com.biskot.infra.gateway.ProductGatewayTest.ProductGatewayTestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ProductGateway.class, ObjectMapper.class})
class ProductGatewayTest {

    @Autowired
    private ProductPort productGateway;

    @Test
    public void shouldGetProduct() {
        //when
        Optional<Product> product = productGateway.getProduct(PRODUCT_ID);

        //then
        assertThat(product)
                .isNotEmpty()
                .get()
                .isEqualTo(PRODUCT);
    }

    @Test
    public void shouldGetEmptyOptional() {
        //when
        Optional<Product> product = productGateway.getProduct(NON_EXISTENT_PRODUCT_ID);

        //then
        assertThat(product)
                .isEmpty();
    }

    static class ProductGatewayTestData {
        private static final String LABEL = "Déodorant Spray 200ml Ice Dive ADIDAS";
        private static final BigDecimal PRICE = BigDecimal.valueOf(2.0);
        static final long NON_EXISTENT_PRODUCT_ID = 10l;
        static final long PRODUCT_ID = 1l;
        static final Product PRODUCT = Product.of(
                PRODUCT_ID,
                LABEL,
                PRICE);
    }
}