package com.toy.shopwebmvc.dto.response;

import com.toy.shopwebmvc.domain.Category;

public record CategoryResponse(
        Long id,
        String name,
        String description
) {
    public CategoryResponse(Category category) {
        this(category.getId(), category.getName(), category.getDescription());
    }
}
