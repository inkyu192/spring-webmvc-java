package spring.webmvc.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailTemplate {

	JOIN_VERIFY(
		"회원가입 인증",
		"email/join-verify"
	),
	PASSWORD_RESET(
		"비밀번호 재설정",
		"email/password-reset"
	);

	private final String subject;
	private final String templatePath;
}
