package spring.webmvc.infrastructure.support;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemDetailSupport {

	private final HttpServletRequest request;

	public URI createType(int statusCode) {
		HttpStatus status = HttpStatus.resolve(statusCode);
		return createType(status);
	}

	public URI createType(HttpStatus status) {
		String baseUrl = getBaseUrl();
		String typeUri = String.format("%s/docs/index.html#%s", baseUrl, status.name());
		return URI.create(typeUri);
	}

	private String getBaseUrl() {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();

		boolean isDefaultPort = (scheme.equals("http") && serverPort == 80)
			|| (scheme.equals("https") && serverPort == 443);

		return isDefaultPort
			? String.format("%s://%s", scheme, serverName)
			: String.format("%s://%s:%d", scheme, serverName, serverPort);
	}
}
