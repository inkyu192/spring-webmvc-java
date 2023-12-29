package spring.web.java.exception;

import lombok.Getter;
import spring.web.java.constant.ApiResponseCode;

@Getter
public class CommonException extends RuntimeException {

    private final ApiResponseCode apiResponseCode;

    public CommonException(ApiResponseCode apiResponseCode) {
        this.apiResponseCode = apiResponseCode;
    }
}
