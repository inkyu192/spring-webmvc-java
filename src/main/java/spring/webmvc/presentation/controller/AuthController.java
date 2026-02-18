package spring.webmvc.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.aspect.RequestLock;
import spring.webmvc.application.dto.command.JoinVerifyConfirmCommand;
import spring.webmvc.application.dto.command.PasswordResetConfirmCommand;
import spring.webmvc.application.dto.command.PasswordResetRequestCommand;
import spring.webmvc.application.dto.command.RefreshTokenCommand;
import spring.webmvc.application.dto.command.SignInCommand;
import spring.webmvc.application.dto.command.SignUpCommand;
import spring.webmvc.application.dto.result.TokenResult;
import spring.webmvc.application.service.AuthService;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.infrastructure.properties.AppProperties;
import spring.webmvc.presentation.dto.request.JoinVerifyConfirmRequest;
import spring.webmvc.presentation.dto.request.JoinVerifyRequest;
import spring.webmvc.presentation.dto.request.PasswordResetConfirmRequest;
import spring.webmvc.presentation.dto.request.PasswordResetRequest;
import spring.webmvc.presentation.dto.request.SignInRequest;
import spring.webmvc.presentation.dto.request.SignUpRequest;
import spring.webmvc.presentation.dto.request.TokenRequest;
import spring.webmvc.presentation.dto.response.SignUpResponse;
import spring.webmvc.presentation.dto.response.TokenResponse;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final AppProperties appProperties;

	@PostMapping("/sign-up")
	@RequestLock
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public SignUpResponse signUp(
		@RequestBody @Validated SignUpRequest request
	) {
		SignUpCommand command = request.toCommand();
		User user = authService.signUp(command);

		return SignUpResponse.of(user, appProperties.aws().cloudfront().domain());
	}

	@PostMapping("/sign-in")
	@ResponseBody
	public TokenResponse signIn(
		@RequestBody @Validated SignInRequest request
	) {
		SignInCommand command = request.toCommand();
		TokenResult tokenResult = authService.signIn(command);

		return TokenResponse.of(tokenResult);
	}

	@PostMapping("/token/refresh")
	@ResponseBody
	public TokenResponse refreshToken(
		@RequestBody @Validated TokenRequest request
	) {
		RefreshTokenCommand command = request.toCommand();
		TokenResult tokenResult = authService.refreshToken(command);

		return TokenResponse.of(tokenResult);
	}

	@PostMapping("/join/verify/request")
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void requestJoinVerify(
		@RequestBody @Validated JoinVerifyRequest request
	) {
		authService.requestJoinVerify(request.toCommand());
	}

	@GetMapping("/join/verify")
	public String getJoinVerifyForm(
		@RequestParam String token,
		Model model
	) {
		model.addAttribute("token", token);
		return "email/join-verify-form";
	}

	@PostMapping("/join/verify/confirm")
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void confirmJoinVerify(
		@RequestBody @Validated JoinVerifyConfirmRequest request
	) {
		JoinVerifyConfirmCommand command = request.toCommand();
		authService.confirmJoinVerify(command);
	}

	@GetMapping("/join/verify/success")
	public String joinVerifySuccess() {
		return "email/join-verify-success";
	}

	@PostMapping("/password/reset/request")
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void requestPasswordReset(
		@RequestBody @Validated PasswordResetRequest request
	) {
		PasswordResetRequestCommand command = request.toCommand();
		authService.requestPasswordReset(command);
	}

	@GetMapping("/password/reset")
	public String getPasswordResetForm(
		@RequestParam String token,
		Model model
	) {
		model.addAttribute("token", token);
		return "email/password-reset-form";
	}

	@PostMapping("/password/reset/confirm")
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void confirmPasswordReset(
		@RequestBody @Validated PasswordResetConfirmRequest request
	) {
		PasswordResetConfirmCommand command = request.toCommand();
		authService.confirmPasswordReset(command);
	}

	@GetMapping("/password/reset/success")
	public String passwordResetSuccess() {
		return "email/password-reset-success";
	}
}
