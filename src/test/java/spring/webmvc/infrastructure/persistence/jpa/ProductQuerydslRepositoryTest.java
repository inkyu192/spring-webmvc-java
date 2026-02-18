package spring.webmvc.infrastructure.persistence.jpa;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import spring.webmvc.application.dto.query.ProductCursorPageQuery;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;
import spring.webmvc.infrastructure.config.RepositoryTest;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@RepositoryTest
class ProductQuerydslRepositoryTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private ProductQuerydslRepository productQuerydslRepository;

	private Accommodation product1;
	private Accommodation product2;
	private Accommodation product3;
	private Accommodation product4;

	@BeforeEach
	void setUp() {
		productQuerydslRepository = new ProductQuerydslRepository(jpaQueryFactory);

		ProductExposureAttribute exposureAttribute = new ProductExposureAttribute(
			false, false, false, false
		);

		Product p1 = Product.create(
			ProductCategory.ACCOMMODATION,
			"product1",
			"description",
			1000L,
			10L,
			exposureAttribute
		);
		product1 = Accommodation.create(
			p1,
			"place1",
			Instant.now(),
			Instant.now().plusSeconds(86400)
		);

		Product p2 = Product.create(
			ProductCategory.ACCOMMODATION,
			"product2",
			"description",
			2000L,
			20L,
			exposureAttribute
		);
		product2 = Accommodation.create(
			p2,
			"place2",
			Instant.now(),
			Instant.now().plusSeconds(86400)
		);

		Product p3 = Product.create(
			ProductCategory.ACCOMMODATION,
			"product3",
			"description",
			3000L,
			30L,
			exposureAttribute
		);
		product3 = Accommodation.create(
			p3,
			"place3",
			Instant.now(),
			Instant.now().plusSeconds(86400)
		);

		Product p4 = Product.create(
			ProductCategory.ACCOMMODATION,
			"product4",
			"description",
			1500L,
			30L,
			exposureAttribute
		);
		product4 = Accommodation.create(
			p4,
			"place4",
			Instant.now(),
			Instant.now().plusSeconds(86400)
		);

		entityManager.persist(p1);
		entityManager.persist(product1);
		entityManager.persist(p2);
		entityManager.persist(product2);
		entityManager.persist(p3);
		entityManager.persist(product3);
		entityManager.persist(p4);
		entityManager.persist(product4);

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	@DisplayName("findAllWithCursorPage: Product 조건 조회 후 반환한다")
	void findAllWithCursorPage() {
		ProductCursorPageQuery query = new ProductCursorPageQuery(
			null,
			null,
			null
		);

		CursorPage<Product> result = productQuerydslRepository.findAllWithCursorPage(query);

		assertThat(result.content().size()).isEqualTo(4);
		assertThat(result.hasNext()).isFalse();
		assertThat(result.nextCursorId()).isNull();
	}
}
