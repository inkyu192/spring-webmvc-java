package spring.web.kotlin.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Notification private constructor(

    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    val id: Long?,
    val type: String,
    val title: String,
    val message: String,
    val url: String
) : Base()