package spring.webmvc.application.dto.command;

import spring.webmvc.domain.model.enums.DeviceType;
import spring.webmvc.domain.model.vo.Email;

public record SignInCommand(
	Email email,
	String password,
	String deviceId,
	String deviceName,
	DeviceType deviceType,
	String token
) {
}
