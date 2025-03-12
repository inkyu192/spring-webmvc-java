package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class Role protected constructor(
    name: String,
) : Base() {
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    var id: Long? = null
        protected set

    var name: String = name
        protected set

    @OneToMany(mappedBy = "role", cascade = [(CascadeType.ALL)])
    private val _rolePermissions: MutableList<RolePermission> = mutableListOf()

    @get:Transient
    val rolePermissions: List<RolePermission>
        get() = _rolePermissions.toList()

    companion object {
        fun create(name: String, rolePermission: List<RolePermission>) =
            Role(name = name).apply { rolePermission.forEach { associatePermission(it) } }
    }

    fun associatePermission(rolePermission: RolePermission) {
        _rolePermissions.add(rolePermission)
        rolePermission.associateRole(this)
    }
}