package com.toy.shopwebmvc.domain;

import com.toy.shopwebmvc.constant.Category;
import com.toy.shopwebmvc.dto.request.ItemSaveRequest;
import com.toy.shopwebmvc.dto.request.ItemUpdateRequest;
import com.toy.shopwebmvc.exception.CommonException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import static com.toy.shopwebmvc.constant.ApiResponseCode.BAD_REQUEST;

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

    public static Item createItem(ItemSaveRequest itemSaveRequest) {
        Item item = new Item();

        item.name = itemSaveRequest.name();
        item.description = itemSaveRequest.description();
        item.price = itemSaveRequest.price();
        item.quantity = itemSaveRequest.quantity();
        item.category = itemSaveRequest.category();

        return item;
    }

    public void updateItem(ItemUpdateRequest itemUpdateRequest) {
        if (StringUtils.hasText(itemUpdateRequest.name())) this.name = itemUpdateRequest.name();
        if (StringUtils.hasText(itemUpdateRequest.description())) this.description = itemUpdateRequest.description();
        if (!ObjectUtils.isEmpty(itemUpdateRequest.category())) this.category = itemUpdateRequest.category();
        this.price = itemUpdateRequest.price();
        this.quantity = itemUpdateRequest.quantity();
    }

    public void removeQuantity(int quantity) {
        int differenceQuantity = this.quantity - quantity;

        if (differenceQuantity < 0) {
            throw new CommonException(BAD_REQUEST);
        }

        this.quantity = differenceQuantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}
