package com.biskot.domain.businessrules;

import com.biskot.domain.exception.business.MaximumCartValueExceeded;
import com.biskot.domain.exception.business.MaximumNumberOfProductsExceeded;
import com.biskot.domain.exception.business.StockAvailabilityExceeded;
import com.biskot.domain.model.Cart;
import com.biskot.domain.model.Item;
import com.biskot.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CartBusinessRulesValidationServiceTest {

    private CartBusinessRulesValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new CartBusinessRulesValidationService();
    }

    @Test
    public void shouldThrowMaximumCartValueExceeded() {
        //given
        Map<Long, Item> items = Map.of(10l, Item.of(Product.of(10l, "product 1", BigDecimal.valueOf(101))));
        Cart tooExpensiveCart = Cart.of(1l, items);

        //then
        assertThatThrownBy(() -> validationService.validate(tooExpensiveCart))
                .isExactlyInstanceOf(MaximumCartValueExceeded.class);
    }

    @Test
    public void shouldThrowMaximumNumberOfProductsExceeded() {
        //given
        Map<Long, Item> items = Map.of(10l, Item.of(Product.of(10l, "product 1", BigDecimal.valueOf(1))),
                20l, Item.of(Product.of(20l, "product 2", BigDecimal.valueOf(2))),
                30l, Item.of(Product.of(30l, "product 3", BigDecimal.valueOf(3))),
                40l, Item.of(Product.of(40l, "product 4", BigDecimal.valueOf(4)))
        );
        Cart cartWithTooManyProducts = Cart.of(1l, items);

        //then
        assertThatThrownBy(() -> validationService.validate(cartWithTooManyProducts))
                .isExactlyInstanceOf(MaximumNumberOfProductsExceeded.class);
    }

    @Test
    public void shouldThrowStockAvailabilityExceeded() {
        //given
        Product product = Product.of(10l, "product 1", BigDecimal.valueOf(1), 50);
        Cart cartWithToManyItems = Cart.of(1l);
        cartWithToManyItems.addItem(product, 51);

        //then
        assertThatThrownBy(() -> validationService.validate(cartWithToManyItems))
                .isExactlyInstanceOf(StockAvailabilityExceeded.class);
    }
}
