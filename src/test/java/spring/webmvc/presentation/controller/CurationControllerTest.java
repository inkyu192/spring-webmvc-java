package spring.webmvc.presentation.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import spring.webmvc.application.dto.command.CurationCreateCommand;
import spring.webmvc.application.dto.result.CurationProductResult;
import spring.webmvc.application.dto.result.CurationResult;
import spring.webmvc.application.service.CurationService;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@WebMvcTest(CurationController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class CurationControllerTest {

	@MockitoBean
	private CurationService curationService;

	private MockMvc mockMvc;

	private Curation curation1;
	private Curation curation2;
	private Product product1;
	private Product product2;

	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation, WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
				.operationPreprocessors()
				.withRequestDefaults(Preprocessors.prettyPrint())
				.withResponseDefaults(Preprocessors.prettyPrint()))
			.build();

		curation1 = Curation.create("Curation 1", true, 1L);
		curation2 = Curation.create("Curation 2", true, 2L);
		product1 = Product.create("Product 1", "Description 1", 1000L, 10L, Category.ACCOMMODATION);
		product2 = Product.create("Product 2", "Description 2", 2000L, 20L, Category.FLIGHT);
	}

	@Test
	void createCuration() throws Exception {
		// Given
		Long id = 1L;

		Mockito.when(curationService.createCuration(Mockito.any(CurationCreateCommand.class))).thenReturn(id);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/curations")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "title": "인기상품",
						  "isExposed": true,
						  "sortOrder": 1,
						  "products": [
						    {
						      "id": 1,
						      "sortOrder": 1
						    }
						  ]
						}
						""")
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document("curation-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("title").description("큐레이션 제목"),
						PayloadDocumentation.fieldWithPath("isExposed").description("노출 여부"),
						PayloadDocumentation.fieldWithPath("sortOrder").description("정렬 순서"),
						PayloadDocumentation.fieldWithPath("products[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products[].sortOrder").description("상품 정렬 순서")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("큐레이션 ID")
					)
				)
			);
	}

	@Test
	void findCurations() throws Exception {
		// Given
		CurationResult curationResult1 = new CurationResult(curation1);
		CurationResult curationResult2 = new CurationResult(curation2);

		List<CurationResult> result = List.of(curationResult1, curationResult2);

		Mockito.when(curationService.findCurations()).thenReturn(result);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/curations")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("curation-list",
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("count").description("큐레이션 개수"),
						PayloadDocumentation.fieldWithPath("curations[].id").description("큐레이션 ID"),
						PayloadDocumentation.fieldWithPath("curations[].title").description("큐레이션 제목")
					)
				)
			);
	}

	@Test
	void findCurationProduct() throws Exception {
		// Given
		Long curationId = 1L;
		Integer size = 10;
		Long cursorId = null;

		CursorPage<Product> cursorPage = new CursorPage<>(List.of(product1, product2), size, false, null);
		CurationProductResult curationProductResult = new CurationProductResult(curation1, cursorPage);

		Mockito.when(curationService.findCurationProduct(curationId, cursorId, size)).thenReturn(curationProductResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/curations/{id}", curationId)
					.queryParam("size", String.valueOf(size))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("curation-product",
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("큐레이션 ID")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("size").description("페이지 크기").optional(),
						RequestDocumentation.parameterWithName("cursorId").description("커서 ID").optional()
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("큐레이션 ID"),
						PayloadDocumentation.fieldWithPath("title").description("큐레이션 제목"),
						PayloadDocumentation.fieldWithPath("page.size").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("page.hasNext").description("다음 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("page.nextCursorId").description("다음 커서 ID"),
						PayloadDocumentation.fieldWithPath("products[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products[].category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("products[].description").description("설명"),
						PayloadDocumentation.fieldWithPath("products[].price").description("가격"),
						PayloadDocumentation.fieldWithPath("products[].quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("products[].createdAt").description("생성일시")
					)
				)
			);
	}
}
