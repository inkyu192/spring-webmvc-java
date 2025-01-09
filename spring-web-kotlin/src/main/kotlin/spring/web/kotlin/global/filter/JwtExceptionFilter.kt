package spring.web.kotlin.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.GenericFilterBean
import spring.web.kotlin.global.common.ApiResponse
import spring.web.kotlin.global.common.ApiResponseCode
import java.nio.charset.StandardCharsets

class JwtExceptionFilter(
    private val objectMapper: ObjectMapper
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        runCatching { chain.doFilter(request, response) }
            .onFailure { e ->
                when (e) {
                    is UnsupportedJwtException -> setResponse(response, ApiResponseCode.UNSUPPORTED_TOKEN)
                    is ExpiredJwtException -> setResponse(response, ApiResponseCode.EXPIRED_TOKEN)
                    is JwtException -> setResponse(response, ApiResponseCode.BAD_TOKEN)
                }
            }
    }

    private fun setResponse(response: ServletResponse, apiResponseCode: ApiResponseCode) {
        val result = objectMapper.writeValueAsString(ApiResponse<ApiResponseCode>(apiResponseCode))

        response.let {
            it.contentType = MediaType.APPLICATION_JSON_VALUE
            it.characterEncoding = StandardCharsets.UTF_8.toString()
            it.writer.write(result)
        }
    }
}