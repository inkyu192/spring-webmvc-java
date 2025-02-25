package spring.web.kotlin.presentation.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.web.kotlin.application.service.AuthService
import spring.web.kotlin.presentation.dto.request.MemberLoginRequest
import spring.web.kotlin.presentation.dto.request.TokenRequest

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody @Validated memberLoginRequest: MemberLoginRequest) = authService.login(memberLoginRequest)

    @PostMapping("/token/refresh")
    fun refreshToken(@RequestBody @Validated tokenRequest: TokenRequest) = authService.refreshToken(tokenRequest)
}