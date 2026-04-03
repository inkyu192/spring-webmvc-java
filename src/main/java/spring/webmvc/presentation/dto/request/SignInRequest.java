package spring.webmvc.presentation.dto.request;

import spring.webmvc.application.dto.command.SignInCommand;
import spring.webmvc.domain.model.enums.DeviceType;
import spring.webmvc.domain.model.vo.Email;

public record SignInRequest(
	@jakarta.validation.constraints.Email
	String email,
	String password,
	String deviceId,
	String deviceName,
	DeviceType deviceType,
	String token
) {
	public SignInCommand toCommand() {
		return new SignInCommand(Email.create(email), password, deviceId, deviceName, deviceType, token);
	}
}
