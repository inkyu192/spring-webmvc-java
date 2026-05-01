package spring.webmvc.domain.repository;

import java.util.List;

import spring.webmvc.domain.model.entity.Tag;

public interface ProductTagRepository {
	List<Tag> findTagsByProductId(Long productId);
}
