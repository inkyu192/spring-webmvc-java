package spring.webmvc.domain.repository;

import spring.webmvc.domain.model.entity.UserCurationProduct;

public interface UserCurationProductRepository {
	UserCurationProduct findByUserIdAndCurationId(Long userId, Long curationId);
}
