package spring.webmvc.application.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.webmvc.application.dto.result.ProductSummaryResult;
import spring.webmvc.application.dto.result.WishlistResult;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.domain.model.entity.Wishlist;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.domain.repository.RecentlyViewedProductRepository;
import spring.webmvc.domain.repository.UserProductBadgeRepository;
import spring.webmvc.domain.repository.UserRepository;
import spring.webmvc.domain.repository.WishlistRepository;
import spring.webmvc.infrastructure.exception.DuplicateEntityException;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;

@Service
@Transactional(readOnly = true)
public class WishlistService {

	private final WishlistRepository wishlistRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final UserProductBadgeRepository userProductBadgeRepository;
	private final RecentlyViewedProductRepository recentlyViewedProductRepository;

	public WishlistService(
		WishlistRepository wishlistRepository,
		UserRepository userRepository,
		ProductRepository productRepository,
		UserProductBadgeRepository userProductBadgeRepository,
		RecentlyViewedProductRepository recentlyViewedProductRepository
	) {
		this.wishlistRepository = wishlistRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.userProductBadgeRepository = userProductBadgeRepository;
		this.recentlyViewedProductRepository = recentlyViewedProductRepository;
	}

	public CursorPage<WishlistResult> findWishlists(Long userId, Long cursorId) {
		CursorPage<Wishlist> page = wishlistRepository.findAllByUserIdWithCursorPage(userId, cursorId);

		List<Long> productIds = page.content().stream()
			.map(w -> w.getProduct().getId())
			.toList();

		Map<Long, UserProductBadge> badgeMap = userProductBadgeRepository
			.findByUserIdAndProductIds(userId, productIds).stream()
			.collect(Collectors.toMap(
				b -> Long.parseLong(b.getSk().replace("PRODUCT#", "")),
				Function.identity()
			));

		Set<Long> recentlyViewedIds = recentlyViewedProductRepository.findProductIdsByUserIdWithinDays(userId);

		return page.map(wishlist -> {
			Product product = wishlist.getProduct();
			ProductSummaryResult productResult = ProductSummaryResult.of(
				product,
				badgeMap.get(product.getId()),
				recentlyViewedIds.contains(product.getId()),
				true
			);
			return WishlistResult.of(wishlist, productResult);
		});
	}

	@Transactional
	public void addWishlist(Long userId, Long productId) {
		wishlistRepository.findByUserIdAndProductId(userId, productId)
			.ifPresent(w -> {
				throw new DuplicateEntityException(Wishlist.class, "userId=" + userId + ", productId=" + productId);
			});

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundEntityException(User.class, userId));

		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new NotFoundEntityException(Product.class, productId));

		Wishlist wishlist = Wishlist.create(user, product);
		wishlistRepository.save(wishlist);
	}

	@Transactional
	public void removeWishlist(Long userId, Long wishlistId) {
		Wishlist wishlist = wishlistRepository.findById(wishlistId)
			.orElseThrow(() -> new NotFoundEntityException(Wishlist.class, wishlistId));

		if (!wishlist.getUser().getId().equals(userId)) {
			throw new NotFoundEntityException(Wishlist.class, wishlistId);
		}

		wishlistRepository.delete(wishlist);
	}
}
