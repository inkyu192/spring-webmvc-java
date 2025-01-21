package spring.web.kotlin.domain.auth.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.web.kotlin.domain.auth.service.AuthService
import spring.web.kotlin.domain.member.dto.MemberLoginRequest
import spring.web.kotlin.domain.token.dto.TokenRequest

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody memberLoginRequest: MemberLoginRequest) = authService.login(memberLoginRequest)

    @PostMapping("/token/refresh")
    fun refreshToken(@RequestBody tokenRequest: TokenRequest) = authService.refreshToken(tokenRequest)
}