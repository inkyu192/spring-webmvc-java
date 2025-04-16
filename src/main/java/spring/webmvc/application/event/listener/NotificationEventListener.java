package spring.webmvc.application.event.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.event.NotificationEvent;
import spring.webmvc.application.service.NotificationService;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

	private final NotificationService notificationService;

	@Async
	@TransactionalEventListener
	public void handleNotificationEvent(NotificationEvent notificationEvent) {
		notificationService.saveNotification(notificationEvent);
	}
}
