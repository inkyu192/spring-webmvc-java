package spring.web.kotlin.presentation.exception

import io.jsonwebtoken.JwtException
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.FilterChain
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import spring.web.kotlin.infrastructure.util.ResponseWriter
import spring.web.kotlin.presentation.exception.handler.ExceptionHandlerFilter

class ExceptionHandlerFilterTest : DescribeSpec({
    val filterChain = mockk<FilterChain>(relaxed = true)
    val responseWriter = mockk<ResponseWriter>(relaxed = true)
    val exceptionHandlerFilter = ExceptionHandlerFilter(responseWriter)
    lateinit var request: MockHttpServletRequest
    lateinit var response: MockHttpServletResponse

    beforeEach {
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
    }

    describe("ExceptionHandlerFilter 는") {
        context("JwtException 발생할 경우") {
            it("UNAUTHORIZED 반환한다") {
                val status = HttpStatus.UNAUTHORIZED
                val message = "JwtException"

                every { filterChain.doFilter(request, response) } throws JwtException(message)

                exceptionHandlerFilter.doFilter(request, response, filterChain)

                verify { responseWriter.writeResponse(ProblemDetail.forStatusAndDetail(status, message)) }
            }
        }

        context("RuntimeException 발생할 경우") {
            it("INTERNAL_SERVER_ERROR 반환한다") {
                val status = HttpStatus.INTERNAL_SERVER_ERROR
                val message = "RuntimeException"

                every { filterChain.doFilter(request, response) } throws RuntimeException(message)

                exceptionHandlerFilter.doFilter(request, response, filterChain)

                verify { responseWriter.writeResponse(ProblemDetail.forStatusAndDetail(status, message)) }
            }
        }
    }
})
