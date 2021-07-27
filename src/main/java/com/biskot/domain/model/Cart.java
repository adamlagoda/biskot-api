package com.biskot.domain.model;

import com.biskot.infra.repository.entity.CartEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class Cart {

    @Getter
    private final long id;
    private final Map<Long, Item> items;

    public void addItem(Product productToAdd, int quantity) {
        long productId = productToAdd.getId();
        var item = items.get(productId);
        if (item != null) {
            int updatedQuantity = item.getQuantity() + quantity;
            item.setQuantity(updatedQuantity);
        } else {
            item = Item.of(productToAdd);
            item.setQuantity(quantity);
            items.put(productId, item);
        }
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
        Map<Long, Item> items = cartEntity.getItems().stream()
                .map(Item::fromEntity)
                .collect(toMap(item -> item.getProduct().getId(), Function.identity()));
        return Cart.of(cartEntity.getId(), items);
    }

    public static Cart of(long cartId) {
        return of(cartId, new HashMap<>());
    }
}
