package spring.web.kotlin.constant

enum class Role(
    val description: String
) {
    ROLE_ADMIN("어드민"),
    ROLE_BUYER("소비자"),
    ROLE_SELLER("판매자")
}