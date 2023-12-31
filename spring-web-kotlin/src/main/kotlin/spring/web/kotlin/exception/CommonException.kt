package spring.web.kotlin.exception

import spring.web.kotlin.constant.ApiResponseCode

class CommonException(
    val apiResponseCode: ApiResponseCode
) : RuntimeException()