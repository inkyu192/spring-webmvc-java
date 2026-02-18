package spring.webmvc.presentation.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import spring.webmvc.domain.model.enums.ProductStatus;

@Component
public class ProductStatusConverter implements Converter<String, ProductStatus> {
	@Override
	public ProductStatus convert(@NonNull String source) {
		return ProductStatus.of(source);
	}
}
