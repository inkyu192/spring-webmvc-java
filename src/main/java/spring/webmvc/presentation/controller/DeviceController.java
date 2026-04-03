package spring.webmvc.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.DeviceService;
import spring.webmvc.domain.model.entity.UserDevice;
import spring.webmvc.infrastructure.security.SecurityContextUtil;
import spring.webmvc.presentation.dto.response.DeviceListResponse;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

	private final DeviceService deviceService;

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public DeviceListResponse getMyDevices() {
		Long userId = SecurityContextUtil.getUserId();
		List<UserDevice> userDevices = deviceService.getMyDevices(userId);
		return DeviceListResponse.of(userDevices);
	}

	@DeleteMapping("/{deviceId}")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteDevice(@PathVariable String deviceId) {
		Long userId = SecurityContextUtil.getUserId();
		deviceService.deleteDevice(userId, deviceId);
	}
}
