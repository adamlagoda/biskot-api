package com.biskot.infra.repository;

import com.biskot.domain.model.Cart;
import com.biskot.domain.spi.CartPersistencePort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InMemoryCartRepository implements CartPersistencePort {

    @Override
    public Optional<Cart> getCart(long id) {
        // TODO: to be implemented
        return null;
    }

    @Override
    public void saveCart(Cart cart) {
        // TODO: to be implemented
    }
}
