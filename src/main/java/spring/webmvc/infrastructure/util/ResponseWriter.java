package spring.webmvc.infrastructure.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResponseWriter {

	private final ObjectMapper objectMapper;
	private final HttpServletResponse response;

	public void writeResponse(ProblemDetail problemDetail) throws IOException {
		response.setStatus(problemDetail.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
	}
}
