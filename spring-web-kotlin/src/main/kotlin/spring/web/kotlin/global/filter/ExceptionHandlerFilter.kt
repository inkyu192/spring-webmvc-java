package spring.web.kotlin.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets

class ExceptionHandlerFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        runCatching { filterChain.doFilter(request, response) }
            .onFailure { throwable ->
                when (throwable) {
                    is JwtException, is AuthenticationException ->
                        handleException(response, HttpStatus.UNAUTHORIZED, throwable.message)

                    is AccessDeniedException ->
                        handleException(response, HttpStatus.FORBIDDEN, throwable.message)

                    is Exception ->
                        handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, throwable.message)
                }
            }
    }

    private fun handleException(response: HttpServletResponse, status: HttpStatus, message: String?) {
        generateProblemDetail(status, message).also { writeResponse(response, it) }
    }

    private fun generateProblemDetail(status: HttpStatus, detail: String?) =
        ProblemDetail.forStatus(status).also {
            it.title = status.reasonPhrase
            it.detail = detail
        }

    private fun writeResponse(response: HttpServletResponse, problemDetail: ProblemDetail) {
        response.status = problemDetail.status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.toString()
        response.writer.write(objectMapper.writeValueAsString(problemDetail))
    }
}
