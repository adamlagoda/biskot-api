package com.biskot.domain.businessrules;

import com.biskot.domain.exception.business.MaximumCartValueExceeded;
import com.biskot.domain.exception.business.MaximumNumberOfProductsExceeded;
import com.biskot.domain.exception.business.StockAvailabilityExceeded;
import com.biskot.domain.model.Cart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class CartBusinessRulesValidationService {

    private static final int MAXIMUM_NUMBER_OF_PRODUCTS = 3;
    private static final BigDecimal MAXIMUM_CART_VALUE = BigDecimal.valueOf(100);

    public void validate(Cart cart) {
        EnumSet.allOf(CartBusinessRuleViolation.class)
                .stream()
                .map(ruleViolation -> ruleViolation.validate(cart))
                .filter(Objects::nonNull)
                .findAny()
                .ifPresent(ruleViolation -> {
                    switch (ruleViolation) {
                        case CART_CONTAINS_TOO_MAN_PRODUCTS:
                            throw new MaximumNumberOfProductsExceeded();
                        case TOTAL_PRICE_EXCEEDS_MAXIMUM:
                            throw new MaximumCartValueExceeded();
                        case QUANTITY_EXCEEDS_STOCK_SIZE:
                            throw new StockAvailabilityExceeded();
                    }
                });
    }

    @AllArgsConstructor
    enum CartBusinessRuleViolation {

        CART_CONTAINS_TOO_MAN_PRODUCTS(cart -> cart.getItems().size() > MAXIMUM_NUMBER_OF_PRODUCTS),
        TOTAL_PRICE_EXCEEDS_MAXIMUM(cart -> MAXIMUM_CART_VALUE.compareTo(cart.getTotalPrice()) < 0),
        QUANTITY_EXCEEDS_STOCK_SIZE(cart -> cart.getItems().stream()
                .anyMatch(item -> item.getQuantity() > item.getProduct().getQuantityInStock())
        );

        private Predicate<Cart> validator;

        public CartBusinessRuleViolation validate(Cart cart) {
            if (validator.test(cart)) {
                return this;
            }
            return null;
        }
    }
}
