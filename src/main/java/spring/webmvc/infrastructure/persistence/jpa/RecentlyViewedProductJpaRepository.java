package spring.webmvc.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.RecentlyViewedProduct;

public interface RecentlyViewedProductJpaRepository extends JpaRepository<RecentlyViewedProduct, Long> {
	Optional<RecentlyViewedProduct> findByUserIdAndProductId(Long userId, Long productId);
}
