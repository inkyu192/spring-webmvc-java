package spring.web.kotlin.constant

import spring.web.kotlin.exception.CommonException

enum class Role(
    val description: String
) {
    ROLE_ADMIN("어드민"),
    ROLE_BUYER("소비자"),
    ROLE_SELLER("판매자");

    companion object {
        fun of(name: Any?) = entries.find { it.name == name }
            ?: throw CommonException(ApiResponseCode.BAD_REQUEST)
    }
}