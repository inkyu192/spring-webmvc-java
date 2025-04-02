package spring.webmvc.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Notification;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
