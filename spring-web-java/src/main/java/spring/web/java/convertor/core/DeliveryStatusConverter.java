package spring.web.java.convertor.core;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import spring.web.java.constant.DeliveryStatus;

@Component
public class DeliveryStatusConverter implements Converter<String, DeliveryStatus> {

	@Override
	public DeliveryStatus convert(String source) {
		return DeliveryStatus.of(source);
	}
}
