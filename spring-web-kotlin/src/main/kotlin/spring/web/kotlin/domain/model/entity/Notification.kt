package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
class Notification protected constructor(
    val title: String,
    val message: String,
    val url: String,
    isRead: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var member: Member,
) : Base() {
    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    var id: Long? = null
        protected set

    var isRead: Boolean = isRead
        protected set

    companion object {
        fun of(member: Member, title: String, message: String, url: String) =
            Notification(
                title = title,
                message = message,
                url = url,
                isRead = false,
                member = member
            )
    }
}