package spring.web.kotlin.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.web.filter.GenericFilterBean
import java.nio.charset.StandardCharsets

class JwtExceptionFilter(
    private val objectMapper: ObjectMapper
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        runCatching { chain.doFilter(request, response) }
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
        response.also {
            it.contentType = MediaType.APPLICATION_JSON_VALUE
            it.characterEncoding = StandardCharsets.UTF_8.toString()
            it.writer.write(objectMapper.writeValueAsString(problemDetail))
        }
    }
}