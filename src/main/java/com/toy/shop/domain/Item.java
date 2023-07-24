package com.toy.shop.domain;

import com.toy.shop.exception.CommonException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static com.toy.shop.common.ApiResponseCode.ITEM_QUANTITY_NOT_ENOUGH;
import static com.toy.shop.business.item.dto.ItemDto.Save;
import static com.toy.shop.business.item.dto.ItemDto.Update;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseDomain {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private String description;
    private int price;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static Item createItem(Save requestDto, Category category) {
        Item item = new Item();

        item.name = requestDto.getName();
        item.description = requestDto.getDescription();
        item.price = requestDto.getPrice();
        item.quantity = requestDto.getQuantity();
        item.category = category;

        return item;
    }

    public void updateItem(Update requestDto, Category category) {
        if (StringUtils.hasText(requestDto.getName())) this.name = requestDto.getName();
        if (StringUtils.hasText(requestDto.getDescription())) this.description = requestDto.getDescription();
        if (requestDto.getPrice() != null) this.price = requestDto.getPrice();
        if (requestDto.getQuantity() != null) this.quantity = requestDto.getQuantity();
        if (category != null) this.category = category;
    }

    public void removeQuantity(int quantity) {
        int differenceQuantity = this.quantity - quantity;

        if (differenceQuantity < 0) {
            throw new CommonException(ITEM_QUANTITY_NOT_ENOUGH);
        }

        this.quantity = differenceQuantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}
