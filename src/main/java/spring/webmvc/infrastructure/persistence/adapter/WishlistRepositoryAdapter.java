package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Wishlist;
import spring.webmvc.domain.repository.WishlistRepository;
import spring.webmvc.infrastructure.persistence.jpa.WishlistJpaRepository;
import spring.webmvc.infrastructure.persistence.jpa.WishlistQuerydslRepository;

@Component
@RequiredArgsConstructor
public class WishlistRepositoryAdapter implements WishlistRepository {

	private final WishlistJpaRepository jpaRepository;
	private final WishlistQuerydslRepository querydslRepository;

	@Override
	public Optional<Wishlist> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId) {
		return jpaRepository.findByUserIdAndProductId(userId, productId);
	}

	@Override
	public Set<Long> findProductIdsByUserId(Long userId) {
		return querydslRepository.findProductIdsByUserId(userId);
	}

	@Override
	public CursorPage<Wishlist> findAllByUserIdWithCursorPage(Long userId, Long cursorId) {
		return querydslRepository.findAllByUserIdWithCursorPage(userId, cursorId);
	}

	@Override
	public Wishlist save(Wishlist wishlist) {
		return jpaRepository.save(wishlist);
	}

	@Override
	public void delete(Wishlist wishlist) {
		jpaRepository.delete(wishlist);
	}
}
