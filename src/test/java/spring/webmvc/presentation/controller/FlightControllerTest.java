package spring.webmvc.presentation.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

import spring.webmvc.application.service.FlightService;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;

@WebMvcTest(FlightController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class FlightControllerTest {

	@MockitoBean
	private FlightService flightService;

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
	void createFlight() throws Exception {
		// Given
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

		Flight flight = Mockito.spy(
			Flight.create(
				name,
				description,
				price,
				quantity,
				airline,
				flightNumber,
				departureAirport,
				arrivalAirport,
				departureTime,
				arrivalTime
			)
		);
		Product product = Mockito.spy(flight.getProduct());

		Mockito.when(flight.getId()).thenReturn(1L);
		Mockito.when(flight.getProduct()).thenReturn(product);
		Mockito.when(product.getId()).thenReturn(1L);
		Mockito.when(product.getCreatedAt()).thenReturn(Instant.now());
		Mockito.when(
			flightService.createFlight(
				name,
				description,
				price,
				quantity,
				airline,
				flightNumber,
				departureAirport,
				arrivalAirport,
				departureTime,
				arrivalTime
			)
		).thenReturn(flight);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/products/flights")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
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
	void updateFlight() throws Exception {
		// Given
		Long flightId = 1L;
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

		Flight flight = Mockito.spy(
			Flight.create(
				name,
				description,
				price,
				quantity,
				airline,
				flightNumber,
				departureAirport,
				arrivalAirport,
				departureTime,
				arrivalTime
			)
		);
		Product product = Mockito.spy(flight.getProduct());

		Mockito.when(flight.getId()).thenReturn(1L);
		Mockito.when(flight.getProduct()).thenReturn(product);
		Mockito.when(product.getId()).thenReturn(1L);
		Mockito.when(product.getCreatedAt()).thenReturn(Instant.now());
		Mockito.when(
			flightService.updateFlight(
				flightId,
				name,
				description,
				price,
				quantity,
				airline,
				flightNumber,
				departureAirport,
				arrivalAirport,
				departureTime,
				arrivalTime
			)
		).thenReturn(flight);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/products/flights/{id}", flightId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer access-token")
					.content("""
						{
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
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("flight-update",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					PayloadDocumentation.requestFields(
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
	void deleteFlight() throws Exception {
		// Given
		Long flightId = 1L;

		Mockito.doNothing().when(flightService).deleteFlight(flightId);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/products/flights/{id}", flightId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document("flight-delete",
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
