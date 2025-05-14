package spring.webmvc.infrastructure.logging;

import java.io.IOException;
import java.util.List;

import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpLogFilter extends OncePerRequestFilter {

	private final HttpLog httpLog;
	private final List<PathPattern> excludedPatterns;

	public HttpLogFilter(HttpLog httpLog, PathPatternParser pathPatternParser) {
		this.httpLog = httpLog;
		this.excludedPatterns = List.of(
			pathPatternParser.parse("/actuator/**"),
			pathPatternParser.parse("/favicon.ico"),
			pathPatternParser.parse("/static/**"),
			pathPatternParser.parse("/public/**"),
			pathPatternParser.parse("/docs/index.html")
		);
	}

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

		long startTime = System.currentTimeMillis();
		filterChain.doFilter(requestWrapper, responseWrapper);
		long endTime = System.currentTimeMillis();

		httpLog.write(requestWrapper, responseWrapper, startTime, endTime);

		responseWrapper.copyBodyToResponse();
	}

	private boolean isLogExclude(HttpServletRequest request) {
		String uri = request.getRequestURI();
		PathContainer pathContainer = PathContainer.parsePath(uri);
		return excludedPatterns.stream().anyMatch(pattern -> pattern.matches(pathContainer));
	}
}
