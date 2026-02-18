package spring.webmvc.application.event.listener;

import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.event.SendPasswordResetEmailEvent;
import spring.webmvc.application.event.SendVerifyEmailEvent;
import spring.webmvc.domain.repository.cache.AuthCacheRepository;
import spring.webmvc.infrastructure.external.notification.NotificationClient;
import spring.webmvc.infrastructure.external.notification.PasswordResetEmailRequest;
import spring.webmvc.infrastructure.external.notification.VerifyEmailRequest;
import spring.webmvc.infrastructure.properties.AppProperties;

@Component
@RequiredArgsConstructor
public class SendEmailEventListener {

	private final AppProperties appProperties;
	private final NotificationClient notificationClient;
	private final AuthCacheRepository authCacheRepository;

	@Async
	@TransactionalEventListener
	public void sendVerifyEmail(SendVerifyEmailEvent event) {
		String token = UUID.randomUUID().toString();
		String verifyLink = "%s/auth/join/verify?token=%s".formatted(appProperties.baseUrl(), token);

		authCacheRepository.setJoinVerifyToken(token, event.email());

		VerifyEmailRequest request = new VerifyEmailRequest(event.email().getValue(), verifyLink);

		notificationClient.sendVerifyEmail(request);
	}

	@Async
	@TransactionalEventListener
	public void handleSendPasswordResetEmailEvent(SendPasswordResetEmailEvent event) {
		String token = UUID.randomUUID().toString();
		String resetLink = "%s/auth/password/reset?token=%s".formatted(appProperties.baseUrl(), token);

		authCacheRepository.setPasswordResetToken(token, event.email());

		PasswordResetEmailRequest request = new PasswordResetEmailRequest(event.email().getValue(), resetLink);

		notificationClient.sendPasswordResetEmail(request);
	}
}
