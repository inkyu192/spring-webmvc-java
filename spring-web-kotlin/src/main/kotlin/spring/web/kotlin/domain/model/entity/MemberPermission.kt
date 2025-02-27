package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class MemberPermission protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "member_permission_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    val permission: Permission
){
    companion object {
        fun create(permission: Permission) = MemberPermission(permission = permission)
    }
}