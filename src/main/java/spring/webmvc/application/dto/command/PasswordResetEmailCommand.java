package spring.webmvc.application.dto.command;

public record PasswordResetEmailCommand(
	String email,
	String resetUrl
) {
}
