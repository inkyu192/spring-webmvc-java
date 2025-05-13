package spring.webmvc.application.strategy;

import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.domain.model.enums.Category;

public interface ProductStrategy {
	boolean supports(Category category);

	ProductResult findByProductId(Long productId);

	ProductResult createProduct(ProductCreateCommand productCreateCommand);

	ProductResult updateProduct(Long productId, ProductUpdateCommand productUpdateCommand);
}
