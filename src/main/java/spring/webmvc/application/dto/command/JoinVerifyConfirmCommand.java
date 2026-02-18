package spring.webmvc.application.dto.command;

public record JoinVerifyConfirmCommand(
	String token
) {
	public static JoinVerifyConfirmCommand of(String token) {
		return new JoinVerifyConfirmCommand(token);
	}
}
