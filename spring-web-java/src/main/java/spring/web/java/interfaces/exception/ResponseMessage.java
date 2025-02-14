package spring.web.java.interfaces.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    AUTHENTICATION_FAILED("Authentication Failed", "The credentials provided are invalid or missing."),
    ORDER_CANCEL_NOT_ALLOWED("Order Cancel Not Allowed", "The order cannot be canceled due to the current state."),
    INSUFFICIENT_QUANTITY("Insufficient Quantity", "There is not enough stock available to complete the request."),
    DUPLICATE_DATA("Duplicate Data", "The data already exists and cannot be duplicated."),
    DATA_NOT_FOUND("Data Not Found", "The requested data could not be found in the system."),
    DUPLICATE_REQUEST("Duplicate Request", "The request has already been processed or is being processed. Please wait and try again later.");

    private final String title;
    private final String detail;
}
