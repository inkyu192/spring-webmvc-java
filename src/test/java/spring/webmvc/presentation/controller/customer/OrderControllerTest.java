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

import spring.webmvc.application.dto.query.OrderCursorPageQuery;
import spring.webmvc.application.dto.result.OrderDetailResult;
import spring.webmvc.application.dto.result.OrderProductResult;
import spring.webmvc.application.dto.result.OrderSummaryResult;
import spring.webmvc.application.service.OrderService;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.infrastructure.config.ControllerTest;
import spring.webmvc.infrastructure.security.SecurityContextUtil;

@ControllerTest(OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OrderService orderService;

	private OrderSummaryResult orderSummaryResult;
	private OrderDetailResult orderDetailResult;
	private final Long orderId = 1L;
	private final Long userId = 1L;
	private final Long productId = 1L;
	private final Long quantity = 3L;
	private final OrderStatus orderStatus = OrderStatus.ORDER;

	@BeforeEach
	void setUp() {
		OrderProductResult orderProductResult = new OrderProductResult(
			productId,
			"name",
			5000L,
			3L
		);

		orderDetailResult = new OrderDetailResult(
			orderId,
			Instant.now(),
			OrderStatus.ORDER,
			List.of(orderProductResult)
		);

		orderSummaryResult = new OrderSummaryResult(
			orderId,
			Instant.now(),
			OrderStatus.ORDER
		);
	}

	@Test
	void createOrder() throws Exception {
		try (MockedStatic<SecurityContextUtil> mockedStatic = mockStatic(SecurityContextUtil.class)) {
			mockedStatic.when(SecurityContextUtil::getUserId).thenReturn(userId);

			when(orderService.createOrder(any())).thenReturn(orderDetailResult);

			mockMvc.perform(
					RestDocumentationRequestBuilders.post("/customer/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer access-token")
						.content("""
							{
							  "products": [
							    {
							      "id": %d,
							      "quantity": %d
							    }
							  ]
							}
							""".formatted(productId, quantity))
				)
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(
					MockMvcRestDocumentation.document(
						"customer-order-create",
						HeaderDocumentation.requestHeaders(
							HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
						),
						PayloadDocumentation.requestFields(
							PayloadDocumentation.fieldWithPath("products[].id").description("상품아이디"),
							PayloadDocumentation.fieldWithPath("products[].quantity").description("주문수량")
						),
						PayloadDocumentation.responseFields(
							PayloadDocumentation.fieldWithPath("id").description("주문아이디"),
							PayloadDocumentation.fieldWithPath("orderedAt").description("주문일시"),
							PayloadDocumentation.fieldWithPath("status").description("주문상태"),
							PayloadDocumentation.fieldWithPath("products[].id").description("상품아이디"),
							PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
							PayloadDocumentation.fieldWithPath("products[].price").description("주문가격"),
							PayloadDocumentation.fieldWithPath("products[].quantity").description("주문수량")
						)
					)
				);
		}
	}

	@Test
	void findOrders() throws Exception {
		CursorPage<OrderSummaryResult> cursorPage = new CursorPage<>(
			List.of(orderSummaryResult),
			10L,
			false,
			null
		);

		try (MockedStatic<SecurityContextUtil> mockedStatic = mockStatic(SecurityContextUtil.class)) {
			mockedStatic.when(SecurityContextUtil::getUserId).thenReturn(userId);

			when(orderService.findOrdersWithCursorPage(any(OrderCursorPageQuery.class))).thenReturn(cursorPage);

			mockMvc.perform(
					RestDocumentationRequestBuilders.get("/customer/orders")
						.header("Authorization", "Bearer access-token")
						.queryParam("orderStatus", orderStatus.toString())
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(
					MockMvcRestDocumentation.document(
						"customer-order-list",
						HeaderDocumentation.requestHeaders(
							HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
						),
						RequestDocumentation.queryParameters(
							RequestDocumentation.parameterWithName("orderStatus").description("주문상태").optional()
						),
						PayloadDocumentation.responseFields(
							PayloadDocumentation.fieldWithPath("size").description("페이지 크기"),
							PayloadDocumentation.fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
							PayloadDocumentation.fieldWithPath("nextCursorId").description("다음 커서 ID"),
							PayloadDocumentation.fieldWithPath("content[].id").description("주문아이디"),
							PayloadDocumentation.fieldWithPath("content[].orderedAt").description("주문일시"),
							PayloadDocumentation.fieldWithPath("content[].status").description("주문상태")
						)
					)
				);
		}
	}

	@Test
	void findOrder() throws Exception {
		try (MockedStatic<SecurityContextUtil> mockedStatic = mockStatic(SecurityContextUtil.class)) {
			mockedStatic.when(SecurityContextUtil::getUserId).thenReturn(userId);

			when(orderService.findOrderByUser(orderId, userId)).thenReturn(orderDetailResult);

			mockMvc.perform(
					RestDocumentationRequestBuilders.get("/customer/orders/{id}", orderId)
						.header("Authorization", "Bearer access-token")
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(
					MockMvcRestDocumentation.document(
						"customer-order-detail",
						HeaderDocumentation.requestHeaders(
							HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
						),
						RequestDocumentation.pathParameters(
							RequestDocumentation.parameterWithName("id").description("아이디")
						),
						PayloadDocumentation.responseFields(
							PayloadDocumentation.fieldWithPath("id").description("주문아이디"),
							PayloadDocumentation.fieldWithPath("orderedAt").description("주문일시"),
							PayloadDocumentation.fieldWithPath("status").description("주문상태"),
							PayloadDocumentation.fieldWithPath("products[].id").description("상품아이디"),
							PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
							PayloadDocumentation.fieldWithPath("products[].price").description("주문가격"),
							PayloadDocumentation.fieldWithPath("products[].quantity").description("주문수량")
						)
					)
				);
		}
	}

	@Test
	void cancelOrder() throws Exception {
		try (MockedStatic<SecurityContextUtil> mockedStatic = mockStatic(SecurityContextUtil.class)) {
			mockedStatic.when(SecurityContextUtil::getUserId).thenReturn(userId);

			when(orderService.cancelOrder(orderId)).thenReturn(orderDetailResult);

			mockMvc.perform(
					RestDocumentationRequestBuilders.patch("/customer/orders/{id}", orderId)
						.header("Authorization", "Bearer access-token")
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(
					MockMvcRestDocumentation.document(
						"customer-order-cancel",
						HeaderDocumentation.requestHeaders(
							HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
						),
						RequestDocumentation.pathParameters(
							RequestDocumentation.parameterWithName("id").description("아이디")
						),
						PayloadDocumentation.responseFields(
							PayloadDocumentation.fieldWithPath("id").description("주문아이디"),
							PayloadDocumentation.fieldWithPath("orderedAt").description("주문일시"),
							PayloadDocumentation.fieldWithPath("status").description("주문상태"),
							PayloadDocumentation.fieldWithPath("products[].id").description("상품아이디"),
							PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
							PayloadDocumentation.fieldWithPath("products[].price").description("주문가격"),
							PayloadDocumentation.fieldWithPath("products[].quantity").description("주문수량")
						)
					)
				);
		}
	}
}
