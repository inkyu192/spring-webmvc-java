package spring.webmvc.application.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Test
	@DisplayName("findProducts: Product 조회 후 반환한다")
	void findProducts() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		String name = "name";
		List<Product> products = List.of(
			Product.create("name1", "description", 1000, 10, Category.ACCOMMODATION),
			Product.create("name2", "description", 2000, 20, Category.FLIGHT),
			Product.create("name3", "description", 3000, 30, Category.TICKET)
		);
		Page<Product> page = new PageImpl<>(products, pageable, products.size());

		Mockito.when(productRepository.findAll(pageable, name)).thenReturn(page);

		// When
		Page<Product> result = productService.findProducts(pageable, name);

		// Then
		Assertions.assertThat(result.getContent()).hasSize(products.size());
	}
}
