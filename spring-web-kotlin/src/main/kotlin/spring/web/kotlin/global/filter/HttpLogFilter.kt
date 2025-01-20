package spring.web.kotlin.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.server.PathContainer
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.pattern.PathPattern
import org.springframework.web.util.pattern.PathPatternParser
import spring.web.kotlin.global.common.HttpLog
import java.util.*

class HttpLogFilter: OncePerRequestFilter() {
    companion object {
        const val TRANSACTION_ID = "transactionId"
    }

    private val pathPatternParser = PathPatternParser()
    private val logExcludeList: List<PathPattern> = listOf(
        pathPatternParser.parse("/actuator/**"),
        pathPatternParser.parse("/favicon.ico"),
        pathPatternParser.parse("/static/**"),
        pathPatternParser.parse("/public/**")
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (isLogExclude(request)) {
            filterChain.doFilter(request, response)
            return
        }

        val requestWrapper = ContentCachingRequestWrapper(request)
        val responseWrapper = ContentCachingResponseWrapper(response)

        val transactionId = UUID.randomUUID().toString()
        request.setAttribute(TRANSACTION_ID, transactionId)

        val startTime = System.currentTimeMillis()
        filterChain.doFilter(requestWrapper, responseWrapper)
        val endTime = System.currentTimeMillis()

        val httpLog = HttpLog(transactionId, requestWrapper, responseWrapper, (endTime - startTime) / 1000.0)
        httpLog.log()

        responseWrapper.copyBodyToResponse()
    }

    private fun isLogExclude(request: HttpServletRequest): Boolean {
        val uri = request.requestURI
        val pathContainer = PathContainer.parsePath(uri)
        return logExcludeList.any { it.matches(pathContainer) }
    }
}
