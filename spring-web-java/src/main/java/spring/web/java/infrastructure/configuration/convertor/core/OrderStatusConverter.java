package spring.web.java.infrastructure.configuration.convertor.core;

import org.springframework.core.convert.converter.Converter;

import spring.web.java.common.constant.OrderStatus;

public class OrderStatusConverter implements Converter<String, OrderStatus> {

	@Override
	public OrderStatus convert(String source) {
		return OrderStatus.of(source);
	}
}
