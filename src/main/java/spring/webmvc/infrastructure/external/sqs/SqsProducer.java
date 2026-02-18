package spring.webmvc.infrastructure.external.sqs;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SqsProducer {

	private final SqsTemplate sqsTemplate;
	private final ObjectMapper objectMapper;

	public <T> void send(
		String queueName,
		T payload,
		Map<String, String> headers
	) {
		String body;

		try {
			body = objectMapper.writeValueAsString(payload);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		sqsTemplate.send(options -> {
			options.queue(queueName)
				.payload(body);

			headers.forEach(options::header);
		});
	}
}
