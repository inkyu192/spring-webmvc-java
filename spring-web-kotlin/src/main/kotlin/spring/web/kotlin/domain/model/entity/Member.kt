package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*
import spring.web.kotlin.domain.converter.CryptoAttributeConverter

@Entity
class Member protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    val id: Long? = null,
    val account: String,
    val password: String,

    @Convert(converter = CryptoAttributeConverter::class)
    var name: String,

    @Embedded
    var address: Address,

    @OneToMany(mappedBy = "member", cascade = [(CascadeType.ALL)])
    private val _memberRoles: MutableList<MemberRole> = mutableListOf(),

    @OneToMany(mappedBy = "member", cascade = [(CascadeType.ALL)])
    private val _memberPermissions: MutableList<MemberPermission> = mutableListOf(),
) : Base() {
    @get:Transient
    val memberRoles: List<MemberRole>
        get() = _memberRoles.toList()

    @get:Transient
    val memberPermissions: List<MemberPermission>
        get() = _memberPermissions.toList()

    companion object {
        fun create(
            account: String,
            password: String,
            name: String,
            address: Address,
            memberRoles: List<MemberRole>?,
            memberPermissions: List<MemberPermission>?
        ) = Member(
            account = account,
            password = password,
            name = name,
            address = address
        ).apply {
            memberRoles?.forEach { associateRole(it) }
            memberPermissions?.forEach { associatePermission(it) }
        }
    }

    fun associateRole(memberRole: MemberRole) {
        _memberRoles.add(memberRole)
        memberRole.member = this
    }

    fun associatePermission(memberPermission: MemberPermission) {
        _memberPermissions.add(memberPermission)
        memberPermission.member = this
    }

    fun update(name: String, address: Address) {
        this.name = name
        this.address = address
    }
}
