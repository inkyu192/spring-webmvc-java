package spring.webmvc.application.event.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.event.ProductViewEvent;
import spring.webmvc.domain.repository.cache.ProductCacheRepository;

@Component
@RequiredArgsConstructor
public class ProductEventListener {

	private final ProductCacheRepository productCacheRepository;

	@Async
	@TransactionalEventListener
	public void handleProductViewEvent(ProductViewEvent event) {
		productCacheRepository.incrementProductViewCount(event.productId(), 1L);
	}
}
