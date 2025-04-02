package spring.webmvc.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Notification;
import spring.webmvc.domain.repository.NotificationRepository;
import spring.webmvc.infrastructure.persistence.NotificationJpaRepository;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

	private final NotificationJpaRepository jpaRepository;

	@Override
	public Notification save(Notification notification) {
		return jpaRepository.save(notification);
	}
}
