package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class MemberRole protected constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var role: Role,
) {
    @Id
    @GeneratedValue
    @Column(name = "member_role_id")
    var id: Long? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var member: Member? = null
        protected set

    companion object {
        fun create(role: Role) = MemberRole(role = role)
    }

    fun associateMember(member: Member) {
        this.member = member
    }
}