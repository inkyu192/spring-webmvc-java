package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.RecentlyViewedProduct;
import spring.webmvc.domain.repository.RecentlyViewedProductRepository;
import spring.webmvc.infrastructure.persistence.jpa.RecentlyViewedProductJpaRepository;
import spring.webmvc.infrastructure.persistence.jpa.RecentlyViewedProductQuerydslRepository;

@Component
@RequiredArgsConstructor
public class RecentlyViewedProductRepositoryAdapter implements RecentlyViewedProductRepository {

	private static final int DEFAULT_DAYS = 30;

	private final RecentlyViewedProductJpaRepository jpaRepository;
	private final RecentlyViewedProductQuerydslRepository querydslRepository;

	@Override
	public Optional<RecentlyViewedProduct> findByUserIdAndProductId(Long userId, Long productId) {
		return jpaRepository.findByUserIdAndProductId(userId, productId);
	}

	@Override
	public Set<Long> findProductIdsByUserIdWithinDays(Long userId, int days) {
		return querydslRepository.findProductIdsByUserIdWithinDays(userId, days);
	}

	@Override
	public Set<Long> findProductIdsByUserIdWithinDays(Long userId) {
		return findProductIdsByUserIdWithinDays(userId, DEFAULT_DAYS);
	}

	@Override
	public CursorPage<RecentlyViewedProduct> findAllByUserIdWithCursorPage(Long userId, Long cursorId, int days) {
		return querydslRepository.findAllByUserIdWithCursorPage(userId, cursorId, days);
	}

	@Override
	public CursorPage<RecentlyViewedProduct> findAllByUserIdWithCursorPage(Long userId, Long cursorId) {
		return findAllByUserIdWithCursorPage(userId, cursorId, DEFAULT_DAYS);
	}

	@Override
	public RecentlyViewedProduct save(RecentlyViewedProduct recentlyViewedProduct) {
		return jpaRepository.save(recentlyViewedProduct);
	}
}
