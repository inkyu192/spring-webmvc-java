package spring.web.java.domain.item;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import spring.web.java.domain.Base;
import spring.web.java.global.common.ResponseMessage;
import spring.web.java.global.exception.CommonException;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends Base {

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

    public static Item create(String name, String description, int price, int quantity, Category category) {
        Item item = new Item();

        item.name = name;
        item.description = description;
        item.price = price;
        item.quantity = quantity;
        item.category = category;

        return item;
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
            throw new CommonException(ResponseMessage.INSUFFICIENT_QUANTITY);
        }

        this.quantity = differenceQuantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    @RequiredArgsConstructor
    public enum Category {

        ROLE_BOOK("책"),
        ROLE_TICKET("표");

        private final String description;

        @JsonCreator
        public static Category of(Object name) {
            return Arrays.stream(Category.values())
                .filter(category -> category.name().equals(name))
                .findFirst()
                .orElse(null);
        }
    }
}
