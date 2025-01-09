package spring.web.kotlin.domain

import jakarta.persistence.Embeddable

@Embeddable
class Address protected constructor(
    var city: String,
    var street: String,
    var zipcode: String
) {
    companion object {
        fun create(city: String, street: String, zipcode: String) =
            Address(
                city = city,
                street = street,
                zipcode = zipcode
            )
    }
}