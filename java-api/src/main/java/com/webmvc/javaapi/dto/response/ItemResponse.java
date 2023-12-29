package com.webmvc.javaapi.dto.response;


import com.webmvc.javaapi.domain.Item;

public record ItemResponse(
        Long id,
        String name,
        String description,
        int price,
        int quantity
) {
    public ItemResponse(Item item) {
        this(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getQuantity()
        );
    }
}
