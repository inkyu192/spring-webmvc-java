package spring.webmvc.domain.repository;

import java.util.Optional;
import java.util.Set;

import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.RecentlyViewedProduct;

public interface RecentlyViewedProductRepository {
	Optional<RecentlyViewedProduct> findByUserIdAndProductId(Long userId, Long productId);

	Set<Long> findProductIdsByUserIdWithinDays(Long userId, int days);

	Set<Long> findProductIdsByUserIdWithinDays(Long userId);

	CursorPage<RecentlyViewedProduct> findAllByUserIdWithCursorPage(Long userId, Long cursorId, int days);

	CursorPage<RecentlyViewedProduct> findAllByUserIdWithCursorPage(Long userId, Long cursorId);

	RecentlyViewedProduct save(RecentlyViewedProduct recentlyViewedProduct);
}
