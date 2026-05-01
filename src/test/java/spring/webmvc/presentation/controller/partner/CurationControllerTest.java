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
import spring.webmvc.application.dto.result.CurationExposureAttributeResult;
import spring.webmvc.application.dto.result.CurationOffsetPageResult;
import spring.webmvc.application.dto.result.CurationProductResult;
import spring.webmvc.application.dto.result.CurationSummaryResult;
import spring.webmvc.application.dto.result.ProductExposureAttributeResult;
import spring.webmvc.application.service.CurationService;
import spring.webmvc.domain.model.enums.CurationLayout;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;
import spring.webmvc.domain.model.vo.CurationAttribute;
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
			CurationPlacement.HOME,
			CurationType.MANUAL,
			new CurationExposureAttributeResult(CurationLayout.CAROUSEL)
		);

		List<CurationProductResult> products = List.of(
			new CurationProductResult(1L, "제주도 호텔", "제주도 3박 4일", 100000L,
				new ProductExposureAttributeResult(true, false, true, false, false, false)),
			new CurationProductResult(2L, "부산 교통편", "부산 왕복 교통편", 50000L,
				new ProductExposureAttributeResult(false, true, false, false, false, false))
		);

		curationDetailResult = new CurationDetailResult(
			curationId,
			"여름 휴가 패키지",
			CurationPlacement.HOME,
			CurationType.MANUAL,
			new CurationAttribute(List.of()),
			new CurationExposureAttributeResult(CurationLayout.CAROUSEL),
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
			CurationPlacement.HOME,
			CurationType.MANUAL,
			new CurationExposureAttributeResult(CurationLayout.CAROUSEL),
			productPage
		);
	}

	@Test
	void findCurations() throws Exception {
		CurationPlacement placement = CurationPlacement.HOME;

		when(curationService.findCurations(placement)).thenReturn(List.of(curationSummaryResult));

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/partner/curations")
					.header("Authorization", "Bearer access-token")
					.queryParam("placement", "HOME")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-curation-list",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("placement").description("큐레이션 카테고리")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("size").description("큐레이션 수"),
						PayloadDocumentation.fieldWithPath("curations[].id").description("큐레이션 ID"),
						PayloadDocumentation.fieldWithPath("curations[].title").description("큐레이션 제목"),
						PayloadDocumentation.fieldWithPath("curations[].placement").description("큐레이션 카테고리"),
						PayloadDocumentation.fieldWithPath("curations[].type").description("큐레이션 타입"),
						PayloadDocumentation.fieldWithPath("curations[].exposureAttribute").description("큐레이션 노출 속성"),
						PayloadDocumentation.fieldWithPath("curations[].exposureAttribute.layout")
							.description("큐레이션 레이아웃")
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
						PayloadDocumentation.fieldWithPath("placement").description("큐레이션 카테고리"),
						PayloadDocumentation.fieldWithPath("type").description("큐레이션 타입"),
						PayloadDocumentation.fieldWithPath("exposureAttribute").description("큐레이션 노출 속성"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.layout").description("큐레이션 레이아웃"),
						PayloadDocumentation.fieldWithPath("products.page").description("현재 페이지 번호"),
						PayloadDocumentation.fieldWithPath("products.size").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("products.totalElements").description("전체 요소 수"),
						PayloadDocumentation.fieldWithPath("products.totalPages").description("전체 페이지 수"),
						PayloadDocumentation.fieldWithPath("products.hasNext").description("다음 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("products.hasPrevious").description("이전 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("products.content[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products.content[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("products.content[].description").description("상품 설명"),
						PayloadDocumentation.fieldWithPath("products.content[].price").description("상품 가격"),
						PayloadDocumentation.fieldWithPath("products.content[].exposureAttribute")
							.description("상품 노출 속성(배지)"),
						PayloadDocumentation.fieldWithPath("products.content[].exposureAttribute.isPromotional")
							.description("프로모션 여부"),
						PayloadDocumentation.fieldWithPath("products.content[].exposureAttribute.isNewArrival")
							.description("신상품 여부"),
						PayloadDocumentation.fieldWithPath("products.content[].exposureAttribute.isFeatured")
							.description("추천 상품 여부"),
						PayloadDocumentation.fieldWithPath("products.content[].exposureAttribute.isLowStock")
							.description("재고 부족 여부"),
						PayloadDocumentation.fieldWithPath("products.content[].exposureAttribute.isRecommended")
							.description("추천 뱃지 여부"),
						PayloadDocumentation.fieldWithPath("products.content[].exposureAttribute.isPersonalPick")
							.description("개인 맞춤 뱃지 여부")
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
						  "placement": "HOME",
						  "type": "MANUAL",
						  "attribute": {
						    "tagIds": [1, 2]
						  },
						  "exposureAttribute": {
						    "layout": "CAROUSEL"
						  },
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
						PayloadDocumentation.fieldWithPath("placement").description("큐레이션 카테고리"),
						PayloadDocumentation.fieldWithPath("type")
							.description("큐레이션 타입 (MANUAL, SEARCH, PERSONALIZED)"),
						PayloadDocumentation.fieldWithPath("attribute").description("큐레이션 속성").optional(),
						PayloadDocumentation.fieldWithPath("attribute.tagIds")
							.description("검색 태그 ID 목록 (SEARCH 타입에서 사용)")
							.optional(),
						PayloadDocumentation.fieldWithPath("exposureAttribute").description("큐레이션 노출 속성").optional(),
						PayloadDocumentation.fieldWithPath("exposureAttribute.layout")
							.description("큐레이션 레이아웃 (GRID, CAROUSEL, LIST)")
							.optional(),
						PayloadDocumentation.fieldWithPath("isExposed").description("노출 여부"),
						PayloadDocumentation.fieldWithPath("sortOrder").description("정렬 순서"),
						PayloadDocumentation.fieldWithPath("products[].productId").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products[].sortOrder").description("상품 정렬 순서")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("큐레이션 ID"),
						PayloadDocumentation.fieldWithPath("title").description("큐레이션 제목"),
						PayloadDocumentation.fieldWithPath("placement").description("큐레이션 카테고리"),
						PayloadDocumentation.fieldWithPath("type").description("큐레이션 타입"),
						PayloadDocumentation.fieldWithPath("attribute").description("큐레이션 속성"),
						PayloadDocumentation.fieldWithPath("attribute.tagIds").description("검색 태그 ID 목록"),
						PayloadDocumentation.fieldWithPath("exposureAttribute").description("큐레이션 노출 속성"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.layout").description("큐레이션 레이아웃"),
						PayloadDocumentation.fieldWithPath("products[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("products[].description").description("상품 설명"),
						PayloadDocumentation.fieldWithPath("products[].price").description("상품 가격"),
						PayloadDocumentation.fieldWithPath("products[].exposureAttribute").description("상품 노출 속성(배지)"),
						PayloadDocumentation.fieldWithPath("products[].exposureAttribute.isPromotional")
							.description("프로모션 여부"),
						PayloadDocumentation.fieldWithPath("products[].exposureAttribute.isNewArrival")
							.description("신상품 여부"),
						PayloadDocumentation.fieldWithPath("products[].exposureAttribute.isFeatured")
							.description("추천 상품 여부"),
						PayloadDocumentation.fieldWithPath("products[].exposureAttribute.isLowStock")
							.description("재고 부족 여부"),
						PayloadDocumentation.fieldWithPath("products[].exposureAttribute.isRecommended")
							.description("추천 뱃지 여부"),
						PayloadDocumentation.fieldWithPath("products[].exposureAttribute.isPersonalPick")
							.description("개인 맞춤 뱃지 여부")
					)
				)
			);
	}
}
