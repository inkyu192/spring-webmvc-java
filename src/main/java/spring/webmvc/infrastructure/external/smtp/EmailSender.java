package spring.webmvc.infrastructure.external.smtp;

import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailSender {

	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	public void send(
		String to,
		String subject,
		String templatePath,
		Map<String, Object> variables
	) {
		Context context = new Context();
		context.setVariables(variables);

		String htmlContent = templateEngine.process(templatePath, context);

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);
		} catch (MessagingException e) {
			throw new IllegalStateException(e);
		}

		mailSender.send(mimeMessage);
	}
}
