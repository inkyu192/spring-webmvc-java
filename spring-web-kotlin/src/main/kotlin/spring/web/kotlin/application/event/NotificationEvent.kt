package spring.web.kotlin.application.event

data class NotificationEvent(
    val memberId: Long,
    val title: String,
    val message: String,
    val url: String,
)
