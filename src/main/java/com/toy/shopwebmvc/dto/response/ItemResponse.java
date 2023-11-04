package com.toy.shopwebmvc.dto.response;

import com.toy.shopwebmvc.domain.Item;

public record ItemResponse(
        Long id,
        String name,
        String description,
        int price,
        int quantity
) {
    public static ItemResponse create(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getQuantity()
        );
    }
}
