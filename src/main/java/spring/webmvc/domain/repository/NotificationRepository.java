package spring.webmvc.domain.repository;

import spring.webmvc.domain.model.entity.Notification;

public interface NotificationRepository {
	Notification save(Notification notification);
}
