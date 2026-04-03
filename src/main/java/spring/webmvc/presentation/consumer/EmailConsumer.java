package spring.webmvc.presentation.consumer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.strategy.email.EmailStrategy;
import spring.webmvc.application.strategy.email.EmailTemplate;

@Component
@Slf4j
public class EmailConsumer {
	private final Map<EmailTemplate, EmailStrategy> emailStrategyMap;

	public EmailConsumer(List<EmailStrategy> emailStrategies) {
		Set<EmailTemplate> duplicates = emailStrategies.stream()
			.collect(Collectors.groupingBy(EmailStrategy::emailTemplate))
			.entrySet()
			.stream()
			.filter(entry -> entry.getValue().size() > 1)
			.map(Map.Entry::getKey)
			.collect(Collectors.toSet());

		if (!duplicates.isEmpty()) {
			throw new IllegalStateException("중복된 EmailStrategy가 존재합니다: %s".formatted(duplicates));
		}

		this.emailStrategyMap = emailStrategies.stream()
			.collect(Collectors.toMap(EmailStrategy::emailTemplate, Function.identity()));
	}

	@SqsListener("${spring.cloud.aws.sqs.queues.email}")
	public void receive(
		@Header(value = "emailTemplate", required = false) String emailTemplate,
		@Payload String payload
	) {
		try {
			if (emailTemplate == null) {
				throw new IllegalArgumentException("emailTemplate header is null");
			}

			EmailTemplate template = EmailTemplate.valueOf(emailTemplate);
			EmailStrategy strategy = emailStrategyMap.get(template);
			if (strategy == null) {
				throw new IllegalStateException("구현되지 않은 이메일 템플릿: %s".formatted(template));
			}

			strategy.handle(payload);

		} catch (Exception e) {
			log.error("이메일 처리 실패: template={}, payload={}", emailTemplate, payload, e);
		}
	}
}
