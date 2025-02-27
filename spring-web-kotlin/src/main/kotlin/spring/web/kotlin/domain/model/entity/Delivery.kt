package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*
import spring.web.kotlin.domain.model.enums.DeliveryStatus

@Entity
class Delivery protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    val id: Long? = null,

    @Embedded
    val address: Address,

    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus,

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    var order: Order? = null,
) : Base() {
    companion object {
        fun create(address: Address) = Delivery(
            status = DeliveryStatus.READY,
            address = address
        )
    }

    fun cancel() {
        this.status = DeliveryStatus.CANCEL
    }
}
