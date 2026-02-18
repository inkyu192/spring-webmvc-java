package spring.webmvc.application.dto.command;

public record PasswordResetConfirmCommand(
	String token,
	String newPassword
) {
}
