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
        this(ApiResponseCode.SUCCESS.name(), ApiResponseCode.SUCCESS.getMessage(), null);
    }

    public ApiResponse(T payload) {
        this(ApiResponseCode.SUCCESS.name(), ApiResponseCode.SUCCESS.getMessage(), payload);
    }

    public ApiResponse(ApiResponseCode apiResponseCode) {
        this(apiResponseCode.name(), apiResponseCode.getMessage(), null);
    }

    public ApiResponse(ApiResponseCode apiResponseCode, T payload) {
        this(apiResponseCode.name(), apiResponseCode.getMessage(), payload);
    }

    public ApiResponse(ApiResponseCode apiResponseCode, String message) {
        this(apiResponseCode.name(), message, null);
    }
}
