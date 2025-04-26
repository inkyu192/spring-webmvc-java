package spring.webmvc.presentation.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import spring.webmvc.application.service.ProductService;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;

@WebMvcTest(ProductController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class ProductControllerTest {

	@MockitoBean
	private ProductService productService;

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
	void findProducts() throws Exception {
		Pageable pageable = PageRequest.of(0, 10);
		String name = "product";

		List<Product> response = List.of(
			Product.create("name1", "description", 1000, 10, Category.ACCOMMODATION),
			Product.create("name2", "description", 2000, 20, Category.FLIGHT),
			Product.create("name3", "description", 3000, 30, Category.TICKET)
		);
		Page<Product> page = new PageImpl<>(response, pageable, response.size());

		Mockito.when(productService.findProducts(pageable, name)).thenReturn(page);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/products")
					.header("Authorization", "Bearer access-token")
					.param("page", String.valueOf(pageable.getPageNumber()))
					.param("size", String.valueOf(pageable.getPageSize()))
					.param("name", name)
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("product-list",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("page").description("페이지 번호").optional(),
						RequestDocumentation.parameterWithName("size").description("페이지 크기").optional(),
						RequestDocumentation.parameterWithName("name").description("상품명").optional()
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("content[].id").description("아이디"),
						PayloadDocumentation.fieldWithPath("content[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("content[].description").description("설명"),
						PayloadDocumentation.fieldWithPath("content[].price").description("가격"),
						PayloadDocumentation.fieldWithPath("content[].quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("content[].createdAt").description("생성일시"),

						PayloadDocumentation.fieldWithPath("pageable.pageNumber").description("현재 페이지 번호"),
						PayloadDocumentation.fieldWithPath("pageable.pageSize").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("pageable.offset").description("정렬 정보"),
						PayloadDocumentation.fieldWithPath("pageable.paged").description("정렬 정보"),
						PayloadDocumentation.fieldWithPath("pageable.unpaged").description("정렬 정보"),

						PayloadDocumentation.fieldWithPath("pageable.sort.empty").description("정렬이 비어있는지 여부"),
						PayloadDocumentation.fieldWithPath("pageable.sort.sorted").description("정렬되었는지 여부"),
						PayloadDocumentation.fieldWithPath("pageable.sort.unsorted").description("정렬되지 않았는지 여부"),

						PayloadDocumentation.fieldWithPath("last").description("마지막 페이지 여부"),
						PayloadDocumentation.fieldWithPath("totalPages").description("전체 페이지 수"),
						PayloadDocumentation.fieldWithPath("totalElements").description("전체 아이템 수"),
						PayloadDocumentation.fieldWithPath("first").description("첫 페이지 여부"),
						PayloadDocumentation.fieldWithPath("size").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("number").description("현재 페이지 번호"),

						PayloadDocumentation.fieldWithPath("sort.empty").description("정렬이 비어있는지 여부"),
						PayloadDocumentation.fieldWithPath("sort.sorted").description("정렬되었는지 여부"),
						PayloadDocumentation.fieldWithPath("sort.unsorted").description("정렬되지 않았는지 여부"),

						PayloadDocumentation.fieldWithPath("numberOfElements").description("현재 페이지의 아이템 수"),
						PayloadDocumentation.fieldWithPath("empty").description("빈 페이지 여부")
					)
				)
			);
	}
}
