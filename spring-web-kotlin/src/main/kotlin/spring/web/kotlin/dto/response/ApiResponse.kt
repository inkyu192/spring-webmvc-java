package spring.web.kotlin.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import spring.web.kotlin.constant.ApiResponseCode

data class ApiResponse<T>(
    val code: String,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val payload: T?
) {
    constructor() : this(ApiResponseCode.SUCCESS.name, ApiResponseCode.SUCCESS.message, null)
    constructor(payload: T?) : this(ApiResponseCode.SUCCESS.name, ApiResponseCode.SUCCESS.message, payload)
    constructor(apiResponseCode: ApiResponseCode) : this(apiResponseCode.name, apiResponseCode.message, null)
    constructor(apiResponseCode: ApiResponseCode, payload: T?) : this(apiResponseCode.name, apiResponseCode.message, payload)
}
