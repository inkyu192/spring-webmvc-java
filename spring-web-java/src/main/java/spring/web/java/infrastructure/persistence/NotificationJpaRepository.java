package spring.web.java.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.web.java.domain.model.entity.Notification;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
