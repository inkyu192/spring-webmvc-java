package spring.web.kotlin.domain.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Permission protected constructor(
    val name: String,
) : BaseTime() {
    @Id
    @GeneratedValue
    @Column(name = "permission_id")
    var id: Long? = null
        protected set
}