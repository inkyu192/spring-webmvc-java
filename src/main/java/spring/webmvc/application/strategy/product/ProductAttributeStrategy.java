package spring.webmvc.application.strategy.product;

import spring.webmvc.application.dto.command.ProductAttributePutCommand;
import spring.webmvc.application.dto.result.ProductAttributeResult;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.ProductCategory;

public interface ProductAttributeStrategy {
	ProductCategory getCategory();

	ProductAttributeResult findByProductId(Long productId);

	ProductAttributeResult create(Product product, ProductAttributePutCommand command);

	ProductAttributeResult update(Long productId, ProductAttributePutCommand command);

	void deleteProduct(Long productId);
}
