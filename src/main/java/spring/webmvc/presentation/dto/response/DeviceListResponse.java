package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.domain.model.entity.UserDevice;

public record DeviceListResponse(
	long size,
	List<DeviceResponse> devices
) {
	public static DeviceListResponse of(List<UserDevice> userDevices) {
		List<DeviceResponse> devices = userDevices.stream()
			.map(DeviceResponse::of)
			.toList();
		return new DeviceListResponse(devices.size(), devices);
	}
}
