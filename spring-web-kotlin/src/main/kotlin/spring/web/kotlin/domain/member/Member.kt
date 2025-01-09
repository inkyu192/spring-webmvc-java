package spring.web.kotlin.domain.member

import jakarta.persistence.*
import spring.web.kotlin.domain.Address

@Entity
class Member protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    val id: Long? = null,
    val account: String,
    val password: String,
    var name: String,

    @Enumerated(EnumType.STRING)
    var role: Role,

    @Embedded
    var address: Address
) {
    companion object {
        fun create(account: String, password: String, name: String, role: Role, address: Address) =
            Member(
                account = account,
                password = password,
                name = name,
                role = role,
                address = address
            )
    }

    fun update(name: String, role: Role, address: Address) {
        this.name = name
        this.role = role
        this.address = address
    }

    enum class Role(
        val description: String
    ) {
        ROLE_ADMIN("어드민"),
        ROLE_BUYER("소비자"),
        ROLE_SELLER("판매자");

        companion object {
            fun of(name: Any?) = entries.find { it.name == name }
        }
    }
}
