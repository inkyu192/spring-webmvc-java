package spring.webmvc.presentation.controller.partner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.webmvc.application.dto.result.CurationDetailResult;
import spring.webmvc.application.dto.result.CurationOffsetPageResult;
import spring.webmvc.application.dto.result.CurationProductResult;
import spring.webmvc.application.dto.result.CurationSummaryResult;
import spring.webmvc.application.service.CurationService;
import spring.webmvc.domain.model.enums.CurationCategory;
import spring.webmvc.infrastructure.config.ControllerTest;

@ControllerTest(CurationController.class)
class CurationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CurationService curationService;

	private CurationSummaryResult curationSummaryResult;
	private CurationDetailResult curationDetailResult;
	private CurationOffsetPageResult curationOffsetPageResult;
	private final Long curationId = 1L;

	@BeforeEach
	void setUp() {
		curationSummaryResult = new CurationSummaryResult(
			curationId,
			"여름 휴가 패키지",
			CurationCategory.HOME
		);

		List<CurationProductResult> products = List.of(
			new CurationProductResult(1L, "제주도 호텔", "제주도 3박 4일", 100000L),
			new CurationProductResult(2L, "부산 교통편", "부산 왕복 교통편", 50000L)
		);

		curationDetailResult = new CurationDetailResult(
			curationId,
			"여름 휴가 패키지",
			CurationCategory.HOME,
			products
		);

		Page<CurationProductResult> productPage = new PageImpl<>(
			products,
			PageRequest.of(0, 20),
			products.size()
		);

		curationOffsetPageResult = new CurationOffsetPageResult(
			curationId,
			"여름 휴가 패키지",
			CurationCategory.HOME,
			productPage
		);
	}

	@Test
	void findCurations() throws Exception {
		CurationCategory category = CurationCategory.HOME;

		when(curationService.findCurations(category)).thenReturn(List.of(curationSummaryResult));

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/partner/curations")
					.header("Authorization", "Bearer access-token")
					.queryParam("category", "HOME")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-curation-list",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("category").description("큐레이션 카테고리")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("size").description("큐레이션 수"),
						PayloadDocumentation.fieldWithPath("curations[].id").description("큐레이션 ID"),
						PayloadDocumentation.fieldWithPath("curations[].title").description("큐레이션 제목"),
						PayloadDocumentation.fieldWithPath("curations[].category").description("큐레이션 카테고리")
					)
				)
			);
	}

	@Test
	void findCuration() throws Exception {
		when(curationService.findCurationProductWithOffsetPage(eq(curationId), any())).thenReturn(
			curationOffsetPageResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/partner/curations/{id}", curationId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-curation-product",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("큐레이션 ID")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("page").description("페이지 번호").optional(),
						RequestDocumentation.parameterWithName("size").description("페이지 크기").optional()
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("큐레이션 ID"),
						PayloadDocumentation.fieldWithPath("title").description("큐레이션 제목"),
						PayloadDocumentation.fieldWithPath("category").description("큐레이션 카테고리"),
						PayloadDocumentation.fieldWithPath("products.page").description("현재 페이지 번호"),
						PayloadDocumentation.fieldWithPath("products.size").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("products.totalElements").description("전체 요소 수"),
						PayloadDocumentation.fieldWithPath("products.totalPages").description("전체 페이지 수"),
						PayloadDocumentation.fieldWithPath("products.hasNext").description("다음 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("products.hasPrevious").description("이전 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("products.content[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products.content[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("products.content[].description").description("상품 설명"),
						PayloadDocumentation.fieldWithPath("products.content[].price").description("상품 가격")
					)
				)
			);
	}

	@Test
	void createCuration() throws Exception {
		when(curationService.createCuration(any())).thenReturn(curationDetailResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/partner/curations")
					.header("Authorization", "Bearer access-token")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "title": "여름 휴가 패키지",
						  "category": "HOME",
						  "isExposed": true,
						  "sortOrder": 1,
						  "products": [
						    {
						      "productId": 1,
						      "sortOrder": 1
						    },
						    {
						      "productId": 2,
						      "sortOrder": 2
						    }
						  ]
						}
						""")
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-curation-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("title").description("큐레이션 제목"),
						PayloadDocumentation.fieldWithPath("category").description("큐레이션 카테고리"),
						PayloadDocumentation.fieldWithPath("isExposed").description("노출 여부"),
						PayloadDocumentation.fieldWithPath("sortOrder").description("정렬 순서"),
						PayloadDocumentation.fieldWithPath("products[].productId").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products[].sortOrder").description("상품 정렬 순서")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("큐레이션 ID"),
						PayloadDocumentation.fieldWithPath("title").description("큐레이션 제목"),
						PayloadDocumentation.fieldWithPath("category").description("큐레이션 카테고리"),
						PayloadDocumentation.fieldWithPath("products[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("products[].description").description("상품 설명"),
						PayloadDocumentation.fieldWithPath("products[].price").description("상품 가격")
					)
				)
			);
	}
}
