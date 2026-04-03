package spring.webmvc.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.UserDevice;
import spring.webmvc.domain.repository.UserDeviceRepository;
import spring.webmvc.domain.repository.cache.TokenCacheRepository;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeviceService {

	private final UserDeviceRepository userDeviceRepository;
	private final TokenCacheRepository tokenCacheRepository;

	public List<UserDevice> getMyDevices(Long userId) {
		return userDeviceRepository.findAllByUserId(userId);
	}

	@Transactional
	public void deleteDevice(Long userId, String deviceId) {
		UserDevice userDevice = userDeviceRepository.findByUserIdAndDeviceId(userId, deviceId)
			.orElseThrow(() -> new NotFoundEntityException(UserDevice.class, deviceId));

		userDeviceRepository.delete(userDevice);
		tokenCacheRepository.removeRefreshToken(userId, deviceId);
	}
}
