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

import spring.webmvc.application.dto.query.OrderOffsetPageQuery;
import spring.webmvc.application.dto.result.OrderDetailResult;
import spring.webmvc.application.dto.result.OrderProductResult;
import spring.webmvc.application.dto.result.OrderSummaryResult;
import spring.webmvc.application.service.OrderService;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.infrastructure.config.ControllerTest;

@ControllerTest(OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OrderService orderService;

	private OrderSummaryResult orderSummaryResult;
	private OrderDetailResult orderDetailResult;
	private final Long orderId = 1L;

	@BeforeEach
	void setUp() {
		Instant now = Instant.now();

		orderSummaryResult = new OrderSummaryResult(
			orderId,
			now,
			OrderStatus.ORDER
		);

		List<OrderProductResult> products = List.of(
			new OrderProductResult(1L, "제주도 호텔", 100000L, 1L),
			new OrderProductResult(2L, "부산 교통편", 50000L, 1L)
		);

		orderDetailResult = new OrderDetailResult(
			orderId,
			now,
			OrderStatus.ORDER,
			products
		);
	}

	@Test
	void findOrders() throws Exception {
		Page<OrderSummaryResult> page = new PageImpl<>(
			List.of(orderSummaryResult),
			PageRequest.of(0, 20),
			1
		);

		when(orderService.findOrdersWithOffsetPage(any(OrderOffsetPageQuery.class))).thenReturn(page);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/partner/orders")
					.header("Authorization", "Bearer access-token")
					.queryParam("page", "0")
					.queryParam("size", "20")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-order-list",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("page").description("페이지 번호").optional(),
						RequestDocumentation.parameterWithName("size").description("페이지 크기").optional()
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("page").description("현재 페이지 번호"),
						PayloadDocumentation.fieldWithPath("size").description("페이지 크기"),
						PayloadDocumentation.fieldWithPath("totalElements").description("전체 요소 수"),
						PayloadDocumentation.fieldWithPath("totalPages").description("전체 페이지 수"),
						PayloadDocumentation.fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("hasPrevious").description("이전 페이지 존재 여부"),
						PayloadDocumentation.fieldWithPath("content[].id").description("주문 ID"),
						PayloadDocumentation.fieldWithPath("content[].orderedAt").description("주문일시"),
						PayloadDocumentation.fieldWithPath("content[].status").description("주문상태")
					)
				)
			);
	}

	@Test
	void findOrder() throws Exception {
		when(orderService.findOrder(orderId)).thenReturn(orderDetailResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/partner/orders/{id}", orderId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-order-detail",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("주문 ID")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("주문 ID"),
						PayloadDocumentation.fieldWithPath("orderedAt").description("주문일시"),
						PayloadDocumentation.fieldWithPath("status").description("주문상태"),
						PayloadDocumentation.fieldWithPath("products[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("products[].price").description("상품가격"),
						PayloadDocumentation.fieldWithPath("products[].quantity").description("주문수량")
					)
				)
			);
	}

	@Test
	void updateOrderStatus() throws Exception {
		when(orderService.updateOrderStatus(any())).thenReturn(orderDetailResult);

		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/partner/orders/{id}", orderId)
					.header("Authorization", "Bearer access-token")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
						{
						  "orderStatus": "CONFIRM"
						}
						""")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"partner-order-update-status",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("주문 ID")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("orderStatus").description("변경할 주문상태")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("주문 ID"),
						PayloadDocumentation.fieldWithPath("orderedAt").description("주문일시"),
						PayloadDocumentation.fieldWithPath("status").description("주문상태"),
						PayloadDocumentation.fieldWithPath("products[].id").description("상품 ID"),
						PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("products[].price").description("상품가격"),
						PayloadDocumentation.fieldWithPath("products[].quantity").description("주문수량")
					)
				)
			);
	}
}
