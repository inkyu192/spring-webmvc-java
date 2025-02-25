package spring.web.kotlin.infrastructure.logging

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.StandardCharsets

data class HttpLog(
    val transactionId: String,
    val httpMethod: HttpMethod,
    val requestUri: String,
    val remoteAddr: String,
    val elapsedTime: Double,
    val requestHeader: String,
    val requestParam: String,
    val requestBody: String,
    val responseBody: String
) {
    private val log = LoggerFactory.getLogger(HttpLog::class.java)

    constructor(
        transactionId: String,
        requestWrapper: ContentCachingRequestWrapper,
        responseWrapper: ContentCachingResponseWrapper,
        elapsedTime: Double
    ): this(
        transactionId = transactionId,
        httpMethod = HttpMethod.valueOf(requestWrapper.method),
        requestUri = requestWrapper.requestURI,
        remoteAddr = requestWrapper.remoteAddr,
        elapsedTime = elapsedTime,
        requestHeader = getHeader(requestWrapper),
        requestParam = getParameter(requestWrapper),
        requestBody = convertByteArrayToString(requestWrapper.contentAsByteArray),
        responseBody = convertByteArrayToString(responseWrapper.contentAsByteArray)
    )

    companion object {
        private fun getHeader(requestWrapper: ContentCachingRequestWrapper) =
            requestWrapper.headerNames.asSequence()
                .joinToString { "\n  ${it}: ${requestWrapper.getHeader(it)}" }

        private fun getParameter(requestWrapper: ContentCachingRequestWrapper) =
            requestWrapper.parameterNames.asSequence()
                .joinToString { "\n  ${it}: ${requestWrapper.getParameter(it)}" }

        private fun convertByteArrayToString(content: ByteArray) =
            if (content.isNotEmpty()) setPretty(String(content, StandardCharsets.UTF_8)) else ""

        private fun setPretty(body: String) =
            runCatching {
                val objectMapper = ObjectMapper()
                val objectWriter = objectMapper.writerWithDefaultPrettyPrinter()
                val json = objectMapper.readValue(body, Any::class.java)
                objectWriter.writeValueAsString(json)
            }.getOrDefault(body)
    }

    fun log() {
        val message = """
            |>> REQUEST: $httpMethod $requestUri ($elapsedTime)
            |>> TRANSACTION_ID: $transactionId
            |>> CLIENT_IP: $remoteAddr
            |>> REQUEST_HEADER: $requestHeader
            |>> REQUEST_PARAMETER: $requestParam
            |>> REQUEST_BODY: $requestBody
            |>> RESPONSE_BODY: $responseBody
        """.trimMargin()
            .let { "\n$it" }

        log.info(message)
    }
}
