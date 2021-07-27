package com.biskot.infra.repository;

import com.biskot.domain.model.Cart;
import com.biskot.domain.spi.CartPersistencePort;
import com.biskot.infra.repository.entity.CartEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemoryCartRepository implements CartPersistencePort {

    //TODO replace global collection with H2 database
    private final Map<Long, CartEntity> carts;

    @Override
    public Optional<Cart> getCart(long id) {
        return Optional.ofNullable(carts.get(id))
                .map(Cart::fromEntity);
    }

    @Override
    public void saveCart(Cart cart) {
        carts.put(cart.getId(), CartEntity.fromDomain(cart));
    }
}
