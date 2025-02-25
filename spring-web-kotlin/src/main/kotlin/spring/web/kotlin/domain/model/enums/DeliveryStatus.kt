package spring.web.kotlin.domain.model.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class DeliveryStatus(
    val description: String
) {
    READY("준비"),
    CANCEL("취소"),
    COMP("완료");

    companion object {
        @JsonCreator
        @JvmStatic
        fun of(name: String) = entries.find { it.name == name }
    }
}