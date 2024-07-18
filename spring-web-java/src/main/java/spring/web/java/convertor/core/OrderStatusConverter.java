package spring.web.java.convertor.core;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import spring.web.java.constant.OrderStatus;

@Component
public class OrderStatusConverter implements Converter<String, OrderStatus> {

	@Override
	public OrderStatus convert(String source) {
		return OrderStatus.of(source);
	}
}
