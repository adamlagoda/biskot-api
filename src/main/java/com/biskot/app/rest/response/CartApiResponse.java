package com.biskot.app.rest.response;


import com.biskot.domain.model.Cart;
import com.biskot.domain.model.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class CartApiResponse {

    @JsonProperty
    long id;

    @JsonProperty
    List<ItemsApiResponse> items;

    @JsonProperty
    double totalPrice;

    public static CartApiResponse fromDomain(Cart cart) {
        return CartApiResponse.builder()
                .id(cart.getId())
                .totalPrice(cart.getTotalPrice().doubleValue())
                .items(cart.getItems().stream()
                        .map(ItemsApiResponse::fromDomain)
                        .collect(toList()))
                .build();
    }

    @Value
    @Builder
    static class ItemsApiResponse {

        @JsonProperty("product_id")
        long productId;

        @JsonProperty("product_label")
        String productLabel;

        @JsonProperty
        int quantity;

        @JsonProperty("unit_price")
        double unitPrice;

        @JsonProperty("line_price")
        double linePrice;

        static ItemsApiResponse fromDomain(Item item) {
            return ItemsApiResponse.builder()
                    .productId(item.getProduct().getId())
                    .productLabel(item.getProduct().getLabel())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getProduct().getPrice().doubleValue())
                    .linePrice(item.getLinePrice().doubleValue())
                    .build();
        }
    }
}