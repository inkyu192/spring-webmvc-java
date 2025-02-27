package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class Notification protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    val id: Long? = null,
    val title: String,
    val message: String,
    val url: String,
    val isRead: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member
) : Base() {
    companion object {
        fun of(member: Member, title: String, message: String, url: String) =
            Notification(
                member = member,
                title = title,
                message = message,
                url = url
            )
    }
}