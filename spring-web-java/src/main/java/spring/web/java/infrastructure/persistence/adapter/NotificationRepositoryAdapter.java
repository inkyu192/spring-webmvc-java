package spring.web.java.infrastructure.persistence.adapter;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Notification;
import spring.web.java.domain.repository.NotificationRepository;
import spring.web.java.infrastructure.persistence.NotificationJpaRepository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

	private final NotificationJpaRepository jpaRepository;

	@Override
	public Notification save(Notification notification) {
		return jpaRepository.save(notification);
	}
}
