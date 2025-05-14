package spring.webmvc.infrastructure.logging;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpLog {

	private final ObjectMapper objectMapper;

	public void write(
		ContentCachingRequestWrapper requestWrapper,
		ContentCachingResponseWrapper responseWrapper,
		long startTime,
		long endTime
	) {
		String message = """
			\n|>> REQUEST: %s %s (%.3f s)
			|>> CLIENT_IP: %s
			|>> REQUEST_HEADER: %s
			|>> REQUEST_PARAMETER: %s
			|>> REQUEST_BODY: %s
			|>> RESPONSE_BODY: %s
			""".formatted(
			requestWrapper.getMethod(),
			requestWrapper.getRequestURI(),
			(endTime - startTime) / 1000.0,
			requestWrapper.getRemoteAddr(),
			extractHeaders(requestWrapper),
			extractParameters(requestWrapper),
			readBody(requestWrapper.getContentAsByteArray()),
			readBody(responseWrapper.getContentAsByteArray())
		);

		log.info(message);
	}

	private String extractHeaders(ContentCachingRequestWrapper request) {
		StringBuilder builder = new StringBuilder();
		Enumeration<String> names = request.getHeaderNames();

		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getHeader(name);
			builder.append("\n  ").append(name).append(": ").append(value);
		}
		return builder.toString();
	}

	private String extractParameters(ContentCachingRequestWrapper request) {
		StringBuilder builder = new StringBuilder();
		Enumeration<String> names = request.getParameterNames();

		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getParameter(name);
			builder.append("\n  ").append(name).append(": ").append(value);
		}
		return builder.toString();
	}

	private String readBody(byte[] content) {
		if (content == null || content.length == 0)
			return "";

		String body = new String(content, StandardCharsets.UTF_8);
		try {
			Object json = objectMapper.readValue(body, Object.class);
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		} catch (Exception e) {
			return body;
		}
	}
}
