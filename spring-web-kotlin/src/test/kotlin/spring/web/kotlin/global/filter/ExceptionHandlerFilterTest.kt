package spring.web.kotlin.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.MalformedJwtException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.FilterChain
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

class ExceptionHandlerFilterTest : DescribeSpec({
    val filterChain = mockk<FilterChain>()
    val objectMapper = mockk<ObjectMapper>()
    val exceptionHandlerFilter = ExceptionHandlerFilter(objectMapper)
    lateinit var request: MockHttpServletRequest
    lateinit var response: MockHttpServletResponse

    beforeEach {
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
    }

    describe("ExceptionHandlerFilter는") {
        context("MalformedJwtException 발생할 경우") {
            it("BAD_REQUEST 반환 한다") {
                val status = HttpStatus.BAD_REQUEST
                val message = "MalformedJwtException"
                val responseBody = """
                    {
                        "status": ${status.value()},
                        "title": "${status.reasonPhrase}",
                        "detail": "$message"
                    }
                """.trimIndent()

                every { filterChain.doFilter(request, response) } throws MalformedJwtException(message)
                every { objectMapper.writeValueAsString(any<ProblemDetail>()) } returns responseBody

                exceptionHandlerFilter.doFilter(request, response, filterChain)

                response.status shouldBe status.value()
                response.contentAsString shouldBe responseBody
            }
        }

        context("JwtException 발생할 경우") {
            it("UNAUTHORIZED 반환 한다") {
                val status = HttpStatus.UNAUTHORIZED
                val message = "JwtException"
                val responseBody = """
                    {
                        "status": ${status.value()},
                        "title": "${status.reasonPhrase}",
                        "detail": "$message"
                    }
                """.trimIndent()

                every { filterChain.doFilter(request, response) } throws JwtException(message)
                every { objectMapper.writeValueAsString(any<ProblemDetail>()) } returns responseBody

                exceptionHandlerFilter.doFilter(request, response, filterChain)

                response.status shouldBe status.value()
                response.contentAsString shouldBe responseBody
            }
        }

        context("RuntimeException 발생할 경우") {
            it("INTERNAL_SERVER_ERROR 반환 한다") {
                val status = HttpStatus.INTERNAL_SERVER_ERROR
                val message = "RuntimeException"
                val responseBody = """
                    {
                        "status": ${status.value()},
                        "title": "${status.reasonPhrase}",
                        "detail": "$message"
                    }
                """.trimIndent()

                every { filterChain.doFilter(request, response) } throws RuntimeException(message)
                every { objectMapper.writeValueAsString(any<ProblemDetail>()) } returns responseBody

                exceptionHandlerFilter.doFilter(request, response, filterChain)

                response.status shouldBe status.value()
                response.contentAsString shouldBe responseBody
            }
        }
    }
})
