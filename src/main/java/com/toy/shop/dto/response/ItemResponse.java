package com.toy.shop.dto.response;

import com.toy.shop.domain.Item;

public record ItemResponse(
        Long id,
        String name,
        String description,
        int price,
        int quantity
) {
    public ItemResponse(Item item) {
        this(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getQuantity());
    }
}
