package spring.webmvc.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.webmvc.application.service.AuthService;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;
import spring.webmvc.presentation.dto.request.MemberLoginRequest;
import spring.webmvc.presentation.dto.request.TokenRequest;
import spring.webmvc.presentation.dto.response.TokenResponse;

@WebMvcTest(AuthController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class AuthControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

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
		MemberLoginRequest request = new MemberLoginRequest("test@gmail.com", "password");
		TokenResponse response = new TokenResponse("accessToken", "refreshToken");

		Mockito.when(authService.login(request)).thenReturn(response);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("login",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("account").description("계정"),
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
		TokenRequest request = new TokenRequest("oldAccessToken", "refreshToken");
		TokenResponse response = new TokenResponse("newAccessToken", "refreshToken");

		Mockito.when(authService.refreshToken(request)).thenReturn(response);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/auth/token/refresh")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
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