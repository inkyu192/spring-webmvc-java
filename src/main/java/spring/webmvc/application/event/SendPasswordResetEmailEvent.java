package spring.webmvc.application.event;

import spring.webmvc.domain.model.vo.Email;

public record SendPasswordResetEmailEvent(
	Email email
) {
}
