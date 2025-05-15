package spring.webmvc.presentation.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

import spring.webmvc.application.dto.command.AccommodationCreateCommand;
import spring.webmvc.application.dto.command.AccommodationUpdateCommand;
import spring.webmvc.application.dto.command.FlightCreateCommand;
import spring.webmvc.application.dto.command.FlightUpdateCommand;
import spring.webmvc.application.dto.command.TicketCreateCommand;
import spring.webmvc.application.dto.command.TicketUpdateCommand;
import spring.webmvc.application.dto.result.AccommodationResult;
import spring.webmvc.application.dto.result.FlightResult;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.application.dto.result.TicketResult;
import spring.webmvc.application.service.ProductService;
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
		String name = "name";

		ProductResult productResult1 = Mockito.mock(ProductResult.class);
		Mockito.when(productResult1.getId()).thenReturn(1L);
		Mockito.when(productResult1.getCategory()).thenReturn(Category.ACCOMMODATION);
		Mockito.when(productResult1.getName()).thenReturn("name1");
		Mockito.when(productResult1.getDescription()).thenReturn("description1");
		Mockito.when(productResult1.getPrice()).thenReturn(1000);
		Mockito.when(productResult1.getQuantity()).thenReturn(10);
		Mockito.when(productResult1.getCreatedAt()).thenReturn(Instant.now());

		ProductResult productResult2 = Mockito.mock(ProductResult.class);
		Mockito.when(productResult2.getId()).thenReturn(2L);
		Mockito.when(productResult2.getCategory()).thenReturn(Category.FLIGHT);
		Mockito.when(productResult2.getName()).thenReturn("name2");
		Mockito.when(productResult2.getDescription()).thenReturn("description2");
		Mockito.when(productResult2.getPrice()).thenReturn(2000);
		Mockito.when(productResult2.getQuantity()).thenReturn(20);
		Mockito.when(productResult2.getCreatedAt()).thenReturn(Instant.now());

		ProductResult productResult3 = Mockito.mock(ProductResult.class);
		Mockito.when(productResult3.getId()).thenReturn(3L);
		Mockito.when(productResult3.getCategory()).thenReturn(Category.TICKET);
		Mockito.when(productResult3.getName()).thenReturn("name3");
		Mockito.when(productResult3.getDescription()).thenReturn("description3");
		Mockito.when(productResult3.getPrice()).thenReturn(3000);
		Mockito.when(productResult3.getQuantity()).thenReturn(30);
		Mockito.when(productResult3.getCreatedAt()).thenReturn(Instant.now());

		List<ProductResult> response = List.of(productResult1, productResult2, productResult3);
		Page<ProductResult> page = new PageImpl<>(response, pageable, response.size());

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

	@Test
	void findTicket() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.TICKET;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant performanceTime = Instant.now();
		String duration = "duration";
		String ageLimit = "ageLimit";
		Instant createdAt = Instant.now();

		TicketResult ticketResult = Mockito.mock(TicketResult.class);
		Mockito.when(ticketResult.getId()).thenReturn(productId);
		Mockito.when(ticketResult.getCategory()).thenReturn(category);
		Mockito.when(ticketResult.getName()).thenReturn(name);
		Mockito.when(ticketResult.getDescription()).thenReturn(description);
		Mockito.when(ticketResult.getPrice()).thenReturn(price);
		Mockito.when(ticketResult.getQuantity()).thenReturn(quantity);
		Mockito.when(ticketResult.getPlace()).thenReturn(place);
		Mockito.when(ticketResult.getPerformanceTime()).thenReturn(performanceTime);
		Mockito.when(ticketResult.getDuration()).thenReturn(duration);
		Mockito.when(ticketResult.getAgeLimit()).thenReturn(ageLimit);
		Mockito.when(ticketResult.getCreatedAt()).thenReturn(createdAt);

		Mockito.when(productService.findProduct(productId, category)).thenReturn(ticketResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
					.queryParam("category", String.valueOf(category))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("ticket-get",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("category").description("카테고리")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("티켓명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("ticketId").description("티켓아이디"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("performanceTime").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("duration").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("ageLimit").description("관람 연령")
					)
				)
			);
	}

	@Test
	void findFlight() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.FLIGHT;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String airline = "airline";
		String flightNumber = "flightNumber";
		String departureAirport = "departureAirport";
		String arrivalAirport = "arrivalAirport";
		Instant departureTime = Instant.now();
		Instant arrivalTime = Instant.now().plus(1, ChronoUnit.HOURS);
		Instant createdAt = Instant.now();

		FlightResult flightResult = Mockito.mock(FlightResult.class);
		Mockito.when(flightResult.getId()).thenReturn(productId);
		Mockito.when(flightResult.getCategory()).thenReturn(category);
		Mockito.when(flightResult.getName()).thenReturn(name);
		Mockito.when(flightResult.getDescription()).thenReturn(description);
		Mockito.when(flightResult.getPrice()).thenReturn(price);
		Mockito.when(flightResult.getQuantity()).thenReturn(quantity);
		Mockito.when(flightResult.getAirline()).thenReturn(airline);
		Mockito.when(flightResult.getFlightNumber()).thenReturn(flightNumber);
		Mockito.when(flightResult.getDepartureAirport()).thenReturn(departureAirport);
		Mockito.when(flightResult.getArrivalAirport()).thenReturn(arrivalAirport);
		Mockito.when(flightResult.getDepartureTime()).thenReturn(departureTime);
		Mockito.when(flightResult.getArrivalTime()).thenReturn(arrivalTime);
		Mockito.when(flightResult.getCreatedAt()).thenReturn(createdAt);

		Mockito.when(productService.findProduct(productId, category)).thenReturn(flightResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
					.queryParam("category", String.valueOf(category))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("flight-get",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("category").description("카테고리")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("티켓명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("flightId").description("항공아이디"),
						PayloadDocumentation.fieldWithPath("airline").description("항공사"),
						PayloadDocumentation.fieldWithPath("flightNumber").description("항공편 ID"),
						PayloadDocumentation.fieldWithPath("departureAirport").description("출발 공항"),
						PayloadDocumentation.fieldWithPath("arrivalAirport").description("도착 공항"),
						PayloadDocumentation.fieldWithPath("departureTime").description("출발 시간"),
						PayloadDocumentation.fieldWithPath("arrivalTime").description("도착 시간")
					)
				)
			);
	}

	@Test
	void findAccommodation() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.ACCOMMODATION;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plus(1, ChronoUnit.DAYS);
		Instant createdAt = Instant.now();

		AccommodationResult accommodationResult = Mockito.mock(AccommodationResult.class);
		Mockito.when(accommodationResult.getId()).thenReturn(productId);
		Mockito.when(accommodationResult.getCategory()).thenReturn(category);
		Mockito.when(accommodationResult.getName()).thenReturn(name);
		Mockito.when(accommodationResult.getDescription()).thenReturn(description);
		Mockito.when(accommodationResult.getPrice()).thenReturn(price);
		Mockito.when(accommodationResult.getQuantity()).thenReturn(quantity);
		Mockito.when(accommodationResult.getPlace()).thenReturn(place);
		Mockito.when(accommodationResult.getCheckInTime()).thenReturn(checkInTime);
		Mockito.when(accommodationResult.getCheckOutTime()).thenReturn(checkOutTime);
		Mockito.when(accommodationResult.getCreatedAt()).thenReturn(createdAt);

		Mockito.when(productService.findProduct(productId, category)).thenReturn(accommodationResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/products/{id}", productId)
					.header("Authorization", "Bearer access-token")
					.queryParam("category", String.valueOf(category))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("accommodation-get",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("category").description("카테고리")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("티켓명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("accommodationId").description("숙소아이디"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("checkOutTime").description("체크아웃 시간")
					)
				)
			);
	}

	@Test
	void createTicket() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.TICKET;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant performanceTime = Instant.now();
		String duration = "duration";
		String ageLimit = "ageLimit";
		Instant createdAt = Instant.now();

		TicketResult ticketResult = Mockito.mock(TicketResult.class);
		Mockito.when(ticketResult.getId()).thenReturn(productId);
		Mockito.when(ticketResult.getCategory()).thenReturn(category);
		Mockito.when(ticketResult.getName()).thenReturn(name);
		Mockito.when(ticketResult.getDescription()).thenReturn(description);
		Mockito.when(ticketResult.getPrice()).thenReturn(price);
		Mockito.when(ticketResult.getQuantity()).thenReturn(quantity);
		Mockito.when(ticketResult.getPlace()).thenReturn(place);
		Mockito.when(ticketResult.getPerformanceTime()).thenReturn(performanceTime);
		Mockito.when(ticketResult.getDuration()).thenReturn(duration);
		Mockito.when(ticketResult.getAgeLimit()).thenReturn(ageLimit);
		Mockito.when(ticketResult.getCreatedAt()).thenReturn(createdAt);

		Mockito.when(productService.createProduct(Mockito.any(TicketCreateCommand.class))).thenReturn(ticketResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/products")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "category": "%s",
						  "name": "%s",
						  "description": "%s",
						  "price": %d,
						  "quantity": %d,
						  "place": "%s",
						  "performanceTime": "%s",
						  "duration": "%s",
						  "ageLimit": "%s"
						}
						""".formatted(
						category,
						name,
						description,
						price,
						quantity,
						place,
						performanceTime.toString(),
						duration,
						ageLimit
					))
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document("ticket-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("티켓명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("performanceTime").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("duration").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("ageLimit").description("관람 연령")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("티켓명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("ticketId").description("티켓아이디"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("performanceTime").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("duration").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("ageLimit").description("관람 연령")
					)
				)
			);
	}

	@Test
	void createFlight() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.FLIGHT;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String airline = "airline";
		String flightNumber = "flightNumber";
		String departureAirport = "departureAirport";
		String arrivalAirport = "arrivalAirport";
		Instant departureTime = Instant.now();
		Instant arrivalTime = Instant.now().plus(1, ChronoUnit.HOURS);
		Instant createdAt = Instant.now();

		FlightResult flightResult = Mockito.mock(FlightResult.class);
		Mockito.when(flightResult.getId()).thenReturn(productId);
		Mockito.when(flightResult.getCategory()).thenReturn(category);
		Mockito.when(flightResult.getName()).thenReturn(name);
		Mockito.when(flightResult.getDescription()).thenReturn(description);
		Mockito.when(flightResult.getPrice()).thenReturn(price);
		Mockito.when(flightResult.getQuantity()).thenReturn(quantity);
		Mockito.when(flightResult.getAirline()).thenReturn(airline);
		Mockito.when(flightResult.getFlightNumber()).thenReturn(flightNumber);
		Mockito.when(flightResult.getDepartureAirport()).thenReturn(departureAirport);
		Mockito.when(flightResult.getArrivalAirport()).thenReturn(arrivalAirport);
		Mockito.when(flightResult.getDepartureTime()).thenReturn(departureTime);
		Mockito.when(flightResult.getArrivalTime()).thenReturn(arrivalTime);
		Mockito.when(flightResult.getCreatedAt()).thenReturn(createdAt);

		Mockito.when(productService.createProduct(Mockito.any(FlightCreateCommand.class))).thenReturn(flightResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/products")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "category": "%s",
						  "name": "%s",
						  "description": "%s",
						  "price": %d,
						  "quantity": %d,
						  "airline": "%s",
						  "flightNumber": "%s",
						  "departureAirport": "%s",
						  "arrivalAirport": "%s",
						  "departureTime": "%s",
						  "arrivalTime": "%s"
						}
						""".formatted(
						category,
						name,
						description,
						price,
						quantity,
						airline,
						flightNumber,
						departureAirport,
						arrivalAirport,
						departureTime.toString(),
						arrivalTime.toString()
					))
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document("flight-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("항공편명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("airline").description("항공사"),
						PayloadDocumentation.fieldWithPath("flightNumber").description("항공편 ID"),
						PayloadDocumentation.fieldWithPath("departureAirport").description("출발 공항"),
						PayloadDocumentation.fieldWithPath("arrivalAirport").description("도착 공항"),
						PayloadDocumentation.fieldWithPath("departureTime").description("출발 시간"),
						PayloadDocumentation.fieldWithPath("arrivalTime").description("도착 시간")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("항공편명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("flightId").description("항공아이디"),
						PayloadDocumentation.fieldWithPath("airline").description("항공사"),
						PayloadDocumentation.fieldWithPath("flightNumber").description("항공편 ID"),
						PayloadDocumentation.fieldWithPath("departureAirport").description("출발 공항"),
						PayloadDocumentation.fieldWithPath("arrivalAirport").description("도착 공항"),
						PayloadDocumentation.fieldWithPath("departureTime").description("출발 시간"),
						PayloadDocumentation.fieldWithPath("arrivalTime").description("도착 시간")
					)
				)
			);
	}

	@Test
	void createAccommodation() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.ACCOMMODATION;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plus(1, ChronoUnit.DAYS);
		Instant createdAt = Instant.now();

		AccommodationResult accommodationResult = Mockito.mock(AccommodationResult.class);
		Mockito.when(accommodationResult.getId()).thenReturn(productId);
		Mockito.when(accommodationResult.getCategory()).thenReturn(category);
		Mockito.when(accommodationResult.getName()).thenReturn(name);
		Mockito.when(accommodationResult.getDescription()).thenReturn(description);
		Mockito.when(accommodationResult.getPrice()).thenReturn(price);
		Mockito.when(accommodationResult.getQuantity()).thenReturn(quantity);
		Mockito.when(accommodationResult.getPlace()).thenReturn(place);
		Mockito.when(accommodationResult.getCheckInTime()).thenReturn(checkInTime);
		Mockito.when(accommodationResult.getCheckOutTime()).thenReturn(checkOutTime);
		Mockito.when(accommodationResult.getCreatedAt()).thenReturn(createdAt);

		Mockito.when(productService.createProduct(Mockito.any(AccommodationCreateCommand.class)))
			.thenReturn(accommodationResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/products")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "category": "%s",
						  "name": "%s",
						  "description": "%s",
						  "price": %d,
						  "quantity": %d,
						  "place": "%s",
						  "checkInTime": "%s",
						  "checkOutTime": "%s"
						}
						""".formatted(
						category,
						name,
						description,
						price,
						quantity,
						place,
						checkInTime.toString(),
						checkOutTime.toString()
					))
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document("accommodation-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("숙소명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("checkOutTime").description("체크아웃 시간")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("숙소명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("accommodationId").description("숙소아이디"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("checkOutTime").description("체크아웃 시간")
					)
				)
			);
	}

	@Test
	void updateTicket() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.TICKET;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant performanceTime = Instant.now();
		String duration = "duration";
		String ageLimit = "ageLimit";
		Instant createdAt = Instant.now();

		TicketResult ticketResult = Mockito.mock(TicketResult.class);
		Mockito.when(ticketResult.getId()).thenReturn(productId);
		Mockito.when(ticketResult.getCategory()).thenReturn(category);
		Mockito.when(ticketResult.getName()).thenReturn(name);
		Mockito.when(ticketResult.getDescription()).thenReturn(description);
		Mockito.when(ticketResult.getPrice()).thenReturn(price);
		Mockito.when(ticketResult.getQuantity()).thenReturn(quantity);
		Mockito.when(ticketResult.getPlace()).thenReturn(place);
		Mockito.when(ticketResult.getPerformanceTime()).thenReturn(performanceTime);
		Mockito.when(ticketResult.getDuration()).thenReturn(duration);
		Mockito.when(ticketResult.getAgeLimit()).thenReturn(ageLimit);
		Mockito.when(ticketResult.getCreatedAt()).thenReturn(createdAt);

		Mockito.when(productService.updateProduct(Mockito.eq(productId), Mockito.any(TicketUpdateCommand.class)))
			.thenReturn(ticketResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/products/{id}", productId)
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "category": "%s",
						  "name": "%s",
						  "description": "%s",
						  "price": %d,
						  "quantity": %d,
						  "place": "%s",
						  "performanceTime": "%s",
						  "duration": "%s",
						  "ageLimit": "%s"
						}
						""".formatted(
						category,
						name,
						description,
						price,
						quantity,
						place,
						performanceTime.toString(),
						duration,
						ageLimit
					))
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("ticket-update",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("티켓명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("performanceTime").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("duration").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("ageLimit").description("관람 연령")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("티켓명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("ticketId").description("티켓아이디"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("performanceTime").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("duration").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("ageLimit").description("관람 연령")
					)
				)
			);
	}

	@Test
	void updateFlight() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.FLIGHT;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String airline = "airline";
		String flightNumber = "flightNumber";
		String departureAirport = "departureAirport";
		String arrivalAirport = "arrivalAirport";
		Instant departureTime = Instant.now();
		Instant arrivalTime = Instant.now().plus(1, ChronoUnit.HOURS);
		Instant createdAt = Instant.now();

		FlightResult flightResult = Mockito.mock(FlightResult.class);
		Mockito.when(flightResult.getId()).thenReturn(productId);
		Mockito.when(flightResult.getCategory()).thenReturn(category);
		Mockito.when(flightResult.getName()).thenReturn(name);
		Mockito.when(flightResult.getDescription()).thenReturn(description);
		Mockito.when(flightResult.getPrice()).thenReturn(price);
		Mockito.when(flightResult.getQuantity()).thenReturn(quantity);
		Mockito.when(flightResult.getAirline()).thenReturn(airline);
		Mockito.when(flightResult.getFlightNumber()).thenReturn(flightNumber);
		Mockito.when(flightResult.getDepartureAirport()).thenReturn(departureAirport);
		Mockito.when(flightResult.getArrivalAirport()).thenReturn(arrivalAirport);
		Mockito.when(flightResult.getDepartureTime()).thenReturn(departureTime);
		Mockito.when(flightResult.getArrivalTime()).thenReturn(arrivalTime);
		Mockito.when(flightResult.getCreatedAt()).thenReturn(createdAt);

		Mockito.when(productService.updateProduct(Mockito.eq(productId), Mockito.any(FlightUpdateCommand.class)))
			.thenReturn(flightResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/products/{id}", productId)
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "category": "%s",
						  "name": "%s",
						  "description": "%s",
						  "price": %d,
						  "quantity": %d,
						  "airline": "%s",
						  "flightNumber": "%s",
						  "departureAirport": "%s",
						  "arrivalAirport": "%s",
						  "departureTime": "%s",
						  "arrivalTime": "%s"
						}
						""".formatted(
						category,
						name,
						description,
						price,
						quantity,
						airline,
						flightNumber,
						departureAirport,
						arrivalAirport,
						departureTime.toString(),
						arrivalTime.toString()
					))
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("flight-update",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("항공편명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("airline").description("항공사"),
						PayloadDocumentation.fieldWithPath("flightNumber").description("항공편 ID"),
						PayloadDocumentation.fieldWithPath("departureAirport").description("출발 공항"),
						PayloadDocumentation.fieldWithPath("arrivalAirport").description("도착 공항"),
						PayloadDocumentation.fieldWithPath("departureTime").description("출발 시간"),
						PayloadDocumentation.fieldWithPath("arrivalTime").description("도착 시간")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("항공편명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("flightId").description("항공아이디"),
						PayloadDocumentation.fieldWithPath("airline").description("항공사"),
						PayloadDocumentation.fieldWithPath("flightNumber").description("항공편 ID"),
						PayloadDocumentation.fieldWithPath("departureAirport").description("출발 공항"),
						PayloadDocumentation.fieldWithPath("arrivalAirport").description("도착 공항"),
						PayloadDocumentation.fieldWithPath("departureTime").description("출발 시간"),
						PayloadDocumentation.fieldWithPath("arrivalTime").description("도착 시간")
					)
				)
			);
	}

	@Test
	void updateAccommodation() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.ACCOMMODATION;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plus(1, ChronoUnit.DAYS);
		Instant createdAt = Instant.now();

		AccommodationResult accommodationResult = Mockito.mock(AccommodationResult.class);
		Mockito.when(accommodationResult.getId()).thenReturn(productId);
		Mockito.when(accommodationResult.getCategory()).thenReturn(category);
		Mockito.when(accommodationResult.getName()).thenReturn(name);
		Mockito.when(accommodationResult.getDescription()).thenReturn(description);
		Mockito.when(accommodationResult.getPrice()).thenReturn(price);
		Mockito.when(accommodationResult.getQuantity()).thenReturn(quantity);
		Mockito.when(accommodationResult.getPlace()).thenReturn(place);
		Mockito.when(accommodationResult.getCheckInTime()).thenReturn(checkInTime);
		Mockito.when(accommodationResult.getCheckOutTime()).thenReturn(checkOutTime);
		Mockito.when(accommodationResult.getCreatedAt()).thenReturn(createdAt);

		Mockito.when(productService.updateProduct(Mockito.eq(productId), Mockito.any(AccommodationUpdateCommand.class)))
			.thenReturn(accommodationResult);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/products/{id}", productId)
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "category": "%s",
						  "name": "%s",
						  "description": "%s",
						  "price": %d,
						  "quantity": %d,
						  "place": "%s",
						  "checkInTime": "%s",
						  "checkOutTime": "%s"
						}
						""".formatted(
						category,
						name,
						description,
						price,
						quantity,
						place,
						checkInTime.toString(),
						checkOutTime.toString()
					))
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("accommodation-update",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("숙소명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("checkOutTime").description("체크아웃 시간")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리"),
						PayloadDocumentation.fieldWithPath("name").description("숙소명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("accommodationId").description("숙소아이디"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("checkOutTime").description("체크아웃 시간")
					)
				)
			);
	}

	@Test
	void deleteTicket() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.TICKET;

		Mockito.doNothing().when(productService).deleteProduct(category, productId);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/products/{id}", productId)
					.param("category", category.toString())
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document("ticket-delete",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					)
				)
			);
	}

	@Test
	void deleteFlight() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.FLIGHT;

		Mockito.doNothing().when(productService).deleteProduct(category, productId);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/products/{id}", productId)
					.param("category", category.toString())
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document("flight-delete",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					)
				)
			);
	}

	@Test
	void deleteAccommodation() throws Exception {
		// Given
		Long productId = 1L;
		Category category = Category.ACCOMMODATION;

		Mockito.doNothing().when(productService).deleteProduct(category, productId);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/products/{id}", productId)
					.param("category", category.toString())
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document("accommodation-delete",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					)
				)
			);
	}
}
