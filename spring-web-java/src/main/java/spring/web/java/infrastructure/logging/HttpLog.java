package spring.web.java.infrastructure.logging;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import org.springframework.http.HttpMethod;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record HttpLog(
	String transactionId,
	HttpMethod httpMethod,
	String requestUri,
	String remoteAddr,
	Double elapsedTime,
	String requestHeader,
	String requestParam,
	String requestBody,
	String responseBody
) {
	public HttpLog(
		String transactionId,
		ContentCachingRequestWrapper requestWrapper,
		ContentCachingResponseWrapper responseWrapper,
		Double elapsedTime
	) {
		this(
			transactionId,
			HttpMethod.valueOf(requestWrapper.getMethod()),
			requestWrapper.getRequestURI(),
			requestWrapper.getRemoteAddr(),
			elapsedTime,
			getHeader(requestWrapper),
			getParameter(requestWrapper),
			convertByteArrayToString(requestWrapper.getContentAsByteArray()),
			convertByteArrayToString(responseWrapper.getContentAsByteArray())
		);
	}

	private static String getHeader(ContentCachingRequestWrapper requestWrapper) {
		StringBuilder builder = new StringBuilder();
		Enumeration<String> headerNames = requestWrapper.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = requestWrapper.getHeader(name);
			builder.append("\n").append("  ").append(name).append(": ").append(value);
		}

		return builder.toString();
	}

	private static String getParameter(ContentCachingRequestWrapper requestWrapper) {
		StringBuilder builder = new StringBuilder();
		Enumeration<String> parameterNames = requestWrapper.getParameterNames();

		while (parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
			String value = requestWrapper.getParameter(name);
			builder.append("\n").append("  ").append(name).append(": ").append(value);
		}

		return builder.toString();
	}

	@SneakyThrows
	private static String convertByteArrayToString(byte[] content) {
		String body = "";
		if (content.length > 0) {
			body = new String(content, StandardCharsets.UTF_8);
		}

		return setPretty(body);
	}

	@SneakyThrows
	private static String setPretty(String body) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
			Object json = objectMapper.readValue(body, Object.class);
			return objectWriter.writeValueAsString(json);
		} catch (Exception e) {
			return body;
		}
	}

	public void log() {
		String message = """
			\n|>> REQUEST: %s %s (%s)
			|>> TRANSACTION_ID: %s
			|>> CLIENT_IP: %s
			|>> REQUEST_HEADER: %s
			|>> REQUEST_PARAMETER: %s
			|>> REQUEST_BODY: %s
			|>> RESPONSE_BODY: %s
			"""
			.formatted(
				httpMethod,
				requestUri,
				elapsedTime,
				transactionId,
				remoteAddr,
				requestHeader,
				requestParam,
				requestBody,
				responseBody
			);

		log.info(message);
	}
}
