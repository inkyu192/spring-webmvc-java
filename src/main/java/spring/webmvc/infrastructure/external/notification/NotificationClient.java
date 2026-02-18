package spring.webmvc.infrastructure.external.notification;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import spring.webmvc.infrastructure.properties.AppProperties;

@Component
public class NotificationClient {

	private final RestClient restClient;

	public NotificationClient(AppProperties appProperties) {
		this.restClient = RestClient.builder()
			.baseUrl(appProperties.external().notification().host())
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}

	public void sendVerifyEmail(VerifyEmailRequest request) {
		restClient.post()
			.uri("/email/verify")
			.body(request)
			.retrieve()
			.toBodilessEntity();
	}

	public void sendPasswordResetEmail(PasswordResetEmailRequest request) {
		restClient.post()
			.uri("/email/password-reset")
			.body(request)
			.retrieve()
			.toBodilessEntity();
	}
}
