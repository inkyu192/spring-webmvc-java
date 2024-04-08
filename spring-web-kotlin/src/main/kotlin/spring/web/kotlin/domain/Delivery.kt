package spring.web.kotlin.domain

import jakarta.persistence.*
import spring.web.kotlin.constant.DeliveryStatus

@Entity
class Delivery private constructor(
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    val id: Long? = null,

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    val order: Order? = null,

    @Embedded
    val address: Address,

    @Enumerated(EnumType.STRING)
    val status: DeliveryStatus
){
    companion object {
        fun create(address: Address) = Delivery(
            status = DeliveryStatus.READY,
            address = address
        )
    }
}