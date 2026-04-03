package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.domain.model.entity.UserDevice;
import spring.webmvc.domain.model.enums.DeviceType;

public record DeviceResponse(
	String deviceId,
	String deviceName,
	DeviceType deviceType,
	Instant lastLoginAt,
	Instant createdAt
) {
	public static DeviceResponse of(UserDevice userDevice) {
		return new DeviceResponse(
			userDevice.getDeviceId(),
			userDevice.getDeviceName(),
			userDevice.getDeviceType(),
			userDevice.getLastLoginAt(),
			userDevice.getCreatedAt()
		);
	}
}
