package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class RolePermission protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "role_permission_id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    var role: Role? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    var permission: Permission
): Base() {
    companion object {
        fun create(permission: Permission): RolePermission {
            return RolePermission(permission = permission)
        }
    }

    fun assignToRole(role: Role) {
        this.role = role
    }
}