package spring.webmvc.domain.repository.cache;

import java.time.Duration;

public interface ProductCacheRepository {
	Long getProductStock(Long productId);

	boolean setProductStockIfAbsent(Long productId, Long stock, Duration timeout);

	Long incrementProductViewCount(Long productId, Long delta);

	Long incrementProductStock(Long productId, Long delta);

	Long decrementProductStock(Long productId, Long delta);
}
