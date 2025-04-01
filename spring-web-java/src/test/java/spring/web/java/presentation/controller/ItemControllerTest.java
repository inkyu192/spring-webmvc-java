package spring.web.java.presentation.controller;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.web.java.application.service.ItemService;
import spring.web.java.domain.model.enums.Category;
import spring.web.java.infrastructure.config.security.JwtTokenProvider;
import spring.web.java.infrastructure.util.ResponseWriter;
import spring.web.java.presentation.dto.request.ItemSaveRequest;
import spring.web.java.presentation.dto.response.ItemResponse;

@WebMvcTest(ItemController.class)
@ExtendWith(RestDocumentationExtension.class)
class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemService;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@MockBean
	private ResponseWriter responseWriter;

	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation, WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
			.build();
	}

	private final String authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJtZW1iZXJJZCI6MSwicGVybWlzc2lvbnMiOlsiSVRFTV9SRUFERVIiLCJJVEVNX1dSSVRFUiJdLCJpYXQiOjE3NDM0ODUzMzYsImV4cCI6MTc0MzQ4NTkzNn0.tu5bcIGsTtmXy0RmqvKa3H-rWKBn3rB1i1RjPzlkTVs";

	@Test
	void saveItemTest() throws Exception {
		ItemSaveRequest request = new ItemSaveRequest("상품명", "설명", 1000, 5, Category.ROLE_BOOK);
		ItemResponse itemResponse = new ItemResponse(1L, "상품명", "설명", 1000, 10, Instant.now());

		Mockito.when(itemService.saveItem(request)).thenReturn(itemResponse);

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/items")
					.contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString(request))
					.header("Authorization", authorization)
			)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andDo(
				MockMvcRestDocumentation.document("item-create",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("name").description("상품명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("name").description("상품명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시")
					)
				)
			);
	}

	@Test
	void findItem() throws Exception {
		Long requestId = 1L;
		ItemResponse itemResponse = new ItemResponse(1L, "item1", "description", 1000, 10, Instant.now());

		Mockito.when(itemService.findItem(requestId)).thenReturn(itemResponse);

		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/items/{id}", requestId)
					.header("Authorization", authorization)
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("item-get",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("name").description("상품명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시")
					)
				)
			);
	}

	@Test
	void findItems() throws Exception {
		Pageable pageable = PageRequest.of(0, 10);
		String name = "item";

		List<ItemResponse> itemResponse = List.of(
			new ItemResponse(1L, "item1", "description", 1000, 10, Instant.now()),
			new ItemResponse(2L, "item2", "description", 2000, 20, Instant.now()),
			new ItemResponse(3L, "item3", "description", 3000, 30, Instant.now())
		);
		Page<ItemResponse> page = new PageImpl<>(itemResponse, pageable, itemResponse.size());

		Mockito.when(itemService.findItems(pageable, name)).thenReturn(page);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/items")
					.header("Authorization", authorization)
					.param("page", "0")
					.param("size", "10")
					.param("name", name)
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("item-list",
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
	void updateItem() throws Exception {
		Long requestId = 1L;

		ItemSaveRequest request = new ItemSaveRequest("상품명", "설명", 1000, 5, Category.ROLE_BOOK);
		ItemResponse itemResponse = new ItemResponse(1L, "상품명", "설명", 1000, 10, Instant.now());

		Mockito.when(itemService.putItem(requestId, request)).thenReturn(Pair.of(false, itemResponse));

		mockMvc.perform(
				RestDocumentationRequestBuilders.put("/items/{id}", requestId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", authorization)
					.content(new ObjectMapper().writeValueAsString(request))
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("item-update",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.pathParameters(
						RequestDocumentation.parameterWithName("id").description("아이디")
					),
					PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("name").description("상품명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("category").description("카테고리")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("id").description("아이디"),
						PayloadDocumentation.fieldWithPath("name").description("상품명"),
						PayloadDocumentation.fieldWithPath("description").description("설명"),
						PayloadDocumentation.fieldWithPath("price").description("가격"),
						PayloadDocumentation.fieldWithPath("quantity").description("수량"),
						PayloadDocumentation.fieldWithPath("createdAt").description("생성일시")
					)
				)
			);
	}

	@Test
	void deleteItem() throws Exception {
		Long requestId = 1L;

		Mockito.doNothing().when(itemService).deleteItem(requestId);

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/items/{id}", requestId)
					.header("Authorization", authorization)
			)
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(
				MockMvcRestDocumentation.document("item-delete",
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
