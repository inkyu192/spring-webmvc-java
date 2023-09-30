package com.toy.shop.domain;

import com.toy.shop.dto.request.ItemSaveRequest;
import com.toy.shop.dto.request.ItemUpdateRequest;
import com.toy.shop.exception.CommonException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static com.toy.shop.common.ApiResponseCode.ITEM_QUANTITY_NOT_ENOUGH;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static Item createItem(ItemSaveRequest itemSaveRequest, Category category) {
        Item item = new Item();

        item.name = itemSaveRequest.name();
        item.description = itemSaveRequest.description();
        item.price = itemSaveRequest.price();
        item.quantity = itemSaveRequest.quantity();
        item.category = category;

        return item;
    }

    public void updateItem(ItemUpdateRequest itemUpdateRequest, Category category) {
        if (StringUtils.hasText(itemUpdateRequest.name())) this.name = itemUpdateRequest.name();
        if (StringUtils.hasText(itemUpdateRequest.description())) this.description = itemUpdateRequest.description();
        this.price = itemUpdateRequest.price();
        this.quantity = itemUpdateRequest.quantity();
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
