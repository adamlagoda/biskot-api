package com.biskot.domain.model;

import com.biskot.infra.repository.entity.CartEntity;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class Cart {

    @Getter
    private final long id;
    private Map<Long, Item> items;

    public void addItem(Product productToAdd, int quantity) {
        if (isEmpty(items)) {
            items = new HashMap<>();
        }
        long productId = productToAdd.getId();
        items.compute(productId, (k, v) -> {
            if (v != null) {
                v.setQuantity(v.getQuantity() + quantity);
            } else {
                v = Item.of(productToAdd);
            }
            return v;
        });
    }

    /**
     * Return an unmodifiable list of items in the cart
     *
     * @return copy of list containing items
     */
    public List<Item> getItems() {
        return List.of(items.values().toArray(new Item[]{}));
    }

    public BigDecimal getTotalPrice() {
        return items.values().stream()
                .reduce(BigDecimal.ZERO,
                        (accumulator, item) -> accumulator.add(item.getLinePrice()),
                        BigDecimal::add);
    }

    public static Cart fromEntity(CartEntity cartEntity) {
        Cart cart = new Cart(cartEntity.getId());
        cart.items = cartEntity.getItems().stream()
                .map(Item::fromEntity)
                .collect(toMap(item -> item.getProduct().getId(), Function.identity()));
        return cart;
    }
}
