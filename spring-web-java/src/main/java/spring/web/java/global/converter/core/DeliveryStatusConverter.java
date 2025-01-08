package spring.web.java.global.converter.core;

import org.springframework.core.convert.converter.Converter;

import spring.web.java.domain.order.Delivery;

public class DeliveryStatusConverter implements Converter<String, Delivery.Status> {

	@Override
	public Delivery.Status convert(String source) {
		return Delivery.Status.of(source);
	}
}
