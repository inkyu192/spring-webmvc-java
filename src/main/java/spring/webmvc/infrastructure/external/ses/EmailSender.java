package spring.webmvc.infrastructure.external.ses;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SesException;
import spring.webmvc.infrastructure.exception.FailedAwsException;
import spring.webmvc.infrastructure.properties.AppProperties;

@Slf4j
@Component
public class EmailSender {

	private final SesClient sesClient;
	private final TemplateEngine templateEngine;
	private final String senderEmail;

	public EmailSender(
		SesClient sesClient,
		TemplateEngine templateEngine,
		AppProperties appProperties
	) {
		this.sesClient = sesClient;
		this.templateEngine = templateEngine;
		this.senderEmail = appProperties.aws().ses().senderEmail();
	}

	public void send(
		String to,
		String subject,
		String templatePath,
		Map<String, Object> variables
	) {
		Context context = new Context();
		context.setVariables(variables);

		String htmlContent = templateEngine.process(templatePath, context);

		SendEmailRequest request = SendEmailRequest.builder()
			.source(senderEmail)
			.destination(Destination.builder().toAddresses(to).build())
			.message(
				Message.builder()
					.subject(
						Content.builder()
							.charset(StandardCharsets.UTF_8.name())
							.data(subject)
							.build()
					)
					.body(
						Body.builder()
							.html(
								Content.builder()
									.charset(StandardCharsets.UTF_8.name())
									.data(htmlContent)
									.build()
							)
							.build()
					)
					.build()
			)
			.build();

		try {
			SendEmailResponse response = sesClient.sendEmail(request);
			log.info(
				"Email sent successfully: to={}, subject={}, messageId={}, variables={}",
				to, subject, response.messageId(), variables
			);
		} catch (SesException e) {
			log.error(
				"Email sending failed: to={}, subject={}, error={}, variables={}",
				to, subject, e.awsErrorDetails().errorMessage(), variables
			);
			throw new FailedAwsException(e.awsErrorDetails().serviceName(), e);
		}
	}
}
