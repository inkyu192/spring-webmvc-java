package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class MemberPermission protected constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var permission: Permission,
) {
    @Id
    @GeneratedValue
    @Column(name = "member_permission_id")
    var id: Long? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var member: Member? = null
        protected set

    companion object {
        fun create(permission: Permission) = MemberPermission(permission = permission)
    }

    fun associateMember(member: Member) {
        this.member = member
    }
}