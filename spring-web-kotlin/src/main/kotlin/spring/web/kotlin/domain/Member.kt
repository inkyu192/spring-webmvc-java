package spring.web.kotlin.domain

import jakarta.persistence.*
import spring.web.kotlin.constant.Role

@Entity
class Member private constructor(
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
}