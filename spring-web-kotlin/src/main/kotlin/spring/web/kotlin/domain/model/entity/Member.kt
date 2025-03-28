package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*
import spring.web.kotlin.domain.converter.CryptoAttributeConverter
import java.time.LocalDate

@Entity
class Member protected constructor(
    @Convert(converter = CryptoAttributeConverter::class)
    val account: String,
    password: String,
    name: String,
    phone: String,
    birthDate: LocalDate,
) : BaseTime() {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    var id: Long? = null
        protected set

    var password: String = password
        protected set

    @Convert(converter = CryptoAttributeConverter::class)
    var name: String = name
        protected set

    @Convert(converter = CryptoAttributeConverter::class)
    var phone: String = phone
        protected set

    var birthDate: LocalDate = birthDate
        protected set

    @OneToMany(mappedBy = "member", cascade = [(CascadeType.ALL)])
    private val _memberRoles: MutableList<MemberRole> = mutableListOf()

    @get:Transient
    val memberRoles: List<MemberRole>
        get() = _memberRoles.toList()

    @OneToMany(mappedBy = "member", cascade = [(CascadeType.ALL)])
    private val _memberPermissions: MutableList<MemberPermission> = mutableListOf()

    @get:Transient
    val memberPermissions: List<MemberPermission>
        get() = _memberPermissions.toList()

    companion object {
        fun create(
            account: String,
            password: String,
            name: String,
            phone: String,
            birthDate: LocalDate,
            memberRoles: List<MemberRole>,
            memberPermissions: List<MemberPermission>
        ) = Member(
            account = account,
            password = password,
            name = name,
            phone = phone,
            birthDate = birthDate,
        ).apply {
            memberRoles.forEach { this.associateRole(it) }
            memberPermissions.forEach { this.associatePermission(it) }
        }
    }

    fun associateRole(memberRole: MemberRole) {
        _memberRoles.add(memberRole)
        memberRole.associateMember(this)
    }

    fun associatePermission(memberPermission: MemberPermission) {
        _memberPermissions.add(memberPermission)
        memberPermission.associateMember(this)
    }

    fun update(password: String?, name: String?, phone: String?, birthDate: LocalDate?) {
        password?.let { this.password = password }
        name?.let { this.name = name }
        phone?.let { this.phone = phone }
        birthDate?.let { this.birthDate = birthDate }
    }
}
