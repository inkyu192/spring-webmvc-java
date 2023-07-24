package com.toy.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.toy.shop.business.order.dto.OrderDto.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public static Delivery createDelivery(Save requestDto) {
        Delivery delivery = new Delivery();

        delivery.address = Address.createAddress(requestDto.getCity(), requestDto.getStreet(), requestDto.getZipcode());
        delivery.status = DeliveryStatus.READY;

        return delivery;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
