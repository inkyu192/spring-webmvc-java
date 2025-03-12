package spring.web.kotlin.domain.model.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class Base protected constructor() {
    @CreatedDate
    @Column(updatable = false)
    var createdAt: Instant = Instant.now()
        protected set

    @LastModifiedDate
    var updatedAt: Instant = Instant.now()
        protected set
}