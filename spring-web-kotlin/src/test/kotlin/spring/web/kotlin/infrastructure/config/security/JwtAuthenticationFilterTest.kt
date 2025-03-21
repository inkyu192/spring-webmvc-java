package spring.web.kotlin.infrastructure.config.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.FilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder

class JwtAuthenticationFilterTest : DescribeSpec({
    val filterChain = mockk<FilterChain>(relaxed = true)
    val jwtTokenProvider = mockk<JwtTokenProvider>()
    val jwtAuthenticationFilter = JwtAuthenticationFilter(jwtTokenProvider)
    lateinit var request: MockHttpServletRequest
    lateinit var response: MockHttpServletResponse

    beforeEach {
        SecurityContextHolder.clearContext()
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
    }

    describe("JwtAuthenticationFilter 는") {
        context("토큰이 null 일 경우") {
            it("authentication 을 생성하지 않는다") {
                jwtAuthenticationFilter.doFilter(request, response, filterChain)

                SecurityContextHolder.getContext().authentication shouldBe null
            }
        }

        context("토큰이 비어있을 경우") {
            it("authentication 을 생성하지 않는다") {
                request.addHeader("Authorization", "")

                jwtAuthenticationFilter.doFilter(request, response, filterChain)

                SecurityContextHolder.getContext().authentication shouldBe null
            }
        }

        context("잘못된 토큰일 경우") {
            it("JwtException 발생한다") {
                every { jwtTokenProvider.parseAccessToken(any()) } throws JwtException("invalidToken")

                request.addHeader("Authorization", "Bearer invalid.jwt.token")

                shouldThrow<JwtException> { jwtAuthenticationFilter.doFilter(request, response, filterChain) }
            }
        }

        context("유효한 토큰일 경우") {
            it("authentication 을 생성 한다") {
                val token = "valid.jwt.token"
                val claims = mockk<Claims>(relaxed = true)
                val memberId = 1L
                val permissions = listOf("ITEM_READ")

                every { jwtTokenProvider.parseAccessToken(any()) } returns claims
                every { claims["memberId"] } returns memberId
                every { claims["permissions", List::class.java] } returns permissions

                request.addHeader("Authorization", "Bearer $token")

                jwtAuthenticationFilter.doFilter(request, response, filterChain)

                SecurityContextHolder.getContext().authentication shouldNotBe null
                SecurityContextHolder.getContext().authentication.credentials shouldBe token
            }
        }
    }
})
