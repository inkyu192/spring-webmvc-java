package spring.webmvc.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.UserDevice;

public interface UserDeviceJpaRepository extends JpaRepository<UserDevice, Long> {
	Optional<UserDevice> findByUserIdAndDeviceId(Long userId, String deviceId);

	List<UserDevice> findAllByUserId(Long userId);

	long countByUserId(Long userId);

	List<UserDevice> findAllByTokenNotNull();
}
