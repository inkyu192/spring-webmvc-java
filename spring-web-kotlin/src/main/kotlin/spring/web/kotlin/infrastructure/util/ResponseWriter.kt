package spring.web.kotlin.infrastructure.util

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class ResponseWriter(
    private val objectMapper: ObjectMapper,
    private val response: HttpServletResponse
) {
    fun writeResponse(problemDetail: ProblemDetail) {
        response.status = problemDetail.status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.toString()
        response.writer.write(objectMapper.writeValueAsString(problemDetail))
    }
}