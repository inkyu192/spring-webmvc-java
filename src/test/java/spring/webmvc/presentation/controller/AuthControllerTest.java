package spring.webmvc.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import spring.webmvc.application.dto.result.TokenResult;
import spring.webmvc.application.service.AuthService;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;

@WebMvcTest(AuthController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class AuthControllerTest {

	@MockitoBean
	private AuthService authService;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation, WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
				.operationPreprocessors()
				.withRequestDefaults(Preprocessors.prettyPrint())
				.withResponseDefaults(Preprocessors.prettyPrint()))
			.build();
	}

	@Test
	void login() throws Exception {
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		String email = "test@gmail.com";
		String password = "password";

		TokenResult tokenResult = new TokenResult(accessToken, refreshToken);

		Mockito.when(authService.login(email, password)).thenReturn(tokenResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "email": "%s",
						  "password": "%s"
						}
						""".formatted(email, password))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("login",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("email").description("계정"),
						PayloadDocumentation.fieldWithPath("password").description("패스워드")
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
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";

		TokenResult tokenResult = new TokenResult(accessToken, refreshToken);

		Mockito.when(authService.refreshToken(accessToken, refreshToken)).thenReturn(tokenResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/token/refresh")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "accessToken": "%s",
						  "refreshToken": "%s"
						}
						""".formatted(accessToken, refreshToken))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("token-refresh",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("accessToken").description("액세스 토큰"),
						PayloadDocumentation.fieldWithPath("refreshToken").description("리프레시 토큰")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("accessToken").description("액세스 토큰"),
						PayloadDocumentation.fieldWithPath("refreshToken").description("리프레시 토큰")
					)
				)
			);
	}
}