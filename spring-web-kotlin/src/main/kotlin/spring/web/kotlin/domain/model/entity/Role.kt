package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class Role protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    val id: Long? = null,
    val name: String,

    @OneToMany(mappedBy = "role", cascade = [(CascadeType.ALL)])
    private val _rolePermissions: MutableList<RolePermission> = mutableListOf(),
): Base() {
    @get:Transient
    val rolePermissions: List<RolePermission>
        get() = _rolePermissions.toList()

    companion object {
        fun create(name: String): Role = Role(name = name)
    }

    fun associatePermission(rolePermission: RolePermission) {
        _rolePermissions.add(rolePermission)
        rolePermission.role = this
    }
}