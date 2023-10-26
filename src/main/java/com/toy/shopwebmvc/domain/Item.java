package com.toy.shopwebmvc.domain;

import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.constant.Category;
import com.toy.shopwebmvc.exception.CommonException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseDomain {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private String description;
    private int price;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder(builderMethodName = "create")
    public Item(String name, String description, int price, int quantity, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public void update(String name, String description, int price, int quantity, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public void removeQuantity(int quantity) {
        int differenceQuantity = this.quantity - quantity;

        if (differenceQuantity < 0) {
            throw new CommonException(ApiResponseCode.QUANTITY_NOT_ENOUGH);
        }

        this.quantity = differenceQuantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}
