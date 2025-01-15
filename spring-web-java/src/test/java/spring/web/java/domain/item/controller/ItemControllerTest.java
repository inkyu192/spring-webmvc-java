package spring.web.java.domain.item.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.web.java.domain.item.dto.ItemResponse;
import spring.web.java.domain.item.serivce.ItemService;

@WithMockUser
@WebMvcTest(ItemController.class)
class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemService;

	@Test
	void findItems() throws Exception {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		String name = "item";

		List<ItemResponse> itemResponse = List.of(
			new ItemResponse(1L, "item1", "description", 1000, 10),
			new ItemResponse(2L, "item2", "description", 2000, 20),
			new ItemResponse(3L, "item3", "description", 3000, 30)
		);
		Page<ItemResponse> page = new PageImpl<>(itemResponse, pageable, itemResponse.size());

		Mockito.when(itemService.findItems(pageable, name)).thenReturn(page);

		// When & Then
		mockMvc.perform(MockMvcRequestBuilders.get("/item")
				.param("page", "0")
				.param("size", "10")
				.param("name", name))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(result -> System.out.println("result.getResponse() = " + result.getResponse().getContentAsString()));
	}
}
