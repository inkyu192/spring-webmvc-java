package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class Notification protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    var id: Long? = null,
    var title: String,
    var message: String,
    var url: String,
    var isRead: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member
) {
    companion object {
        fun of(member: Member, title: String, message: String, url: String): Notification {
            return Notification(
                id = null, // ID는 자동 생성
                member = member,
                title = title,
                message = message,
                url = url,
                isRead = false
            )
        }
    }
}