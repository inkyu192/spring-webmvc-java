package spring.webmvc.application.strategy.email;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.PasswordResetEmailCommand;
import spring.webmvc.application.enums.EmailTemplate;
import spring.webmvc.infrastructure.external.smtp.EmailSender;

@Component
@RequiredArgsConstructor
public class PasswordResetEmailStrategy implements EmailStrategy {

	private final ObjectMapper objectMapper;
	private final EmailSender emailSender;

	@Override
	public EmailTemplate emailTemplate() {
		return EmailTemplate.PASSWORD_RESET;
	}

	@Override
	public void handle(String payload) {
		PasswordResetEmailCommand command;

		try {
			command = objectMapper.readValue(payload, PasswordResetEmailCommand.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		emailSender.send(
			command.email(),
			emailTemplate().getSubject(),
			emailTemplate().getTemplatePath(),
			Map.of("resetLink", command.resetUrl())
		);
	}
}
