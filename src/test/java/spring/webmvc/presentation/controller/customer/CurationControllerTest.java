package spring.webmvc.presentation.controller.customer;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.webmvc.application.dto.result.CurationCursorPageResult;
import spring.webmvc.application.dto.result.CurationExposureAttributeResult;
import spring.webmvc.application.dto.result.CurationProductResult;
import spring.webmvc.application.dto.result.CurationSummaryResult;
import spring.webmvc.application.dto.result.ProductExposureAttributeResult;
import spring.webmvc.application.service.CurationService;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.enums.CurationLayout;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;
import spring.webmvc.infrastructure.config.ControllerTest;

@ControllerTest(CurationController.class)
class CurationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CurationService curationService;

	private CurationSummaryResult curationSummaryResult;
	private CurationCursorPageResult curationCursorPageResult;
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

		CursorPage<CurationProductResult> productPage = new CursorPage<>(
			products,
			10L,
			false,
			null
		);

		curationCursorPageResult = new CurationCursorPageResult(
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

		when(curationService.findCurationsCached(placement)).thenReturn(List.of(curationSummaryResult));

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/customer/curations")
					.queryParam("placement", placement.name())
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"customer-curation-list",
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
	void findCurationProduct() throws Exception {
		Long userId = null;
		Long cursorId = null;

		when(curationService.findCurationProductCached(userId, curationId, cursorId)).thenReturn(
			curationCursorPageResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/customer/curations/{id}", curationId)
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"customer-curation-product",
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("큐레이션 ID")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("cursorId").description("커서 ID").optional()
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("큐레이션 ID"),
						PayloadDocumentation.fieldWithPath("title").description("큐레이션 제목"),
						PayloadDocumentation.fieldWithPath("placement").description("큐레이션 카테고리"),
						PayloadDocumentation.fieldWithPath("type").description("큐레이션 타입"),
						PayloadDocumentation.fieldWithPath("exposureAttribute").description("큐레이션 노출 속성"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.layout").description("큐레이션 레이아웃"),
						PayloadDocumentation.fieldWithPath("products.size").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("products.hasNext").description("다음 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("products.nextCursorId").description("다음 커서 ID"),
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
}
