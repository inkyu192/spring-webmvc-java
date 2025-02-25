package spring.web.kotlin.application.event

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.model.entity.Notification
import spring.web.kotlin.domain.repository.MemberRepository
import spring.web.kotlin.domain.repository.NotificationRepository

@Component
@Transactional(readOnly = true)
class NotificationEventListener(
    private val memberRepository: MemberRepository,
    private val notificationRepository: NotificationRepository
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Async
    @EventListener
    @Transactional
    fun handleNotificationEvent(notificationEvent: NotificationEvent) {
        runCatching {
            val member = memberRepository.findByIdOrNull(notificationEvent.memberId) ?: throw RuntimeException()

            notificationRepository.save(
                Notification.of(
                    member,
                    notificationEvent.title,
                    notificationEvent.message,
                    notificationEvent.url
                )
            )
        }.onFailure { e -> log.error("알림 이벤트 처리 중 예외 발생: {}", e.message, e) }
    }
}