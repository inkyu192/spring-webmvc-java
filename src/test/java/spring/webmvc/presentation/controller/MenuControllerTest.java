package spring.webmvc.presentation.controller;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.webmvc.application.dto.result.MenuResult;
import spring.webmvc.application.service.MenuService;
import spring.webmvc.infrastructure.config.ControllerTest;

@ControllerTest(MenuController.class)
class MenuControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MenuService menuService;

	private MenuResult menuResult1;
	private MenuResult menuResult2;

	@BeforeEach
	void setUp() {
		MenuResult childMenu1 = new MenuResult(
			3L,
			"상품 관리",
			"/admin/products",
			List.of()
		);

		MenuResult childMenu2 = new MenuResult(
			4L,
			"주문 관리",
			"/admin/orders",
			List.of()
		);

		menuResult1 = new MenuResult(
			1L,
			"관리자",
			null,
			List.of(childMenu1, childMenu2)
		);

		menuResult2 = new MenuResult(
			2L,
			"대시보드",
			"/dashboard",
			List.of()
		);
	}

	@Test
	void findMenus() throws Exception {
		List<MenuResult> result = List.of(menuResult1, menuResult2);

		when(menuService.findMenus()).thenReturn(result);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/menus")
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"menu-list",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("[].id").description("메뉴 ID"),
						PayloadDocumentation.fieldWithPath("[].name").description("메뉴명"),
						PayloadDocumentation.fieldWithPath("[].path").description("메뉴 경로").optional(),
						PayloadDocumentation.subsectionWithPath("[].children").description("하위 메뉴 목록")
					)
				)
			);
	}
}
