package spring.web.java.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Notification;
import spring.web.java.domain.repository.NotificationRepository;
import spring.web.java.infrastructure.persistence.NotificationJpaRepository;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

	private final NotificationJpaRepository jpaRepository;

	@Override
	public Notification save(Notification notification) {
		return jpaRepository.save(notification);
	}
}
