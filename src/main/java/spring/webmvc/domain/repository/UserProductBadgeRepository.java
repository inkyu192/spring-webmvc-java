package spring.webmvc.domain.repository;

import java.util.List;

import spring.webmvc.domain.model.entity.UserProductBadge;

public interface UserProductBadgeRepository {
	UserProductBadge findByUserIdAndProductId(Long userId, Long productId);

	List<UserProductBadge> findByUserIdAndProductIds(Long userId, List<Long> productIds);
}
