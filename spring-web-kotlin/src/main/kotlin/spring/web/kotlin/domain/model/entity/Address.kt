package spring.web.kotlin.domain.model.entity

import jakarta.persistence.Embeddable

@Embeddable
class Address protected constructor(
    val city: String,
    val street: String,
    val zipcode: String
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