package spring.webmvc.infrastructure.cache.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.domain.repository.cache.ProductCacheRepository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ProductRedisRepository implements ProductCacheRepository {

	private static final String PRODUCT_STOCK_KEY = "product:%d:stock";
	private static final String PRODUCT_VIEW_COUNT_KEY = "product:%d:view-count";

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public Long getProductStock(Long productId) {
		String key = String.format(PRODUCT_STOCK_KEY, productId);

		try {
			String value = redisTemplate.opsForValue().get(key);

			return value != null ? Long.parseLong(value) : null;
		} catch (Exception e) {
			log.warn("Failed to get product stock for id={}: {}", productId, e.getMessage());
			return null;
		}
	}

	@Override
	public boolean setProductStockIfAbsent(Long productId, Long stock, Duration timeout) {
		String key = String.format(PRODUCT_STOCK_KEY, productId);

		try {
			Boolean result = redisTemplate.opsForValue().setIfAbsent(key, stock.toString(), timeout);

			return result != null && result;
		} catch (Exception e) {
			log.error("Failed to set product stock if absent for id={}: {}", productId, e.getMessage(), e);
			return false;
		}
	}

	@Override
	public Long incrementProductViewCount(Long productId, Long delta) {
		String key = String.format(PRODUCT_VIEW_COUNT_KEY, productId);
		try {
			return redisTemplate.opsForValue().increment(key, delta);
		} catch (Exception e) {
			log.error("Failed to increment product view count for id={}: {}", productId, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Long incrementProductStock(Long productId, Long delta) {
		String key = String.format(PRODUCT_STOCK_KEY, productId);
		try {
			return redisTemplate.opsForValue().increment(key, delta);
		} catch (Exception e) {
			log.error("Failed to increment product stock for id={}: {}", productId, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Long decrementProductStock(Long productId, Long delta) {
		String key = String.format(PRODUCT_STOCK_KEY, productId);
		try {
			return redisTemplate.opsForValue().decrement(key, delta);
		} catch (Exception e) {
			log.error("Failed to decrement product stock for id={}: {}", productId, e.getMessage(), e);
			return null;
		}
	}
}
