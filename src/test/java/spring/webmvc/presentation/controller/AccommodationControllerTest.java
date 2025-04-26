package spring.webmvc.presentation.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

import spring.webmvc.application.service.AccommodationService;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;
import spring.webmvc.presentation.dto.request.AccommodationCreateRequest;
import spring.webmvc.presentation.dto.request.AccommodationUpdateRequest;

@WebMvcTest(AccommodationController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class AccommodationControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private AccommodationService accommodationService;

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
	void createAccommodation() throws Exception {
		// Given
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plus(1, ChronoUnit.DAYS);

		AccommodationCreateRequest request = new AccommodationCreateRequest(
			name,
			description,
			price,
			quantity,
			place,
			checkInTime,
			checkOutTime
		);
		Accommodation accommodation = Accommodation.create(
			name,
			description,
			price,
			quantity,
			place,
			checkInTime,
			checkOutTime
		);
		Mockito.when(
			accommodationService.createAccommodation(
				name,
				description,
				price,
				quantity,
				place,
				checkInTime,
				checkOutTime
			)
		).thenReturn(accommodation);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/products/accommodations")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document("accommodation-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
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
						PayloadDocumentation.fieldWithPath("name").description("숙소명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("checkOutTime").description("체크아웃 시간")
					)
				)
			);
	}

	@Test
	void findAccommodation() throws Exception {
		// Given
		Long requestId = 1L;

		Accommodation accommodation = Accommodation.create(
			"name",
			"description",
			1000,
			5,
			"place",
			Instant.now(),
			Instant.now().plus(1, ChronoUnit.DAYS)
		);

		Mockito.when(accommodationService.findAccommodation(requestId)).thenReturn(accommodation);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/products/accommodations/{id}", requestId)
					.header("Authorization", "Bearer access-token")
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
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("name").description("숙소명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("checkOutTime").description("체크아웃 시간")
					)
				)
			);
	}

	@Test
	void updateAccommodation() throws Exception {
		// Given
		Long requestId = 1L;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plus(1, ChronoUnit.DAYS);

		AccommodationUpdateRequest request = new AccommodationUpdateRequest(
			name,
			description,
			price,
			quantity,
			place,
			checkInTime,
			checkOutTime
		);
		Accommodation accommodation = Accommodation.create(
			name,
			description,
			price,
			quantity,
			place,
			checkInTime,
			checkOutTime
		);
		Mockito.when(
			accommodationService.updateAccommodation(
				requestId,
				name,
				description,
				price,
				quantity,
				place,
				checkInTime,
				checkOutTime
			)
		).thenReturn(accommodation);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/products/accommodations/{id}", requestId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer access-token")
					.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("accommodation-update",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					PayloadDocumentation.requestFields(
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
						PayloadDocumentation.fieldWithPath("name").description("숙소명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
						PayloadDocumentation.fieldWithPath("place").description("장소"),
						PayloadDocumentation.fieldWithPath("checkInTime").description("체크인 시간"),
						PayloadDocumentation.fieldWithPath("checkOutTime").description("체크아웃 시간")
					)
				)
			);
	}

	@Test
	void deleteAccommodation() throws Exception {
		// Given
		Long requestId = 1L;

		Mockito.doNothing().when(accommodationService).deleteAccommodation(requestId);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/products/accommodations/{id}", requestId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document("accommodation-delete",
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