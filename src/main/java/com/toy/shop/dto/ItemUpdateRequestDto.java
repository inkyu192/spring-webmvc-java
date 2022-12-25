package com.toy.shop.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemUpdateRequestDto {

    private String name;

    private String description;

    @Min(100)
    private int price;

    @Max(value = 9999)
    private int quantity;

    private Long categoryId;
}
