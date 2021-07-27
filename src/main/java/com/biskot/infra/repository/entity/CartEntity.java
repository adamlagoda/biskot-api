package com.biskot.infra.repository.entity;

import com.biskot.domain.model.Cart;
import lombok.Value;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class CartEntity {

    long id;
    List<ItemEntity> items;

    public static CartEntity fromDomain(Cart cart) {
        List<ItemEntity> items = cart.getItems().stream()
                .map(ItemEntity::fromDomain)
                .collect(toList());
        return new CartEntity(cart.getId(), items);
    }
}
