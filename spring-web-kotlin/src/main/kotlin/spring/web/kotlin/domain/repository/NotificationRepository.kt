package spring.web.kotlin.domain.repository

import spring.web.kotlin.domain.model.entity.Notification

interface NotificationRepository {
    fun save(notification: Notification): Notification
}