package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.ProductTag;
import spring.webmvc.domain.model.entity.Tag;
import spring.webmvc.domain.repository.ProductTagRepository;
import spring.webmvc.infrastructure.persistence.jpa.ProductTagJpaRepository;

@Component
@RequiredArgsConstructor
public class ProductTagRepositoryAdapter implements ProductTagRepository {

	private final ProductTagJpaRepository jpaRepository;

	@Override
	public List<Tag> findTagsByProductId(Long productId) {
		return jpaRepository.findByProductId(productId).stream()
			.map(ProductTag::getTag)
			.toList();
	}
}
