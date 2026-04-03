package spring.webmvc.domain.repository;

import java.util.List;
import java.util.Optional;

import spring.webmvc.domain.model.entity.UserDevice;

public interface UserDeviceRepository {
	Optional<UserDevice> findByUserIdAndDeviceId(Long userId, String deviceId);

	List<UserDevice> findAllByUserId(Long userId);

	long countByUserId(Long userId);

	UserDevice save(UserDevice userDevice);

	List<UserDevice> findAllByTokenNotNull();

	void delete(UserDevice userDevice);
}
