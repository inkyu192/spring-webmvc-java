package spring.webmvc.presentation.controller.partner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.webmvc.application.dto.query.UserQuery;
import spring.webmvc.application.dto.result.UserCredentialResult;
import spring.webmvc.application.service.UserService;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.entity.UserCredential;
import spring.webmvc.domain.model.enums.Gender;
import spring.webmvc.domain.model.vo.Email;
import spring.webmvc.domain.model.vo.Phone;
import spring.webmvc.infrastructure.config.ControllerTest;

@ControllerTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	private User user;
	private UserCredential userCredential;
	private final Long userId = 1L;

	@BeforeEach
	void setUp() {
		user = spy(User.create(
			"홍길동",
			Phone.create("010-1234-5678"),
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			null
		));
		doReturn(userId).when(user).getId();

		userCredential = spy(UserCredential.create(
			user,
			Email.create("test@example.com"),
			"encodedPassword"
		));
		userCredential.verify();
	}

	@Test
	void findUsers() throws Exception {
		Page<User> page = new PageImpl<>(List.of(user), PageRequest.of(0, 20), 1);
		Instant createdFrom = Instant.parse("2025-01-01T00:00:00Z");
		Instant createdTo = Instant.parse("2025-12-31T23:59:59Z");

		when(userService.findUsers(any(UserQuery.class))).thenReturn(page);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/partner/users")
					.header("Authorization", "Bearer access-token")
					.queryParam("page", "0")
					.queryParam("size", "20")
					.queryParam("createdFrom", createdFrom.toString())
					.queryParam("createdTo", createdTo.toString())
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-user-list",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("page").description("페이지 번호").optional(),
						RequestDocumentation.parameterWithName("size").description("페이지 크기").optional(),
						RequestDocumentation.parameterWithName("createdFrom").description("생성일 시작"),
						RequestDocumentation.parameterWithName("createdTo").description("생성일 종료")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("page").description("현재 페이지 번호"),
						PayloadDocumentation.fieldWithPath("size").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("totalElements").description("전체 요소 수"),
						PayloadDocumentation.fieldWithPath("totalPages").description("전체 페이지 수"),
						PayloadDocumentation.fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("hasPrevious").description("이전 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("content[].id").description("사용자 ID"),
						PayloadDocumentation.fieldWithPath("content[].name").description("이름"),
						PayloadDocumentation.fieldWithPath("content[].phone").description("전화번호"),
						PayloadDocumentation.fieldWithPath("content[].gender").description("성별"),
						PayloadDocumentation.fieldWithPath("content[].birthDate").description("생년월일").optional(),
						PayloadDocumentation.fieldWithPath("content[].createdAt").description("생성일시").optional()
					)
				)
			);
	}

	@Test
	void findUser() throws Exception {
		UserCredentialResult credentialResult = new UserCredentialResult(
			user,
			userCredential,
			List.of()
		);

		when(userService.findUserDetail(userId)).thenReturn(credentialResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/partner/users/{id}", userId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-user-detail",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("사용자 ID")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("사용자 ID"),
						PayloadDocumentation.fieldWithPath("name").description("이름"),
						PayloadDocumentation.fieldWithPath("phone").description("전화번호"),
						PayloadDocumentation.fieldWithPath("gender").description("성별"),
						PayloadDocumentation.fieldWithPath("birthday").description("생년월일"),
						PayloadDocumentation.fieldWithPath("credential").description("인증 정보"),
						PayloadDocumentation.fieldWithPath("credential.email").description("이메일"),
						PayloadDocumentation.fieldWithPath("credential.verifiedAt").description("인증 일시").optional(),
						PayloadDocumentation.fieldWithPath("oAuths").description("OAuth 정보"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시").optional()
					)
				)
			);
	}
}
