package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*
import spring.web.kotlin.domain.converter.CryptoAttributeConverter

@Entity
class Destination protected constructor(
    recipient: String,
    phone: String,
    zipcode: String,
    address: String,
    isDefault: Boolean,
    member: Member,
) : Base() {
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

    var isDefault: Boolean = isDefault
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var member: Member = member
        protected set

    companion object {
        fun create(
            member: Member,
            recipient: String,
            phone: String,
            zipcode: String,
            address: String,
            isDefault: Boolean
        ) = Destination(
            recipient = recipient,
            phone = phone,
            zipcode = zipcode,
            address = address,
            isDefault = isDefault,
            member = member
        )
    }
}