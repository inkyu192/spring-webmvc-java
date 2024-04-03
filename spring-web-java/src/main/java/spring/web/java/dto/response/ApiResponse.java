package spring.web.java.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import spring.web.java.constant.ApiResponseCode;


public record ApiResponse<T>(
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T payload
) {
    public ApiResponse() {
        this(ApiResponseCode.SUCCESS.name(), ApiResponseCode.SUCCESS.getDescription(), null);
    }

    public ApiResponse(T payload) {
        this(ApiResponseCode.SUCCESS.name(), ApiResponseCode.SUCCESS.getDescription(), payload);
    }

    public ApiResponse(ApiResponseCode apiResponseCode) {
        this(apiResponseCode.name(), apiResponseCode.getDescription(), null);
    }

    public ApiResponse(ApiResponseCode apiResponseCode, T payload) {
        this(apiResponseCode.name(), apiResponseCode.getDescription(), payload);
    }
}
