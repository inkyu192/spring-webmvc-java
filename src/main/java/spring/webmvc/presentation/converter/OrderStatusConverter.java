package spring.webmvc.presentation.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import spring.webmvc.domain.model.enums.OrderStatus;

@Component
public class OrderStatusConverter implements Converter<String, OrderStatus> {
	@Override
	public OrderStatus convert(@NonNull String source) {
		return OrderStatus.of(source);
	}
}
