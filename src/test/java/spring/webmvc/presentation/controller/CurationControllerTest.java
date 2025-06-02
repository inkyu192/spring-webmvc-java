package spring.webmvc.presentation.controller;

import java.time.Instant;
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

import spring.webmvc.application.command.CurationCreateCommand;
import spring.webmvc.application.dto.result.CurationResult;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.application.service.CurationService;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;

@WebMvcTest(CurationController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class CurationControllerTest {

	@MockitoBean
	private CurationService curationService;

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
	void createCuration() throws Exception {
		// Given
		Long id = 1L;
		String title = "Test Curation";

		CurationResult curationResult = Mockito.mock(CurationResult.class);
		Mockito.when(curationResult.id()).thenReturn(id);
		Mockito.when(curationResult.title()).thenReturn(title);

		Mockito.when(curationService.createCuration(Mockito.any(CurationCreateCommand.class)))
			.thenReturn(curationResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/curations")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "title": "%s",
						  "isExposed": true,
						  "sortOrder": 1,
						  "products": [
						    {
						      "id": 1,
						      "sortOrder": 1
						    }
						  ]
						}
						""".formatted(title))
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
						PayloadDocumentation.fieldWithPath("products").description("상품 목록"),
						PayloadDocumentation.fieldWithPath("products[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products[].sortOrder").description("상품 정렬 순서")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("큐레이션 ID"),
						PayloadDocumentation.fieldWithPath("title").description("큐레이션 제목")
					)
				)
			);
	}

	@Test
	void findCurations() throws Exception {
		// Given
		CurationResult curationResult1 = Mockito.mock(CurationResult.class);
		Mockito.when(curationResult1.id()).thenReturn(1L);
		Mockito.when(curationResult1.title()).thenReturn("Curation 1");

		CurationResult curationResult2 = Mockito.mock(CurationResult.class);
		Mockito.when(curationResult2.id()).thenReturn(2L);
		Mockito.when(curationResult2.title()).thenReturn("Curation 2");

		List<CurationResult> curationResults = List.of(curationResult1, curationResult2);

		Mockito.when(curationService.findCurations()).thenReturn(curationResults);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/curations")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("curation-list",
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("[].id").description("큐레이션 ID"),
						PayloadDocumentation.fieldWithPath("[].title").description("큐레이션 제목")
					)
				)
			);
	}

	@Test
	void findCurationProduct() throws Exception {
		// Given
		Long curationId = 1L;
		Pageable pageable = PageRequest.of(0, 10);

		ProductResult productResult1 = Mockito.mock(ProductResult.class);
		Mockito.when(productResult1.getId()).thenReturn(1L);
		Mockito.when(productResult1.getCategory()).thenReturn(Category.ACCOMMODATION);
		Mockito.when(productResult1.getName()).thenReturn("Product 1");
		Mockito.when(productResult1.getDescription()).thenReturn("Description 1");
		Mockito.when(productResult1.getPrice()).thenReturn(1000L);
		Mockito.when(productResult1.getQuantity()).thenReturn(10L);
		Mockito.when(productResult1.getCreatedAt()).thenReturn(Instant.now());

		ProductResult productResult2 = Mockito.mock(ProductResult.class);
		Mockito.when(productResult2.getId()).thenReturn(2L);
		Mockito.when(productResult2.getCategory()).thenReturn(Category.FLIGHT);
		Mockito.when(productResult2.getName()).thenReturn("Product 2");
		Mockito.when(productResult2.getDescription()).thenReturn("Description 2");
		Mockito.when(productResult2.getPrice()).thenReturn(2000L);
		Mockito.when(productResult2.getQuantity()).thenReturn(20L);
		Mockito.when(productResult2.getCreatedAt()).thenReturn(Instant.now());

		List<ProductResult> products = List.of(productResult1, productResult2);
		Page<ProductResult> productPage = new PageImpl<>(products, pageable, products.size());

		Mockito.when(curationService.findCurationProduct(pageable, curationId)).thenReturn(productPage);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/curations/{id}", curationId)
					.param("page", String.valueOf(pageable.getPageNumber()))
					.param("size", String.valueOf(pageable.getPageSize()))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("curation-product",
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("큐레이션 ID")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("page").description("페이지 번호").optional(),
						RequestDocumentation.parameterWithName("size").description("페이지 크기").optional()
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("content[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("content[].category").description("카테고리"),
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
