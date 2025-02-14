package spring.web.java.infrastructure.logging;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.server.PathContainer;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpLogFilter extends OncePerRequestFilter {

	public static final String TRANSACTION_ID = "transactionId";

	private final PathPatternParser pathPatternParser = new PathPatternParser();
	private final List<PathPattern> logExcludeList = List.of(
		pathPatternParser.parse("/actuator/**"),
		pathPatternParser.parse("/favicon.ico"),
		pathPatternParser.parse("/static/**"),
		pathPatternParser.parse("/public/**")
	);

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		if (isLogExclude(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

		String transactionId = UUID.randomUUID().toString();
		request.setAttribute(TRANSACTION_ID, transactionId);

		long startTime = System.currentTimeMillis();
		filterChain.doFilter(requestWrapper, responseWrapper);
		long endTime = System.currentTimeMillis();

		HttpLog httpLog = new HttpLog(transactionId, requestWrapper, responseWrapper, (endTime - startTime) / 1000.0);
		httpLog.log();

		responseWrapper.copyBodyToResponse();
	}

	private boolean isLogExclude(HttpServletRequest request) {
		String uri = request.getRequestURI();
		PathContainer pathContainer = PathContainer.parsePath(uri);
		return logExcludeList.stream().anyMatch(pattern -> pattern.matches(pathContainer));
	}
}
