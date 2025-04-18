package spring.webmvc.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.presentation.exception.InsufficientQuantityException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public class Product extends BaseCreator {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;
    protected String name;
    protected String description;
    protected int price;
    protected int quantity;

    @Enumerated(EnumType.STRING)
    protected Category category;

    public static Product create(String name, String description, int price, int quantity, Category category) {
        Product product = new Product();

        product.name = name;
        product.description = description;
        product.price = price;
        product.quantity = quantity;
        product.category = category;

        return product;
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
            throw new InsufficientQuantityException(name, quantity, this.quantity);
        }

        this.quantity = differenceQuantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}
