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

import spring.webmvc.application.service.FlightService;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;
import spring.webmvc.presentation.dto.request.FlightCreateRequest;
import spring.webmvc.presentation.dto.request.FlightUpdateRequest;
import spring.webmvc.presentation.dto.response.FlightResponse;

@WebMvcTest(FlightController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class FlightControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

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
    void saveFlight() throws Exception {
        // Given
        FlightCreateRequest request = new FlightCreateRequest(
            "Flight 123",
            "A great flight",
            1000,
            5,
            "Airline",
            "FL123",
            "ICN",
            "JFK",
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS)
        );
        FlightResponse response = new FlightResponse(
            1L,
            "Flight 123",
            "A great flight",
            1000,
            5,
            Instant.now(),
            "Airline",
            "FL123",
            "ICN",
            "JFK",
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS)
        );

        Mockito.when(flightService.createFlight(request)).thenReturn(response);

        // When & Then
        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/products/flights")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
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
                        PayloadDocumentation.fieldWithPath("name").description("항공편명"),
                        PayloadDocumentation.fieldWithPath("description").description("설명"),
                        PayloadDocumentation.fieldWithPath("price").description("가격"),
                        PayloadDocumentation.fieldWithPath("quantity").description("수량"),
                        PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
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
    void findFlight() throws Exception {
        // Given
        Long requestId = 1L;
        FlightResponse response = new FlightResponse(
            1L,
            "Updated Flight",
            "Updated description",
            2000,
            10,
            Instant.now(),
            "New Airline",
            "FL456",
            "LAX",
            "SFO",
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS)
        );

        Mockito.when(flightService.findFlight(requestId)).thenReturn(response);

        // When & Then
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/products/flights/{id}", requestId)
                    .header("Authorization", "Bearer access-token")
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
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("id").description("아이디"),
                        PayloadDocumentation.fieldWithPath("name").description("항공편명"),
                        PayloadDocumentation.fieldWithPath("description").description("설명"),
                        PayloadDocumentation.fieldWithPath("price").description("가격"),
                        PayloadDocumentation.fieldWithPath("quantity").description("수량"),
                        PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
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
        Long requestId = 1L;
        FlightUpdateRequest request = new FlightUpdateRequest(
            "Flight 123",
            "A great flight",
            1000,
            5,
            "Airline",
            "FL123",
            "ICN",
            "JFK",
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS)
        );
        FlightResponse response = new FlightResponse(
            1L,
            "Updated Flight",
            "Updated description",
            2000,
            10,
            Instant.now(),
            "New Airline",
            "FL456",
            "LAX",
            "SFO",
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS)
        );

        Mockito.when(flightService.updateFlight(requestId, request)).thenReturn(response);

        // When & Then
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/products/flights/{id}", requestId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer access-token")
                    .content(objectMapper.writeValueAsString(request))
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
                        PayloadDocumentation.fieldWithPath("name").description("항공편명"),
                        PayloadDocumentation.fieldWithPath("description").description("설명"),
                        PayloadDocumentation.fieldWithPath("price").description("가격"),
                        PayloadDocumentation.fieldWithPath("quantity").description("수량"),
                        PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
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
        Long requestId = 1L;

        Mockito.doNothing().when(flightService).deleteFlight(requestId);

        // When & Then
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/products/flights/{id}", requestId)
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