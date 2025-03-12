package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class RolePermission protected constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var permission: Permission,
) {
    @Id
    @GeneratedValue
    @Column(name = "role_permission_id")
    var id: Long? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var role: Role? = null
        protected set

    companion object {
        fun create(permission: Permission) = RolePermission(permission = permission)
    }

    fun associateRole(role: Role) {
        this.role = role
    }
}