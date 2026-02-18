package spring.webmvc.application.strategy.email;

import spring.webmvc.application.enums.EmailTemplate;

public interface EmailStrategy {

	EmailTemplate emailTemplate();

	void handle(String payload);
}

