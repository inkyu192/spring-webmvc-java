package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class Role protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    var id: Long? = null,
    var name: String,

    @OneToMany(mappedBy = "role", cascade = [(CascadeType.ALL)])
    var rolePermissions: MutableList<RolePermission> = mutableListOf(),
): Base() {
    companion object {
        fun create(name: String): Role = Role(name = name)
    }

    fun addPermission(rolePermission: RolePermission) {
        rolePermissions.add(rolePermission)
        rolePermission.assignToRole(this)
    }

    fun update(name: String, rolePermissions: List<RolePermission>) {
        this.name = name
        this.rolePermissions = rolePermissions.toMutableList()
    }
}