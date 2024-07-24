package spring.web.java.convertor.core;

import org.springframework.core.convert.converter.Converter;

import spring.web.java.constant.OrderStatus;

public class OrderStatusConverter implements Converter<String, OrderStatus> {

	@Override
	public OrderStatus convert(String source) {
		return OrderStatus.of(source);
	}
}
