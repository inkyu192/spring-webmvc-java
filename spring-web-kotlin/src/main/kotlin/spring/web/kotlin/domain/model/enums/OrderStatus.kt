package spring.web.kotlin.domain.model.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class OrderStatus(
    val description: String
) {
    ORDER("주문"),
    CANCEL("취소");

    companion object {
        @JsonCreator
        fun of(name: String) = entries.find { it.name == name }
    }
}