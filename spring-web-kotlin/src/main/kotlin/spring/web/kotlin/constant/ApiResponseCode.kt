package spring.web.kotlin.constant

enum class ApiResponseCode(
    val description: String
) {
    SUCCESS("성공"),
    PARAMETER_NOT_VALID("유효성 검증 실패"),
    BAD_CREDENTIALS("자격 증명 실패"),
    BAD_TOKEN("잘못된 토큰"),
    EXPIRED_TOKEN("만료된 토큰"),
    UNSUPPORTED_TOKEN("지원되지 않는 토큰"),
    ORDER_NOT_CANCEL("주문 취소가 불가"),
    QUANTITY_NOT_ENOUGH("수량 부족"),
    DATA_DUPLICATE("데이터 중복"),
    DATA_NOT_FOUND("데이터 없음"),
    BAD_REQUEST("잘못된 요청"),
    SYSTEM_ERROR("시스템 오류");
}