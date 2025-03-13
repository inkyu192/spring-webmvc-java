package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*
import spring.web.kotlin.domain.model.enums.DeliveryStatus

@Entity
class Delivery protected constructor(
    city: String,
    street: String,
    zipcode: String,
    status: DeliveryStatus,
) : Base() {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    var id: Long? = null
        protected set

    var city: String = city
        protected set

    var street: String = street
        protected set

    var zipcode: String = zipcode
        protected set

    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus = status
        protected set

    companion object {
        fun create(city: String, street: String, zipcode: String) =
            Delivery(
                city = city,
                street = street,
                zipcode = zipcode,
                status = DeliveryStatus.READY,
            )
    }
}
