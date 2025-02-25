package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class MemberRole protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "member_role_id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    var role: Role
) : Base() {
    companion object {
        fun create(role: Role): MemberRole = MemberRole(role = role)
    }

    fun assignToMember(member: Member) {
        this.member = member
    }
}