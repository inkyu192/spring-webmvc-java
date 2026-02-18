package spring.webmvc.application.event;

import spring.webmvc.domain.model.vo.Email;

public record SendVerifyEmailEvent(
	Email email
) {
}
