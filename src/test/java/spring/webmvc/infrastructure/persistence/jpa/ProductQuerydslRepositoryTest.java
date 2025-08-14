package spring.webmvc.infrastructure.persistence.jpa;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.infrastructure.config.DataJpaTestConfig;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@DataJpaTest
@Import(DataJpaTestConfig.class)
class ProductQuerydslRepositoryTest {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	private EntityManager entityManager;

	private ProductQuerydslRepository productQuerydslRepository;

	private Product product1;
	private Product product2;
	private Product product3;
	private Product product4;

	@BeforeEach
	void setUp() {
		productQuerydslRepository = new ProductQuerydslRepository(jpaQueryFactory);

		product1 = Product.create("product1", "description", 1000, 10, Category.TICKET);
		product2 = Product.create("product2", "description", 2000, 20, Category.FLIGHT);
		product3 = Product.create("product3", "description", 3000, 30, Category.TICKET);
		product4 = Product.create("product4", "description", 1500, 30, Category.FLIGHT);

		entityManager.persist(product1);
		entityManager.persist(product2);
		entityManager.persist(product3);
		entityManager.persist(product4);

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	@DisplayName("findAll: Product 조건 조회 후 반환한다")
	void findAll() {
		// Given
		Long nextCursorId = null;
		int size = 3;
		String name = null;

		// When
		CursorPage<Product> result = productQuerydslRepository.findAll(nextCursorId, size, name);

		// Then
		assertThat(result.content().size()).isEqualTo(size);
		assertThat(result.size()).isEqualTo(size);
		assertThat(result.hasNext()).isTrue();
		assertThat(result.nextCursorId()).isEqualTo(product1.getId());
	}
}
