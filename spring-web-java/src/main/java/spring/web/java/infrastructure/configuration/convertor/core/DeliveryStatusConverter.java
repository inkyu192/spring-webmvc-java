package spring.web.java.infrastructure.configuration.convertor.core;

import org.springframework.core.convert.converter.Converter;

import spring.web.java.common.constant.DeliveryStatus;

public class DeliveryStatusConverter implements Converter<String, DeliveryStatus> {

	@Override
	public DeliveryStatus convert(String source) {
		return DeliveryStatus.of(source);
	}
}
