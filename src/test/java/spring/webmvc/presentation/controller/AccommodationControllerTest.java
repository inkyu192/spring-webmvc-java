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

import spring.webmvc.application.service.AccommodationService;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;

@WebMvcTest(AccommodationController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class AccommodationControllerTest {

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
	void updateAccommodation() throws Exception {
		// Given
		Long accommodationId = 1L;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plus(1, ChronoUnit.DAYS);

		Accommodation accommodation = Mockito.spy(
			Accommodation.create(
				name,
				description,
				price,
				quantity,
				place,
				checkInTime,
				checkOutTime
			)
		);
		Product product = Mockito.spy(accommodation.getProduct());

		Mockito.when(accommodation.getId()).thenReturn(1L);
		Mockito.when(accommodation.getProduct()).thenReturn(product);
		Mockito.when(product.getId()).thenReturn(1L);
		Mockito.when(product.getCreatedAt()).thenReturn(Instant.now());
		Mockito.when(
			accommodationService.updateAccommodation(
				accommodationId,
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
				RestDocumentationRequestBuilders.patch("/products/accommodations/{id}", accommodationId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer access-token")
					.content("""
						{
						  "name": "%s",
						  "description": "%s",
						  "price": %d,
						  "quantity": %d,
						  "place": "%s",
						  "checkInTime": "%s",
						  "checkOutTime": "%s"
						}
						""".formatted(
						name,
						description,
						price,
						quantity,
						place,
						checkInTime.toString(),
						checkOutTime.toString()
					))
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
	void deleteAccommodation() throws Exception {
		// Given
		Long accommodationId = 1L;

		Mockito.doNothing().when(accommodationService).deleteAccommodation(accommodationId);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/products/accommodations/{id}", accommodationId)
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