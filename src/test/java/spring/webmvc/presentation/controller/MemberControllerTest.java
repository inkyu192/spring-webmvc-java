package spring.webmvc.presentation.controller;

import java.time.LocalDate;
import java.util.List;

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
import org.springframework.restdocs.headers.HeaderDocumentation;
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

import spring.webmvc.application.service.MemberService;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;
import spring.webmvc.presentation.dto.request.MemberCreateRequest;
import spring.webmvc.presentation.dto.request.MemberUpdateRequest;

@WebMvcTest(MemberController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class MemberControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private MemberService memberService;

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
	void createMember() throws Exception {
		String account = "test@gmail.com";
		String password = "password";
		String name = "name";
		String phone = "010-1234-1234";
		LocalDate birthDate = LocalDate.now();
		List<Long> roleIds = List.of(1L);
		List<Long> permissionIds = List.of();

		MemberCreateRequest request =
			new MemberCreateRequest(account, password, name, phone, birthDate, roleIds, permissionIds);

		Member member = Mockito.mock(Member.class);

		Mockito.when(memberService.createMember(account, password, name, phone, birthDate, roleIds, permissionIds))
			.thenReturn(member);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/members")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document("member-create",
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("account").description("계정"),
						PayloadDocumentation.fieldWithPath("password").description("패스워드"),
						PayloadDocumentation.fieldWithPath("name").description("회원명"),
						PayloadDocumentation.fieldWithPath("phone").description("번호"),
						PayloadDocumentation.fieldWithPath("birthDate").description("생년월일"),
						PayloadDocumentation.fieldWithPath("roleIds").description("역할목록"),
						PayloadDocumentation.fieldWithPath("permissionIds").description("권한목록")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("account").description("계정"),
						PayloadDocumentation.fieldWithPath("name").description("회원명"),
						PayloadDocumentation.fieldWithPath("phone").description("번호"),
						PayloadDocumentation.fieldWithPath("birthDate").description("생년월일"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시")
					)
				)
			);
	}

	@Test
	void findMember() throws Exception {
		Member member = Mockito.mock(Member.class);

		Mockito.when(memberService.findMember()).thenReturn(member);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/members")
					.header("Authorization", "Bearer accessToken")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("member-get",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("account").description("계정"),
						PayloadDocumentation.fieldWithPath("name").description("회원명"),
						PayloadDocumentation.fieldWithPath("phone").description("번호"),
						PayloadDocumentation.fieldWithPath("birthDate").description("생년월일"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시")
					)
				)
			);
	}

	@Test
	void updateMember() throws Exception {
		String password = "password";
		String name = "name";
		String phone = "010-1234-1234";
		LocalDate birthDate = LocalDate.now();

		MemberUpdateRequest request = new MemberUpdateRequest(password, name, phone, birthDate);

		Member member = Mockito.mock(Member.class);

		Mockito.when(memberService.updateMember(password, name, phone, birthDate)).thenReturn(member);

		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/members")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken")
					.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("member-update",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("password").description("패스워드"),
						PayloadDocumentation.fieldWithPath("name").description("회원명"),
						PayloadDocumentation.fieldWithPath("phone").description("번호"),
						PayloadDocumentation.fieldWithPath("birthDate").description("생년월일")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("account").description("계정"),
						PayloadDocumentation.fieldWithPath("name").description("회원명"),
						PayloadDocumentation.fieldWithPath("phone").description("번호"),
						PayloadDocumentation.fieldWithPath("birthDate").description("생년월일"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시")
					)
				)
			);
	}

	@Test
	void deleteMember() throws Exception {
		Mockito.doNothing().when(memberService).deleteMember();

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/members")
					.header("Authorization", "Bearer accessToken")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document("member-delete",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					)
				)
			);
	}
}