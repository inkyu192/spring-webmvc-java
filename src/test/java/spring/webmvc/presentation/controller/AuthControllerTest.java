package spring.webmvc.presentation.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.webmvc.application.dto.command.SignInCommand;
import spring.webmvc.application.dto.command.SignUpCommand;
import spring.webmvc.application.dto.result.TokenResult;
import spring.webmvc.application.service.AuthService;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.infrastructure.config.ControllerTest;
import spring.webmvc.infrastructure.properties.AppProperties;

@ControllerTest(AuthController.class)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AuthService authService;

	@MockitoBean
	private AppProperties appProperties;

	private final String accessToken = "accessToken";
	private final String refreshToken = "refreshToken";

	@Test
	void signUp() throws Exception {
		User mockUser = mock(User.class);
		when(mockUser.getId()).thenReturn(1L);
		when(mockUser.getProfileImage()).thenReturn(null);

		when(authService.signUp(any(SignUpCommand.class))).thenReturn(mockUser);

		AppProperties.AwsProperties aws = mock(AppProperties.AwsProperties.class);
		AppProperties.AwsProperties.CloudFrontProperties cloudfront = mock(
			AppProperties.AwsProperties.CloudFrontProperties.class);
		when(appProperties.aws()).thenReturn(aws);
		when(aws.cloudfront()).thenReturn(cloudfront);
		when(cloudfront.domain()).thenReturn("https://cdn.example.com");

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/sign-up")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "name": "홍길동",
						  "phone": "010-1234-5678",
						  "gender": "MALE",
						  "birthday": "1990-01-01",
						  "email": "test@example.com",
						  "password": "password123"
						}
						""")
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document(
					"sign-up",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("name").description("이름"),
						PayloadDocumentation.fieldWithPath("phone").description("전화번호"),
						PayloadDocumentation.fieldWithPath("gender").description("성별"),
						PayloadDocumentation.fieldWithPath("birthday").description("생년월일"),
						PayloadDocumentation.fieldWithPath("email").description("이메일"),
						PayloadDocumentation.fieldWithPath("password").description("비밀번호")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("사용자 ID"),
						PayloadDocumentation.fieldWithPath("profileImage").description("프로필 이미지 URL").optional()
					)
				)
			);
	}

	@Test
	void signIn() throws Exception {
		TokenResult tokenResult = new TokenResult(accessToken, refreshToken);

		when(authService.signIn(any(SignInCommand.class))).thenReturn(tokenResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/sign-in")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "email": "test@example.com",
						  "password": "password123"
						}
						""")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"sign-in",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("email").description("이메일"),
						PayloadDocumentation.fieldWithPath("password").description("비밀번호")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("accessToken").description("액세스 토큰"),
						PayloadDocumentation.fieldWithPath("refreshToken").description("리프레시 토큰")
					)
				)
			);
	}

	@Test
	void refreshToken() throws Exception {
		TokenResult tokenResult = new TokenResult("newAccessToken", "newRefreshToken");

		when(authService.refreshToken(any())).thenReturn(tokenResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/token/refresh")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "accessToken": "accessToken",
						  "refreshToken": "refreshToken"
						}
						""")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"token-refresh",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("accessToken").description("액세스 토큰"),
						PayloadDocumentation.fieldWithPath("refreshToken").description("리프레시 토큰")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("accessToken").description("새 액세스 토큰"),
						PayloadDocumentation.fieldWithPath("refreshToken").description("새 리프레시 토큰")
					)
				)
			);
	}

	@Test
	void requestJoinVerify() throws Exception {
		doNothing().when(authService).requestJoinVerify(any());

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/join/verify/request")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "email": "test@example.com"
						}
						""")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document(
					"join-verify-request",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("email").description("이메일")
					)
				)
			);
	}

	@Test
	void confirmJoinVerify() throws Exception {
		doNothing().when(authService).confirmJoinVerify(any());

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/join/verify/confirm")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "token": "verifyToken123"
						}
						""")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document(
					"join-verify-confirm",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("token").description("인증 토큰")
					)
				)
			);
	}

	@Test
	void requestPasswordReset() throws Exception {
		doNothing().when(authService).requestPasswordReset(any());

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/password/reset/request")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "email": "test@example.com"
						}
						""")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document(
					"password-reset-request",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("email").description("이메일")
					)
				)
			);
	}

	@Test
	void confirmPasswordReset() throws Exception {
		doNothing().when(authService).confirmPasswordReset(any());

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/password/reset/confirm")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "token": "resetToken123",
						  "newPassword": "newPassword123"
						}
						""")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document(
					"password-reset-confirm",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("token").description("리셋 토큰"),
						PayloadDocumentation.fieldWithPath("newPassword").description("새 비밀번호")
					)
				)
			);
	}
}
