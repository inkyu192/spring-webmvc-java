package com.webmvc.javaapi.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String street;
    private String zipcode;

    public static Address create(String city, String street, String zipcode) {
        Address address = new Address();

        address.city = city;
        address.street = street;
        address.zipcode = zipcode;

        return address;
    }
}
