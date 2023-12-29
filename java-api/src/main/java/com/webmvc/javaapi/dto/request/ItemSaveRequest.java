package com.webmvc.javaapi.dto.request;

import com.webmvc.javaapi.constant.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemSaveRequest(
        @NotNull
        String name,
        @NotNull
        String description,
        @Min(100)
        int price,
        @Max(value = 9999)
        int quantity,
        Category category
) {
}
