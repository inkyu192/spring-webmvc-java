package com.toy.shopwebmvc.dto.request;

import com.toy.shopwebmvc.constant.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ItemSaveRequest(
        @NotEmpty
        String name,
        @NotEmpty
        String description,
        @NotNull
        @Min(100)
        int price,
        @NotNull
        @Max(value = 9999)
        int quantity,
        Category category
) {
}
