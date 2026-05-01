package spring.webmvc.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.ProductTag;

public interface ProductTagJpaRepository extends JpaRepository<ProductTag, Long> {
	List<ProductTag> findByProductId(Long productId);
}
