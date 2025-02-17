package spring.web.kotlin.global.common

enum class ResponseMessage(
    val detail: String,
) {
    AUTHENTICATION_FAILED("The credentials provided are invalid or missing."),
    ORDER_CANCEL_NOT_ALLOWED("The order cannot be canceled due to the current state."),
    INSUFFICIENT_QUANTITY("There is not enough stock available to complete the request."),
    DUPLICATE_DATA("The data already exists and cannot be duplicated."),
    DATA_NOT_FOUND("The requested data could not be found in the system."),
    DUPLICATE_REQUEST("The request has already been processed or is being processed. Please wait and try again later.");
}