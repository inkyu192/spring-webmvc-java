package com.toy.shopwebmvc.domain;

import com.toy.shopwebmvc.constant.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder(builderMethodName = "create")
    public Delivery(String city, String street, String zipcode) {
        this.status = DeliveryStatus.READY;
        this.address = Address.create(city, street, zipcode);
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
