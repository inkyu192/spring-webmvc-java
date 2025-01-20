package spring.web.kotlin.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets

class JwtExceptionFilter(
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
                    is JwtException -> ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED).also {
                        it.title = HttpStatus.UNAUTHORIZED.reasonPhrase
                        it.detail = throwable.message
                        writeResponse(response, it)
                    }

                    else -> throw throwable
                }
            }
    }

    private fun writeResponse(response: ServletResponse, problemDetail: ProblemDetail) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.toString()
        response.writer.write(objectMapper.writeValueAsString(problemDetail))
    }
}