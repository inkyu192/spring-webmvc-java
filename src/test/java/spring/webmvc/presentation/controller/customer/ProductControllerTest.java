package spring.webmvc.presentation.controller.customer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.webmvc.application.dto.query.ProductCursorPageQuery;
import spring.webmvc.application.dto.result.AccommodationResult;
import spring.webmvc.application.dto.result.ProductDetailResult;
import spring.webmvc.application.dto.result.ProductSummaryResult;
import spring.webmvc.application.dto.result.TransportResult;
import spring.webmvc.application.service.ProductService;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;
import spring.webmvc.infrastructure.config.ControllerTest;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@ControllerTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	private ProductSummaryResult productSummaryResult;
	private ProductDetailResult transportDetailResult;
	private ProductDetailResult accommodationDetailResult;
	private final Long productId = 1L;

	@BeforeEach
	void setUp() {
		Instant now = Instant.now();

		ProductExposureAttribute exposureAttribute = new ProductExposureAttribute(
			false, true, false, false
		);

		productSummaryResult = new ProductSummaryResult(
			productId,
			ProductCategory.ACCOMMODATION,
			ProductStatus.SELLING,
			"제주도 호텔",
			"제주도 3박 4일 패키지",
			100000L,
			10L,
			exposureAttribute,
			now
		);

		AccommodationResult accommodationResult = new AccommodationResult(
			1L,
			"제주도",
			now,
			now.plusSeconds(86400)
		);

		accommodationDetailResult = new ProductDetailResult(
			productId,
			ProductCategory.ACCOMMODATION,
			ProductStatus.SELLING,
			"제주도 호텔",
			"제주도 3박 4일 패키지",
			100000L,
			10L,
			exposureAttribute,
			now,
			accommodationResult
		);

		TransportResult transportResult = new TransportResult(
			"Seoul",
			"Busan",
			now,
			now.plusSeconds(7200)
		);

		transportDetailResult = new ProductDetailResult(
			2L,
			ProductCategory.TRANSPORT,
			ProductStatus.SELLING,
			"서울-부산 교통편",
			"KTX 왕복",
			50000L,
			20L,
			exposureAttribute,
			now,
			transportResult
		);
	}

	@Test
	void findProducts() throws Exception {
		CursorPage<ProductSummaryResult> cursorPage = new CursorPage<>(
			List.of(productSummaryResult),
			20L,
			false,
			null
		);

		when(productService.findProductsWithCursorPage(any(ProductCursorPageQuery.class))).thenReturn(cursorPage);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/customer/products")
					.header("Authorization", "Bearer access-token")
					.queryParam("size", "20")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"customer-product-list",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("size").description("페이지 크기").optional()
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("size").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("nextCursorId").description("다음 커서 ID"),
						PayloadDocumentation.fieldWithPath("content[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("content[].category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("content[].status").description("상태"),
						PayloadDocumentation.fieldWithPath("content[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("content[].description").description("상품 설명"),
						PayloadDocumentation.fieldWithPath("content[].price").description("가격"),
						PayloadDocumentation.fieldWithPath("content[].quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("content[].exposureAttribute").description("노출 속성"),
						PayloadDocumentation.fieldWithPath("content[].exposureAttribute.isPromotional")
							.description("프로모션 여부"),
						PayloadDocumentation.fieldWithPath("content[].exposureAttribute.isNewArrival")
							.description("신상품 여부"),
						PayloadDocumentation.fieldWithPath("content[].exposureAttribute.isFeatured")
							.description("추천 상품 여부"),
						PayloadDocumentation.fieldWithPath("content[].exposureAttribute.isLowStock")
							.description("품절 임박 여부"),
						PayloadDocumentation.fieldWithPath("content[].createdAt").description("생성일시")
					)
				)
			);
	}

	@Test
	void findTransport() throws Exception {
		when(productService.findProductCached(productId)).thenReturn(transportDetailResult);
		doNothing().when(productService).incrementProductViewCount(productId);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/customer/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"customer-transport-detail",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("status").description("상태"),
						PayloadDocumentation.fieldWithPath("name").description("교통수단명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("exposureAttribute").description("노출 속성"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isPromotional").description("프로모션 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isNewArrival").description("신상품 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isFeatured").description("추천 상품 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isLowStock").description("품절 임박 여부"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("attribute").description("상세 정보"),
						PayloadDocumentation.fieldWithPath("attribute.departureLocation").description("출발지"),
						PayloadDocumentation.fieldWithPath("attribute.arrivalLocation").description("도착지"),
						PayloadDocumentation.fieldWithPath("attribute.departureTime").description("출발 시간"),
						PayloadDocumentation.fieldWithPath("attribute.arrivalTime").description("도착 시간")
					)
				)
			);
	}

	@Test
	void findAccommodation() throws Exception {
		when(productService.findProductCached(productId)).thenReturn(accommodationDetailResult);
		doNothing().when(productService).incrementProductViewCount(productId);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/customer/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"customer-accommodation-detail",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("status").description("상태"),
						PayloadDocumentation.fieldWithPath("name").description("숙소명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("exposureAttribute").description("노출 속성"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isPromotional").description("프로모션 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isNewArrival").description("신상품 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isFeatured").description("추천 상품 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isLowStock").description("품절 임박 여부"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("attribute").description("상세 정보"),
						PayloadDocumentation.fieldWithPath("attribute.place").description("장소"),
						PayloadDocumentation.fieldWithPath("attribute.checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("attribute.checkOutTime").description("체크아웃 시간")
					)
				)
			);
	}
}
