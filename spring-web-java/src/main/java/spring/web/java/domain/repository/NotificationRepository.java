package spring.web.java.domain.repository;

import spring.web.java.domain.model.entity.Notification;

public interface NotificationRepository {
	Notification save(Notification notification);
}
