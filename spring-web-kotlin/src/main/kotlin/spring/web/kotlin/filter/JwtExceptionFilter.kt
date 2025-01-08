package spring.web.kotlin.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.GenericFilterBean
import spring.web.kotlin.constant.ApiResponseCode
import spring.web.kotlin.dto.response.ApiResponse

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

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write(result)
    }
}