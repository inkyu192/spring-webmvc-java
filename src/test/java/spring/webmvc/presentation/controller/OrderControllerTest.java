package spring.webmvc.presentation.controller;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
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

import spring.webmvc.application.service.OrderService;
import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.entity.OrderProduct;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;
import spring.webmvc.presentation.dto.request.OrderCreateRequest;
import spring.webmvc.presentation.dto.request.OrderProductCreateRequest;

@WebMvcTest(OrderController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class OrderControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private OrderService orderService;

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
	void createOrder() throws Exception {
		OrderCreateRequest request = new OrderCreateRequest(
			List.of(new OrderProductCreateRequest(1L, 3))
		);
		List<Pair<Long, Integer>> productQuantities = List.of(Pair.of(1L, 3));

		Order order = Mockito.mock(Order.class);
		Product product = Mockito.mock(Product.class);
		OrderProduct orderProduct = Mockito.mock(OrderProduct.class);

		Mockito.when(order.getId()).thenReturn(1L);
		Mockito.when(order.getOrderedAt()).thenReturn(Instant.now());
		Mockito.when(order.getStatus()).thenReturn(OrderStatus.ORDER);
		Mockito.when(product.getName()).thenReturn("name");
		Mockito.when(orderProduct.getQuantity()).thenReturn(3);
		Mockito.when(orderProduct.getOrderPrice()).thenReturn(5000);
		Mockito.when(orderProduct.getProduct()).thenReturn(product);
		Mockito.when(order.getOrderProducts()).thenReturn(List.of(orderProduct));
		Mockito.when(orderService.createOrder(productQuantities)).thenReturn(order);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/orders")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer access-token")
					.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document("order-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("products[].productId").description("상품아이디"),
						PayloadDocumentation.fieldWithPath("products[].quantity").description("주문수량")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("주문아이디"),
						PayloadDocumentation.fieldWithPath("orderedAt").description("주문일시"),
						PayloadDocumentation.fieldWithPath("status").description("주문상태"),
						PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("products[].price").description("주문가격"),
						PayloadDocumentation.fieldWithPath("products[].quantity").description("주문수량")
					)
				)
			);
	}

	@Test
	void findOrders() throws Exception {
		Pageable pageable = PageRequest.of(0, 10);
		Long memberId = 1L;
		OrderStatus orderStatus = OrderStatus.ORDER;

		Order order = Mockito.mock(Order.class);
		Product product = Mockito.mock(Product.class);
		OrderProduct orderProduct = Mockito.mock(OrderProduct.class);

		List<Order> response = List.of(order);
		Page<Order> page = new PageImpl<>(response, pageable, response.size());

		Mockito.when(order.getId()).thenReturn(1L);
		Mockito.when(order.getOrderedAt()).thenReturn(Instant.now());
		Mockito.when(order.getStatus()).thenReturn(OrderStatus.ORDER);
		Mockito.when(product.getName()).thenReturn("name");
		Mockito.when(orderProduct.getQuantity()).thenReturn(3);
		Mockito.when(orderProduct.getOrderPrice()).thenReturn(5000);
		Mockito.when(orderProduct.getProduct()).thenReturn(product);
		Mockito.when(order.getOrderProducts()).thenReturn(List.of(orderProduct));
		Mockito.when(orderService.findOrders(pageable, orderStatus)).thenReturn(page);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/orders")
					.header("Authorization", "Bearer access-token")
					.param("page", String.valueOf(pageable.getPageNumber()))
					.param("size", String.valueOf(pageable.getPageSize()))
					.param("memberId", String.valueOf(memberId))
					.param("orderStatus", String.valueOf(orderStatus))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("order-list",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.queryParameters(
						RequestDocumentation.parameterWithName("page").description("페이지 번호").optional(),
						RequestDocumentation.parameterWithName("size").description("페이지 크기").optional(),
						RequestDocumentation.parameterWithName("memberId").description("회원아아디").optional(),
						RequestDocumentation.parameterWithName("orderStatus").description("주문상태").optional()
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("content[].id").description("주문아이디"),
						PayloadDocumentation.fieldWithPath("content[].orderedAt").description("주문일시"),
						PayloadDocumentation.fieldWithPath("content[].status").description("주문상태"),
						PayloadDocumentation.fieldWithPath("content[].products[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("content[].products[].price").description("주문가격"),
						PayloadDocumentation.fieldWithPath("content[].products[].quantity").description("주문수량"),

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
	void findOrder() throws Exception {
		Long orderId = 1L;

		Order order = Mockito.mock(Order.class);
		Product product = Mockito.mock(Product.class);
		OrderProduct orderProduct = Mockito.mock(OrderProduct.class);

		Mockito.when(order.getId()).thenReturn(1L);
		Mockito.when(order.getOrderedAt()).thenReturn(Instant.now());
		Mockito.when(order.getStatus()).thenReturn(OrderStatus.ORDER);
		Mockito.when(product.getName()).thenReturn("name");
		Mockito.when(orderProduct.getQuantity()).thenReturn(3);
		Mockito.when(orderProduct.getOrderPrice()).thenReturn(5000);
		Mockito.when(orderProduct.getProduct()).thenReturn(product);
		Mockito.when(order.getOrderProducts()).thenReturn(List.of(orderProduct));
		Mockito.when(orderService.findOrder(orderId)).thenReturn(order);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/orders/{id}", orderId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("order-get",
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
						PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("products[].price").description("주문가격"),
						PayloadDocumentation.fieldWithPath("products[].quantity").description("주문수량")
					)
				)
			);
	}

	@Test
	void cancelOder() throws Exception {
		Long orderId = 1L;

		Order order = Mockito.mock(Order.class);
		Product product = Mockito.mock(Product.class);
		OrderProduct orderProduct = Mockito.mock(OrderProduct.class);

		Mockito.when(order.getId()).thenReturn(1L);
		Mockito.when(order.getOrderedAt()).thenReturn(Instant.now());
		Mockito.when(order.getStatus()).thenReturn(OrderStatus.ORDER);
		Mockito.when(product.getName()).thenReturn("name");
		Mockito.when(orderProduct.getQuantity()).thenReturn(3);
		Mockito.when(orderProduct.getOrderPrice()).thenReturn(5000);
		Mockito.when(orderProduct.getProduct()).thenReturn(product);
		Mockito.when(order.getOrderProducts()).thenReturn(List.of(orderProduct));
		Mockito.when(orderService.cancelOrder(orderId)).thenReturn(order);

		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/orders/{id}", orderId)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("order-cancel",
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
						PayloadDocumentation.fieldWithPath("products[].name").description("상품명"),
						PayloadDocumentation.fieldWithPath("products[].price").description("주문가격"),
						PayloadDocumentation.fieldWithPath("products[].quantity").description("주문수량")
					)
				)
			);
	}
}