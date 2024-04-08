package spring.web.kotlin.constant

enum class DeliveryStatus(
    val description: String
) {
    READY("준비"),
    CANCEL("취소"),
    COMP("완료")
}