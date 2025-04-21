package spring.webmvc.infrastructure.common;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UriFactory {

	private final HttpServletRequest request;

	public URI createApiDocUri(int statusCode) {
		HttpStatus status = HttpStatus.resolve(statusCode);

		if (status == null) {
			return URI.create("about:blank");
		}

		return createApiDocUri(status);
	}

	public URI createApiDocUri(HttpStatus status) {
		String baseUri = getBaseUri();
		String typeUri = String.format("%s/docs/index.html#%s", baseUri, status.name());
		return URI.create(typeUri);
	}

	private String getBaseUri() {
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
