package spring.web.java.domain.item.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import spring.web.java.domain.item.Item;

public record ItemSaveRequest(
        @NotNull
        String name,
        @NotNull
        String description,
        @Min(100)
        int price,
        @Max(value = 9999)
        int quantity,
        Item.Category category
) {
}
