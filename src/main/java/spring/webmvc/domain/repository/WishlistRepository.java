package spring.webmvc.domain.repository;

import java.util.Optional;
import java.util.Set;

import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Wishlist;

public interface WishlistRepository {
	Optional<Wishlist> findById(Long id);

	Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);

	Set<Long> findProductIdsByUserId(Long userId);

	CursorPage<Wishlist> findAllByUserIdWithCursorPage(Long userId, Long cursorId);

	Wishlist save(Wishlist wishlist);

	void delete(Wishlist wishlist);
}
