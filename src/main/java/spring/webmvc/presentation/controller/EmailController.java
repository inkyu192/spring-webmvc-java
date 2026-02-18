package spring.webmvc.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.PasswordResetEmailCommand;
import spring.webmvc.application.dto.command.VerifyEmailCommand;
import spring.webmvc.application.service.EmailService;
import spring.webmvc.presentation.dto.request.PasswordResetEmailRequest;
import spring.webmvc.presentation.dto.request.VerifyEmailRequest;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;

	@PostMapping("/verify")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void sendVerifyEmail(
		@RequestBody @Validated VerifyEmailRequest request
	) {
		VerifyEmailCommand command = request.toCommand();

		emailService.sendVerifyEmail(command);
	}

	@PostMapping("/password-reset")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void sendPasswordResetEmail(
		@RequestBody @Validated PasswordResetEmailRequest request
	) {
		PasswordResetEmailCommand command = request.toCommand();

		emailService.sendPasswordResetEmail(command);
	}
}
