package spring.webmvc.application.dto.command;

public record VerifyEmailCommand(
	String email,
	String verifyUrl
) {
}
