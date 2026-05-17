package spring.webmvc.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Wishlist;

public interface WishlistJpaRepository extends JpaRepository<Wishlist, Long> {
	Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);
}
