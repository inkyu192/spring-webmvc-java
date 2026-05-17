package spring.webmvc.presentation.controller.customer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.webmvc.application.dto.result.ProductExposureAttributeResult;
import spring.webmvc.application.dto.result.ProductSummaryResult;
import spring.webmvc.application.dto.result.WishlistResult;
import spring.webmvc.application.service.ProductService;
import spring.webmvc.application.service.WishlistService;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.infrastructure.config.ControllerTest;
import spring.webmvc.infrastructure.security.SecurityContextUtil;

@ControllerTest(MyController.class)
class MyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	@MockitoBean
	private WishlistService wishlistService;

	private ProductSummaryResult productSummaryResult;
	private final Long userId = 1L;

	@BeforeEach
	void setUp() {
		Instant now = Instant.now();

		ProductExposureAttributeResult exposureAttribute = new ProductExposureAttributeResult(
			false, true, false, false, false, false, true, false
		);

		productSummaryResult = new ProductSummaryResult(
			1L,
			ProductCategory.ACCOMMODATION,
			ProductStatus.SELLING,
			"제주도 호텔",
			"제주도 3박 4일 패키지",
			100000L,
			10L,
			exposureAttribute,
			now
		);
	}

	@Test
	void findRecentlyViewedProducts() throws Exception {
		CursorPage<ProductSummaryResult> cursorPage = new CursorPage<>(
			List.of(productSummaryResult),
			20L,
			false,
			null
		);

		try (MockedStatic<SecurityContextUtil> mockedStatic = mockStatic(SecurityContextUtil.class)) {
			mockedStatic.when(SecurityContextUtil::getUserId).thenReturn(userId);

			when(productService.findRecentlyViewedProducts(any(), any())).thenReturn(cursorPage);

			mockMvc.perform(
					RestDocumentationRequestBuilders.get("/customer/my/recently-viewed")
						.header("Authorization", "Bearer access-token")
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(
					MockMvcRestDocumentation.document(
						"customer-my-recently-viewed",
						HeaderDocumentation.requestHeaders(
							HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
						),
						RequestDocumentation.queryParameters(
							RequestDocumentation.parameterWithName("cursorId").description("커서 ID").optional()
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
							PayloadDocumentation.fieldWithPath("content[].exposureAttribute.isRecommended")
								.description("추천 뱃지 여부"),
							PayloadDocumentation.fieldWithPath("content[].exposureAttribute.isPersonalPick")
								.description("개인 맞춤 뱃지 여부"),
							PayloadDocumentation.fieldWithPath("content[].exposureAttribute.isRecentlyViewed")
								.description("최근 본 상품 여부"),
							PayloadDocumentation.fieldWithPath("content[].exposureAttribute.isWished")
								.description("찜 여부"),
							PayloadDocumentation.fieldWithPath("content[].createdAt").description("생성일시")
						)
					)
				);
		}
	}

	@Test
	void findWishlists() throws Exception {
		Instant now = Instant.now();
		WishlistResult wishlistResult = new WishlistResult(1L, productSummaryResult, now);

		CursorPage<WishlistResult> cursorPage = new CursorPage<>(
			List.of(wishlistResult),
			20L,
			false,
			null
		);

		try (MockedStatic<SecurityContextUtil> mockedStatic = mockStatic(SecurityContextUtil.class)) {
			mockedStatic.when(SecurityContextUtil::getUserId).thenReturn(userId);

			when(wishlistService.findWishlists(any(), any())).thenReturn(cursorPage);

			mockMvc.perform(
					RestDocumentationRequestBuilders.get("/customer/my/wishlists")
						.header("Authorization", "Bearer access-token")
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(
					MockMvcRestDocumentation.document(
						"customer-my-wishlists",
						HeaderDocumentation.requestHeaders(
							HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
						),
						RequestDocumentation.queryParameters(
							RequestDocumentation.parameterWithName("cursorId").description("커서 ID").optional()
						),
						PayloadDocumentation.responseFields(
							PayloadDocumentation.fieldWithPath("size").description("페이지 크기"),
							PayloadDocumentation.fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
							PayloadDocumentation.fieldWithPath("nextCursorId").description("다음 커서 ID"),
							PayloadDocumentation.fieldWithPath("content[].id").description("찜 ID"),
							PayloadDocumentation.fieldWithPath("content[].product").description("상품 정보"),
							PayloadDocumentation.fieldWithPath("content[].product.id").description("상품 ID"),
							PayloadDocumentation.fieldWithPath("content[].product.category").description("카테고리"),
							PayloadDocumentation.fieldWithPath("content[].product.status").description("상태"),
							PayloadDocumentation.fieldWithPath("content[].product.name").description("상품명"),
							PayloadDocumentation.fieldWithPath("content[].product.description").description("상품 설명"),
							PayloadDocumentation.fieldWithPath("content[].product.price").description("가격"),
							PayloadDocumentation.fieldWithPath("content[].product.quantity").description("수량"),
							PayloadDocumentation.fieldWithPath("content[].product.exposureAttribute")
								.description("노출 속성"),
							PayloadDocumentation.fieldWithPath("content[].product.exposureAttribute.isPromotional")
								.description("프로모션 여부"),
							PayloadDocumentation.fieldWithPath("content[].product.exposureAttribute.isNewArrival")
								.description("신상품 여부"),
							PayloadDocumentation.fieldWithPath("content[].product.exposureAttribute.isFeatured")
								.description("추천 상품 여부"),
							PayloadDocumentation.fieldWithPath("content[].product.exposureAttribute.isLowStock")
								.description("품절 임박 여부"),
							PayloadDocumentation.fieldWithPath("content[].product.exposureAttribute.isRecommended")
								.description("추천 뱃지 여부"),
							PayloadDocumentation.fieldWithPath("content[].product.exposureAttribute.isPersonalPick")
								.description("개인 맞춤 뱃지 여부"),
							PayloadDocumentation.fieldWithPath("content[].product.exposureAttribute.isRecentlyViewed")
								.description("최근 본 상품 여부"),
							PayloadDocumentation.fieldWithPath("content[].product.exposureAttribute.isWished")
								.description("찜 여부"),
							PayloadDocumentation.fieldWithPath("content[].product.createdAt").description("상품 생성일시"),
							PayloadDocumentation.fieldWithPath("content[].createdAt").description("찜 생성일시")
						)
					)
				);
		}
	}

	@Test
	void addWishlist() throws Exception {
		try (MockedStatic<SecurityContextUtil> mockedStatic = mockStatic(SecurityContextUtil.class)) {
			mockedStatic.when(SecurityContextUtil::getUserId).thenReturn(userId);

			doNothing().when(wishlistService).addWishlist(any(), any());

			mockMvc.perform(
					RestDocumentationRequestBuilders.post("/customer/my/wishlists")
						.header("Authorization", "Bearer access-token")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"productId\": 1}")
				)
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(
					MockMvcRestDocumentation.document(
						"customer-my-wishlists-add",
						HeaderDocumentation.requestHeaders(
							HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
						),
						PayloadDocumentation.requestFields(
							PayloadDocumentation.fieldWithPath("productId").description("상품 ID")
						)
					)
				);
		}
	}

	@Test
	void removeWishlist() throws Exception {
		try (MockedStatic<SecurityContextUtil> mockedStatic = mockStatic(SecurityContextUtil.class)) {
			mockedStatic.when(SecurityContextUtil::getUserId).thenReturn(userId);

			doNothing().when(wishlistService).removeWishlist(any(), any());

			mockMvc.perform(
					RestDocumentationRequestBuilders.delete("/customer/my/wishlists/{wishlistId}", 1L)
						.header("Authorization", "Bearer access-token")
				)
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				.andDo(
					MockMvcRestDocumentation.document(
						"customer-my-wishlists-remove",
						HeaderDocumentation.requestHeaders(
							HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
						),
						RequestDocumentation.pathParameters(
							RequestDocumentation.parameterWithName("wishlistId").description("찜 ID")
						)
					)
				);
		}
	}
}
