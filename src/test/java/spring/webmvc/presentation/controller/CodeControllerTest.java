package spring.webmvc.presentation.controller;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.webmvc.application.dto.result.CodeGroupResult;
import spring.webmvc.application.dto.result.CodeResult;
import spring.webmvc.application.service.CodeService;
import spring.webmvc.infrastructure.config.ControllerTest;

@ControllerTest(CodeController.class)
class CodeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CodeService codeService;

	private List<CodeGroupResult> codeGroupResults;

	@BeforeEach
	void setUp() {
		codeGroupResults = List.of(
			new CodeGroupResult(
				"Gender",
				"성별",
				List.of(
					new CodeResult("MALE", "남성"),
					new CodeResult("FEMALE", "여성")
				)
			),
			new CodeGroupResult(
				"OrderStatus",
				"주문 상태",
				List.of(
					new CodeResult("ORDER", "주문"),
					new CodeResult("CONFIRM", "확정"),
					new CodeResult("CANCEL", "취소")
				)
			)
		);
	}

	@Test
	void findCodes() throws Exception {
		when(codeService.findCodes()).thenReturn(codeGroupResults);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/codes")
					.header("Accept-Language", "ko")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"code-list",
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("size").description("코드 그룹 수"),
						PayloadDocumentation.fieldWithPath("codeGroups[].name").description("코드 그룹명"),
						PayloadDocumentation.fieldWithPath("codeGroups[].label").description("코드 그룹 라벨 (번역)"),
						PayloadDocumentation.fieldWithPath("codeGroups[].codes[].code").description("코드"),
						PayloadDocumentation.fieldWithPath("codeGroups[].codes[].label").description("코드 라벨 (번역)")
					)
				)
			);
	}
}
