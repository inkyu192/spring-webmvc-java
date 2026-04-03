package spring.webmvc.application.strategy.email;

public interface EmailStrategy {

	EmailTemplate emailTemplate();

	void handle(String payload);
}

