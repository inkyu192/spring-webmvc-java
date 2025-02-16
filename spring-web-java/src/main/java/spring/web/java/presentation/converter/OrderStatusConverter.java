package spring.web.java.presentation.converter;

import org.springframework.core.convert.converter.Converter;

import spring.web.java.domain.model.enums.OrderStatus;

public class OrderStatusConverter implements Converter<String, OrderStatus> {

	@Override
	public OrderStatus convert(String source) {
		return OrderStatus.of(source);
	}
}
