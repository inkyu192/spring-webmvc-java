package spring.web.java.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.web.java.common.constant.DeliveryStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public static Delivery create(Address address) {
        Delivery delivery = new Delivery();

        delivery.status = DeliveryStatus.READY;
        delivery.address = address;

        return delivery;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void cancel() {
        this.status = DeliveryStatus.CANCEL;
    }
}
