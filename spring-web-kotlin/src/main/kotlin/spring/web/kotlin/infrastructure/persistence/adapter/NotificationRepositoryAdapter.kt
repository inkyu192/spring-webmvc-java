package spring.web.kotlin.infrastructure.persistence.adapter

import org.springframework.stereotype.Repository
import spring.web.kotlin.domain.model.entity.Notification
import spring.web.kotlin.domain.repository.NotificationRepository
import spring.web.kotlin.infrastructure.persistence.NotificationJpaRepository

@Repository
class NotificationRepositoryAdapter(
    private val jpaRepository: NotificationJpaRepository
) : NotificationRepository {
    override fun save(notification: Notification) = jpaRepository.save(notification)
}