package com.toy.shop.controller.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

@Getter
public class ItemUpdateDTO {

    private String name;

    @Range(min = 100, max = 1000000)
    private Integer price;

    @Max(value = 9999)
    private Integer quantity;
}
