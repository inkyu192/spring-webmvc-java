package spring.web.kotlin.constant

enum class OrderStatus(
    val description: String
) {
    ORDER("주문"),
    CANCEL("취소")
}