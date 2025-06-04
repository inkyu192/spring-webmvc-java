package spring.webmvc.infrastructure.persistence.querydsl;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.infrastructure.config.DataJpaTestConfig;
import spring.webmvc.infrastructure.persistence.jpa.ProductJpaRepository;

@DataJpaTest
@Import(DataJpaTestConfig.class)
class ProductQuerydslRepositoryTest {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	private ProductQuerydslRepository productQuerydslRepository;

	@BeforeEach
	void setUp() {
		productQuerydslRepository = new ProductQuerydslRepository(jpaQueryFactory);
	}

	@Test
	@DisplayName("findAll: Product 조건 조회 후 반환한다")
	void findAll() {
		// Given
		List<Product> request = List.of(
			Product.create("product1", "description", 1000, 10, Category.TICKET),
			Product.create("product2", "description", 2000, 20, Category.FLIGHT),
			Product.create("product3", "description", 3000, 30, Category.TICKET),
			Product.create("product4", "description", 1500, 30, Category.FLIGHT),
			Product.create("fake", "description", 2500, 30, Category.ACCOMMODATION)
		);

		productJpaRepository.saveAll(request);

		Pageable pageable = PageRequest.of(0, 3);
		String name = "product";

		// When
		Page<Product> result = productQuerydslRepository.findAll(pageable, name);

		// Then
		Assertions.assertThat(result.getNumber()).isEqualTo(pageable.getPageNumber());
		Assertions.assertThat(result.getSize()).isEqualTo(pageable.getPageSize());
		Assertions.assertThat(result.getTotalElements())
			.isEqualTo(request.stream().map(Product::getName).filter(s -> s.contains(name)).count());
	}
}
