package spring.webmvc.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.document.Notification;
import spring.webmvc.domain.repository.NotificationRepository;
import spring.webmvc.infrastructure.persistence.mongo.NotificationMongoRepository;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

	private final NotificationMongoRepository mongoRepository;

	@Override
	public Notification save(Notification notification) {
		return mongoRepository.save(notification);
	}
}
