package spring.webmvc.application.event.listener;

import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.event.RecentlyViewedEvent;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.entity.RecentlyViewedProduct;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.domain.repository.RecentlyViewedProductRepository;
import spring.webmvc.domain.repository.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecentlyViewedEventListener {

	private final RecentlyViewedProductRepository recentlyViewedProductRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	@Async
	@TransactionalEventListener
	@Transactional
	public void handleRecentlyViewed(RecentlyViewedEvent event) {
		Optional<RecentlyViewedProduct> existing = recentlyViewedProductRepository
			.findByUserIdAndProductId(event.userId(), event.productId());

		if (existing.isPresent()) {
			existing.get().updateViewedAt();
		} else {
			User user = userRepository.findById(event.userId()).orElse(null);
			Product product = productRepository.findById(event.productId()).orElse(null);

			if (user == null || product == null) {
				log.warn("User or Product not found for RecentlyViewedEvent: userId={}, productId={}",
					event.userId(), event.productId());
				return;
			}

			RecentlyViewedProduct newRecord = RecentlyViewedProduct.create(user, product);
			recentlyViewedProductRepository.save(newRecord);
		}
	}
}
