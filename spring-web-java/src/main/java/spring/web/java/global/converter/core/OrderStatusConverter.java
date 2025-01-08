package spring.web.java.global.converter.core;

import org.springframework.core.convert.converter.Converter;

import spring.web.java.domain.order.Order;

public class OrderStatusConverter implements Converter<String, Order.Status> {

	@Override
	public Order.Status convert(String source) {
		return Order.Status.of(source);
	}
}
