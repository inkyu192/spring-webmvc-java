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
import spring.webmvc.presentation.dto.response.ProductResponse;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Test
	@DisplayName("findProducts 은 여러개를 조회한다")
	void findProducts_case1() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		String name = "product";
		List<Product> products = List.of(
			Product.create("product1", "description", 1000, 10, Category.ACCOMMODATION),
			Product.create("product2", "description", 2000, 20, Category.FLIGHT),
			Product.create("product3", "description", 3000, 30, Category.TICKET)
		);
		Page<Product> page = new PageImpl<>(products, pageable, products.size());

		Mockito.when(productRepository.findAll(pageable, name)).thenReturn(page);

		// When
		Page<ProductResponse> response = productService.findProducts(pageable, name);

		// Then
		Assertions.assertThat(response.getTotalElements()).isEqualTo(products.size());
	}
}