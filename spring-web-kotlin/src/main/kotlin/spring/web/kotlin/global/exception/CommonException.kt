package spring.web.kotlin.global.exception

import spring.web.kotlin.global.common.ApiResponseCode

class CommonException(
    val apiResponseCode: ApiResponseCode
) : RuntimeException()