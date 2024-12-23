package spring.web.java.infrastructure.configuration.filter;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import spring.web.java.common.ApiResponseCode;
import spring.web.java.dto.response.ApiResponse;

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
			setResponse(response, ApiResponseCode.UNSUPPORTED_TOKEN);
		} catch (ExpiredJwtException e) {
			setResponse(response, ApiResponseCode.EXPIRED_TOKEN);
		} catch (JwtException e) {
			setResponse(response, ApiResponseCode.BAD_TOKEN);
		}
	}

	private void setResponse(
		ServletResponse response, ApiResponseCode apiResponseCode
	) throws RuntimeException, IOException {
		String result = objectMapper.writeValueAsString(new ApiResponse<>(apiResponseCode));

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(result);
	}
}
