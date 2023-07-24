package com.toy.shop.business.item.dto;

import com.toy.shop.domain.Item;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ItemDto {

    @Getter
    public static class Save {

        @NotEmpty
        private String name;

        @NotEmpty
        private String description;

        @NotNull
        @Min(100)
        private int price;

        @NotNull
        @Max(value = 9999)
        private int quantity;

        @NotNull
        private Long categoryId;
    }

    @Getter
    public static class Update {

        private String name;

        private String description;

        @Min(100)
        private Integer price;

        @Max(value = 9999)
        private Integer quantity;

        private Long categoryId;
    }

    @Getter
    public static class Response {

        private Long id;
        private String name;
        private String description;
        private int price;
        private int quantity;

        public Response(Item item) {
            this.id = item.getId();
            this.name = item.getName();
            this.description = item.getDescription();
            this.price = item.getPrice();
            this.quantity = item.getQuantity();
        }
    }
}
