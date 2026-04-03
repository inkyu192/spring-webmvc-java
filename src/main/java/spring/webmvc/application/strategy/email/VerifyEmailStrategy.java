package spring.webmvc.application.strategy.email;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.VerifyEmailCommand;
import spring.webmvc.infrastructure.external.ses.EmailSender;

@Component
@RequiredArgsConstructor
public class VerifyEmailStrategy implements EmailStrategy {

	private final ObjectMapper objectMapper;
	private final EmailSender emailSender;

	@Override
	public EmailTemplate emailTemplate() {
		return EmailTemplate.JOIN_VERIFY;
	}

	@Override
	public void handle(String payload) {
		VerifyEmailCommand command;

		try {
			command = objectMapper.readValue(payload, VerifyEmailCommand.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		emailSender.send(
			command.email(),
			emailTemplate().getSubject(),
			emailTemplate().getTemplatePath(),
			Map.of("verifyLink", command.verifyUrl())
		);
	}
}
