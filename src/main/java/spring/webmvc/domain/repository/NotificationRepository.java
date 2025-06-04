package spring.webmvc.domain.repository;

import spring.webmvc.domain.model.document.Notification;

public interface NotificationRepository {
	Notification save(Notification notification);
}
