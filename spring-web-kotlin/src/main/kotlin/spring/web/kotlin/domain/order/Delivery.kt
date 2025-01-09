package spring.web.kotlin.domain.order

import jakarta.persistence.*
import spring.web.kotlin.domain.Address

@Entity
class Delivery protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    val id: Long? = null,

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    val order: Order? = null,

    @Embedded
    val address: Address,

    @Enumerated(EnumType.STRING)
    val status: Status
) {
    companion object {
        fun create(address: Address) = Delivery(
            status = Status.READY,
            address = address
        )
    }

    enum class Status(
        val description: String
    ) {
        READY("준비"),
        CANCEL("취소"),
        COMP("완료")
    }
}
