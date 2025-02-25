package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*
import spring.web.kotlin.domain.converter.CryptoAttributeConverter

@Entity
class Member protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    var id: Long? = null,
    var account: String,
    var password: String,

    @Convert(converter = CryptoAttributeConverter::class)
    var name: String,

    @Embedded
    var address: Address,

    @OneToMany(mappedBy = "member", cascade = [(CascadeType.ALL)])
    var memberRoles: MutableList<MemberRole> = mutableListOf(),

    @OneToMany(mappedBy = "member", cascade = [(CascadeType.ALL)])
    var memberPermissions: MutableList<MemberPermission> = mutableListOf(),
) : Base() {
    companion object {
        fun create(account: String, password: String, name: String, address: Address) =
            Member(
                account = account,
                password = password,
                name = name,
                address = address
            )
    }

    fun addRole(memberRole: MemberRole) {
        memberRoles.add(memberRole)
        memberRole.assignToMember(this)
    }

    fun addPermission(memberPermission: MemberPermission) {
        memberPermissions.add(memberPermission)
        memberPermission.member = this
    }

    fun update(name: String, address: Address) {
        this.name = name
        this.address = address
    }
}
