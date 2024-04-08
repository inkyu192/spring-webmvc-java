package spring.web.kotlin.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import spring.web.kotlin.constant.ApiResponseCode

data class ApiResponse<T>(
    val code: String,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val payload: T?
) {
    constructor() : this(
        code = ApiResponseCode.SUCCESS.name,
        message = ApiResponseCode.SUCCESS.description,
        payload = null
    )
    constructor(payload: T?) : this(
        code = ApiResponseCode.SUCCESS.name,
        message = ApiResponseCode.SUCCESS.description,
        payload = payload
    )
    constructor(apiResponseCode: ApiResponseCode) : this(
        code = apiResponseCode.name,
        message = apiResponseCode.description,
        payload = null
    )
    constructor(apiResponseCode: ApiResponseCode, payload: T?) : this(
        code = apiResponseCode.name,
        message = apiResponseCode.description,
        payload = payload
    )
}
