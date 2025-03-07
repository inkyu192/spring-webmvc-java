package spring.web.java.presentation.controller;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import spring.web.java.application.service.ItemService;
import spring.web.java.infrastructure.config.security.JwtTokenProvider;
import spring.web.java.infrastructure.util.ResponseWriter;
import spring.web.java.presentation.dto.response.ItemResponse;

@WebMvcTest(ItemController.class)
@ExtendWith(RestDocumentationExtension.class)
class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemService;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@MockBean
	private ResponseWriter responseWriter;

	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation, WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	void findItems() throws Exception {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		String name = "item";

		List<ItemResponse> itemResponse = List.of(
			new ItemResponse(1L, "item1", "description", 1000, 10),
			new ItemResponse(2L, "item2", "description", 2000, 20),
			new ItemResponse(3L, "item3", "description", 3000, 30)
		);
		Page<ItemResponse> page = new PageImpl<>(itemResponse, pageable, itemResponse.size());

		Mockito.when(itemService.findItems(pageable, name)).thenReturn(page);

		// When & Then
		mockMvc.perform(RestDocumentationRequestBuilders.get("/items")
				.param("page", "0")
				.param("size", "10")
				.param("name", name))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("items",
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("page").description("페이지 번호").optional(),
						RequestDocumentation.parameterWithName("size").description("페이지 크기").optional(),
						RequestDocumentation.parameterWithName("name").description("아이템 이름").optional()
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("content[].id").description("아이템 ID"),
						PayloadDocumentation.fieldWithPath("content[].name").description("아이템 이름"),
						PayloadDocumentation.fieldWithPath("content[].description").description("아이템 설명"),
						PayloadDocumentation.fieldWithPath("content[].price").description("아이템 가격"),
						PayloadDocumentation.fieldWithPath("content[].quantity").description("아이템 수량"),

						PayloadDocumentation.fieldWithPath("totalPages").description("전체 페이지 수"),
						PayloadDocumentation.fieldWithPath("totalElements").description("전체 아이템 수"),
						PayloadDocumentation.fieldWithPath("size").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("number").description("현재 페이지 번호"),
						PayloadDocumentation.fieldWithPath("sort").description("정렬 정보"),
						PayloadDocumentation.fieldWithPath("numberOfElements").description("현재 페이지의 아이템 수"),
						PayloadDocumentation.fieldWithPath("first").description("첫 페이지 여부"),
						PayloadDocumentation.fieldWithPath("last").description("마지막 페이지 여부"),
						PayloadDocumentation.fieldWithPath("empty").description("빈 페이지 여부"),
						PayloadDocumentation.fieldWithPath("pageable").description("페이지 정보"),
						PayloadDocumentation.fieldWithPath("pageable.pageNumber").description("현재 페이지 번호"),
						PayloadDocumentation.fieldWithPath("pageable.pageSize").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("pageable.sort").description("정렬 정보"),

						PayloadDocumentation.fieldWithPath("pageable.offset").description("정렬 정보"),
						PayloadDocumentation.fieldWithPath("pageable.paged").description("정렬 정보"),
						PayloadDocumentation.fieldWithPath("pageable.unpaged").description("정렬 정보"),

						PayloadDocumentation.fieldWithPath("sort").description("정렬 정보"),
						PayloadDocumentation.fieldWithPath("sort.empty").description("정렬이 비어있는지 여부"),
						PayloadDocumentation.fieldWithPath("sort.unsorted").description("정렬되지 않았는지 여부"),
						PayloadDocumentation.fieldWithPath("sort.sorted").description("정렬되었는지 여부"),
						PayloadDocumentation.fieldWithPath("pageable.sort").description("정렬 정보"),
						PayloadDocumentation.fieldWithPath("pageable.sort.empty").description("정렬이 비어있는지 여부"),
						PayloadDocumentation.fieldWithPath("pageable.sort.unsorted").description("정렬되지 않았는지 여부"),
						PayloadDocumentation.fieldWithPath("pageable.sort.sorted").description("정렬되었는지 여부")
					)
				)
			);
	}
}
