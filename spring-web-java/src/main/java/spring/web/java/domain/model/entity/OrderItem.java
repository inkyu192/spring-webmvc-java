package spring.web.java.domain.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends Base {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    private int orderPrice;
    private int count;

    public static OrderItem create(Item item, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.item = item;
        orderItem.orderPrice = item.getPrice();
        orderItem.count = count;

        item.removeQuantity(count);

        return orderItem;
    }

    public void associateOrder(Order order) {
        this.order = order;
    }

    public void cancel() {
        item.addQuantity(count);
    }
}
