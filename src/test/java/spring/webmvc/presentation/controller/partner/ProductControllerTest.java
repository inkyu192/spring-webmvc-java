package spring.webmvc.presentation.controller.partner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
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

import spring.webmvc.application.dto.query.ProductOffsetPageQuery;
import spring.webmvc.application.dto.result.AccommodationResult;
import spring.webmvc.application.dto.result.ProductDetailResult;
import spring.webmvc.application.dto.result.ProductSummaryResult;
import spring.webmvc.application.dto.result.TransportResult;
import spring.webmvc.application.service.ProductService;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;
import spring.webmvc.infrastructure.config.ControllerTest;

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
	private final String name = "name";

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
		Page<ProductSummaryResult> page = new PageImpl<>(
			List.of(productSummaryResult),
			PageRequest.of(0, 10),
			1
		);

		when(productService.findProductsWithOffsetPage(any(ProductOffsetPageQuery.class))).thenReturn(page);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/partner/products")
					.header("Authorization", "Bearer access-token")
					.queryParam("page", "0")
					.queryParam("size", "10")
					.queryParam("name", name)
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-product-list",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("page").description("페이지 번호").optional(),
						RequestDocumentation.parameterWithName("size").description("페이지 크기").optional(),
						RequestDocumentation.parameterWithName("name").description("상품명").optional()
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("page").description("현재 페이지 번호"),
						PayloadDocumentation.fieldWithPath("size").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("totalElements").description("전체 요소 수"),
						PayloadDocumentation.fieldWithPath("totalPages").description("전체 페이지 수"),
						PayloadDocumentation.fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("hasPrevious").description("이전 페이지 존재 여부"),
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
		when(productService.findProduct(productId)).thenReturn(transportDetailResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/partner/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-transport-detail",
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
						PayloadDocumentation.fieldWithPath("exposureAttribute").description("노출 속성").optional(),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isPromotional")
							.description("프로모션 여부")
							.optional(),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isNewArrival")
							.description("신상품 여부")
							.optional(),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isFeatured")
							.description("추천 상품 여부")
							.optional(),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isLowStock")
							.description("품절 임박 여부")
							.optional(),
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
		when(productService.findProduct(productId)).thenReturn(accommodationDetailResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/partner/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-accommodation-detail",
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
						PayloadDocumentation.fieldWithPath("exposureAttribute").description("노출 속성").optional(),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isPromotional")
							.description("프로모션 여부")
							.optional(),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isNewArrival")
							.description("신상품 여부")
							.optional(),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isFeatured")
							.description("추천 상품 여부")
							.optional(),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isLowStock")
							.description("품절 임박 여부")
							.optional(),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("attribute").description("상세 정보"),
						PayloadDocumentation.fieldWithPath("attribute.place").description("장소"),
						PayloadDocumentation.fieldWithPath("attribute.checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("attribute.checkOutTime").description("체크아웃 시간")
					)
				)
			);
	}

	@Test
	void createTransport() throws Exception {
		when(productService.createProduct(any())).thenReturn(transportDetailResult);

		Instant departureTime = Instant.now();
		Instant arrivalTime = Instant.now().plusSeconds(3600);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/partner/products")
					.header("Authorization", "Bearer access-token")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "category": "TRANSPORT",
						  "name": "서울-부산 교통편",
						  "description": "KTX 왕복",
						  "price": 50000,
						  "quantity": 20,
						  "exposureAttribute": {
						    "isPromotional": false,
						    "isNewArrival": false,
						    "isFeatured": false,
						    "isLowStock": false
						  },
						  "attribute": {
						    "departureLocation": "Seoul",
						    "arrivalLocation": "Busan",
						    "departureTime": "%s",
						    "arrivalTime": "%s"
						  }
						}
						""".formatted(departureTime, arrivalTime))
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-transport-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("교통수단명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("exposureAttribute").description("노출 속성"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isPromotional").description("프로모션 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isNewArrival").description("신상품 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isFeatured").description("추천 상품 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isLowStock").description("품절 임박 여부"),
						PayloadDocumentation.fieldWithPath("attribute").description("상세 정보"),
						PayloadDocumentation.fieldWithPath("attribute.departureLocation").description("출발지"),
						PayloadDocumentation.fieldWithPath("attribute.arrivalLocation").description("도착지"),
						PayloadDocumentation.fieldWithPath("attribute.departureTime").description("출발 시간"),
						PayloadDocumentation.fieldWithPath("attribute.arrivalTime").description("도착 시간")
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
	void createAccommodation() throws Exception {
		when(productService.createProduct(any())).thenReturn(accommodationDetailResult);

		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plusSeconds(86400);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/partner/products")
					.header("Authorization", "Bearer access-token")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "category": "ACCOMMODATION",
						  "name": "제주도 호텔",
						  "description": "제주도 3박 4일 패키지",
						  "price": 100000,
						  "quantity": 10,
						  "exposureAttribute": {
						    "isPromotional": false,
						    "isNewArrival": false,
						    "isFeatured": false,
						    "isLowStock": false
						  },
						  "attribute": {
						    "place": "제주도",
						    "checkInTime": "%s",
						    "checkOutTime": "%s"
						  }
						}
						""".formatted(checkInTime, checkOutTime))
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-accommodation-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("숙소명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("exposureAttribute").description("노출 속성"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isPromotional").description("프로모션 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isNewArrival").description("신상품 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isFeatured").description("추천 상품 여부"),
						PayloadDocumentation.fieldWithPath("exposureAttribute.isLowStock").description("품절 임박 여부"),
						PayloadDocumentation.fieldWithPath("attribute").description("상세 정보"),
						PayloadDocumentation.fieldWithPath("attribute.place").description("장소"),
						PayloadDocumentation.fieldWithPath("attribute.checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("attribute.checkOutTime").description("체크아웃 시간")
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

	@Test
	void updateTransport() throws Exception {
		when(productService.updateProduct(any())).thenReturn(transportDetailResult);

		Instant departureTime = Instant.now();
		Instant arrivalTime = Instant.now().plusSeconds(3600);

		mockMvc.perform(
				RestDocumentationRequestBuilders.put("/partner/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "status": "SELLING",
						  "name": "서울-부산 교통편",
						  "description": "KTX 왕복",
						  "price": 50000,
						  "quantity": 20,
						  "exposureAttribute": {
						    "isPromotional": false,
						    "isNewArrival": false,
						    "isFeatured": false,
						    "isLowStock": false
						  },
						  "attribute": {
						    "departureLocation": "Seoul",
						    "arrivalLocation": "Busan",
						    "departureTime": "%s",
						    "arrivalTime": "%s"
						  }
						}
						""".formatted(departureTime, arrivalTime))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-transport-replace",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					PayloadDocumentation.requestFields(
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
						PayloadDocumentation.fieldWithPath("attribute").description("상세 정보"),
						PayloadDocumentation.fieldWithPath("attribute.departureLocation").description("출발지"),
						PayloadDocumentation.fieldWithPath("attribute.arrivalLocation").description("도착지"),
						PayloadDocumentation.fieldWithPath("attribute.departureTime").description("출발 시간"),
						PayloadDocumentation.fieldWithPath("attribute.arrivalTime").description("도착 시간")
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
	void updateAccommodation() throws Exception {
		when(productService.updateProduct(any())).thenReturn(accommodationDetailResult);

		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plusSeconds(86400);

		mockMvc.perform(
				RestDocumentationRequestBuilders.put("/partner/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "status": "SELLING",
						  "name": "제주도 호텔",
						  "description": "제주도 3박 4일 패키지",
						  "price": 100000,
						  "quantity": 10,
						  "exposureAttribute": {
						    "isPromotional": false,
						    "isNewArrival": false,
						    "isFeatured": false,
						    "isLowStock": false
						  },
						  "attribute": {
						    "place": "제주도",
						    "checkInTime": "%s",
						    "checkOutTime": "%s"
						  }
						}
						""".formatted(checkInTime, checkOutTime))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-accommodation-replace",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					PayloadDocumentation.requestFields(
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
						PayloadDocumentation.fieldWithPath("attribute").description("상세 정보"),
						PayloadDocumentation.fieldWithPath("attribute.place").description("장소"),
						PayloadDocumentation.fieldWithPath("attribute.checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("attribute.checkOutTime").description("체크아웃 시간")
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

	@Test
	void deleteTransport() throws Exception {
		doNothing().when(productService).deleteProduct(productId);

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/partner/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-transport-delete",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					)
				)
			);
	}

	@Test
	void deleteAccommodation() throws Exception {
		doNothing().when(productService).deleteProduct(productId);

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/partner/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-accommodation-delete",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					)
				)
			);
	}
}
