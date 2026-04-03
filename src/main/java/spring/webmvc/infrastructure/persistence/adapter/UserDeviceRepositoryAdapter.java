package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.UserDevice;
import spring.webmvc.domain.repository.UserDeviceRepository;
import spring.webmvc.infrastructure.persistence.jpa.UserDeviceJpaRepository;

@Component
@RequiredArgsConstructor
public class UserDeviceRepositoryAdapter implements UserDeviceRepository {

	private final UserDeviceJpaRepository jpaRepository;

	@Override
	public Optional<UserDevice> findByUserIdAndDeviceId(Long userId, String deviceId) {
		return jpaRepository.findByUserIdAndDeviceId(userId, deviceId);
	}

	@Override
	public List<UserDevice> findAllByUserId(Long userId) {
		return jpaRepository.findAllByUserId(userId);
	}

	@Override
	public long countByUserId(Long userId) {
		return jpaRepository.countByUserId(userId);
	}

	@Override
	public List<UserDevice> findAllByTokenNotNull() {
		return jpaRepository.findAllByTokenNotNull();
	}

	@Override
	public UserDevice save(UserDevice userDevice) {
		return jpaRepository.save(userDevice);
	}

	@Override
	public void delete(UserDevice userDevice) {
		jpaRepository.delete(userDevice);
	}
}
