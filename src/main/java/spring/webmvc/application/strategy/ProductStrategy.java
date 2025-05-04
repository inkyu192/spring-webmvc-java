package spring.webmvc.application.strategy;

import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.domain.model.enums.Category;

public interface ProductStrategy {
	boolean supports(Category category);

	ProductResult findByProductId(Long productId);
}
