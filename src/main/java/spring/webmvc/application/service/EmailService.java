package spring.webmvc.application.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.PasswordResetEmailCommand;
import spring.webmvc.application.dto.command.VerifyEmailCommand;
import spring.webmvc.application.enums.EmailTemplate;
import spring.webmvc.infrastructure.external.sqs.SqsProducer;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final SqsProducer sqsProducer;

	@Value("${spring.cloud.aws.sqs.queues.email}")
	private String queueName;

	public void sendVerifyEmail(VerifyEmailCommand command) {
		sqsProducer.send(
			queueName,
			command,
			Map.of("emailTemplate", EmailTemplate.JOIN_VERIFY.name())
		);
	}

	public void sendPasswordResetEmail(PasswordResetEmailCommand command) {
		sqsProducer.send(
			queueName,
			command,
			Map.of("emailTemplate", EmailTemplate.PASSWORD_RESET.name())
		);
	}
}
