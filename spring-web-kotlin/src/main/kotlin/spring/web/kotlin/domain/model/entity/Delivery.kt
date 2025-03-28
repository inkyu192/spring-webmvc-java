package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*
import spring.web.kotlin.domain.converter.CryptoAttributeConverter
import spring.web.kotlin.domain.model.enums.DeliveryStatus

@Entity
class Delivery protected constructor(
    recipient: String,
    phone: String,
    zipcode: String,
    address: String,
    status: DeliveryStatus,
    order: Order,
) : BaseTime() {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    var id: Long? = null
        protected set

    @Convert(converter = CryptoAttributeConverter::class)
    var recipient: String = recipient
        protected set

    @Convert(converter = CryptoAttributeConverter::class)
    var phone: String = phone
        protected set

    var zipcode: String = zipcode
        protected set

    @Convert(converter = CryptoAttributeConverter::class)
    var address: String = address
        protected set

    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus = status
        protected set

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var order: Order = order
        protected set

    companion object {
        fun create(order: Order, destination: Destination) =
            Delivery(
                order = order,
                recipient = destination.recipient,
                phone = destination.phone,
                zipcode = destination.zipcode,
                address = destination.address,
                status = DeliveryStatus.READY,
            )
    }
}
