package spring.webmvc.presentation.controller;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.webmvc.application.dto.TicketDto;
import spring.webmvc.application.service.TicketService;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;
import spring.webmvc.presentation.dto.request.TicketCreateRequest;
import spring.webmvc.presentation.dto.request.TicketUpdateRequest;

@WebMvcTest(TicketController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class TicketControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private TicketService ticketService;

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
	void createTicket() throws Exception {
		// Given
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant performanceTime = Instant.now();
		String duration = "duration";
		String ageLimit = "ageLimit";

		TicketCreateRequest request = new TicketCreateRequest(
			name,
			description,
			price,
			quantity,
			place,
			performanceTime,
			duration,
			ageLimit
		);
		Ticket ticket = Ticket.create(
			name,
			description,
			price,
			quantity,
			place,
			performanceTime,
			duration,
			ageLimit
		);

		Mockito.when(
			ticketService.createTicket(
				name,
				description,
				price,
				quantity,
				place,
				performanceTime,
				duration,
				ageLimit
			)
		).thenReturn(ticket);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/products/tickets")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document("ticket-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
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
						PayloadDocumentation.fieldWithPath("name").description("티켓명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("performanceTime").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("duration").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("ageLimit").description("관람 연령")
					)
				)
			);
	}

	@Test
	void findTicket() throws Exception {
		// Given
		Long requestId = 1L;

		TicketDto ticketDto = new TicketDto(
			1L,
			"name",
			"description",
			1000,
			5,
			Instant.now(),
			"place",
			Instant.now(),
			"duration",
			"ageLimit"
		);

		Mockito.when(ticketService.findTicket(requestId)).thenReturn(ticketDto);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/products/tickets/{id}", requestId)
					.header("Authorization", "Bearer access-token")
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
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("name").description("티켓명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("performanceTime").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("duration").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("ageLimit").description("관람 연령")
					)
				)
			);
	}

	@Test
	void updateTicket() throws Exception {
		// Given
		Long requestId = 1L;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant performanceTime = Instant.now();
		String duration = "duration";
		String ageLimit = "ageLimit";

		TicketUpdateRequest request = new TicketUpdateRequest(
			name,
			description,
			price,
			quantity,
			place,
			performanceTime,
			duration,
			ageLimit
		);
		Ticket ticket = Ticket.create(
			name,
			description,
			price,
			quantity,
			place,
			performanceTime,
			duration,
			ageLimit
		);

		Mockito.when(
			ticketService.updateTicket(
				requestId,
				name,
				description,
				price,
				quantity,
				place,
				performanceTime,
				duration,
				ageLimit
			)
		).thenReturn(ticket);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/products/tickets/{id}", requestId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer access-token")
					.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("ticket-update",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					PayloadDocumentation.requestFields(
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
						PayloadDocumentation.fieldWithPath("name").description("티켓명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("performanceTime").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("duration").description("공연 시간"),
						PayloadDocumentation.fieldWithPath("ageLimit").description("관람 연령")
					)
				)
			);
	}

	@Test
	void deleteTicket() throws Exception {
		// Given
		Long requestId = 1L;

		Mockito.doNothing().when(ticketService).deleteTicket(requestId);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/products/tickets/{id}", requestId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document("ticket-delete",
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
