package spring.web.java.global.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtExceptionFilter extends GenericFilterBean {

	private final ObjectMapper objectMapper;

	@Override
	public void doFilter(
		ServletRequest request, ServletResponse response, FilterChain chain
	) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (UnsupportedJwtException e) {
			ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
			problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
			problemDetail.setDetail(e.getMessage());

			writeResponse(response, problemDetail);
		} catch (JwtException e) {
			ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
			problemDetail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
			problemDetail.setDetail(e.getMessage());

			writeResponse(response, problemDetail);
		}
	}

	private void writeResponse(ServletResponse response, ProblemDetail problemDetail) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
		response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
	}
}
