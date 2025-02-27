package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class MemberRole protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "member_role_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    val role: Role
) : Base() {
    companion object {
        fun create(role: Role): MemberRole = MemberRole(role = role)
    }
}