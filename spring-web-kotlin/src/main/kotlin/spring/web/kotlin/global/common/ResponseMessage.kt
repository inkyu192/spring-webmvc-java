package spring.web.kotlin.global.common

enum class ResponseMessage(
    val title: String,
    val detail: String,
) {
    AUTHENTICATION_FAILED("Authentication Failed", "The credentials provided are invalid or missing."),
    ORDER_CANCEL_NOT_ALLOWED("Order Cancel Not Allowed", "The order cannot be canceled due to the current state."),
    INSUFFICIENT_QUANTITY("Insufficient Quantity", "There is not enough stock available to complete the request."),
    DUPLICATE_DATA("Duplicate Data", "The data already exists and cannot be duplicated."),
    DATA_NOT_FOUND("Data Not Found", "The requested data could not be found in the system.");
}