package spring.web.java.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.web.java.constant.ApiResponseCode;
import spring.web.java.constant.DeliveryStatus;
import spring.web.java.constant.OrderStatus;
import spring.web.java.exception.CommonException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Base {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public static Order create(Member member, Delivery delivery, List<OrderItem> orderItems) {
        Order order = new Order();

        order.setMember(member);
        order.setDelivery(delivery);
        orderItems.forEach(order::setOrderItem);
        order.status = OrderStatus.ORDER;
        order.orderDate = LocalDateTime.now();

        return order;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void setOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new CommonException(ApiResponseCode.ORDER_NOT_CANCEL);
        }

        status = OrderStatus.CANCEL;
        orderItems.forEach(OrderItem::cancel);
        delivery.cancel();
    }
}
