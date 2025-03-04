package spring.web.kotlin.presentation.exception.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import spring.web.kotlin.infrastructure.util.ResponseWriter

@Component
class AuthenticationExceptionHandler(
    private val responseWriter: ResponseWriter
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        responseWriter.writeResponse(
            ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                authException?.message
            )
        )
    }
}