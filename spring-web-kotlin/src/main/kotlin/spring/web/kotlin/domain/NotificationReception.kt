package spring.web.kotlin.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class NotificationReception private constructor(
    @Id
    @GeneratedValue
    @Column(name = "notification_reception_id")
    val id: Long?,
    val isRead: Boolean,
    val readDate: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    val notification: Notification,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member
) : Base()