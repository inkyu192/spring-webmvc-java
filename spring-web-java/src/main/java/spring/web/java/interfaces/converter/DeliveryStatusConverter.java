package spring.web.java.interfaces.converter;

import org.springframework.core.convert.converter.Converter;

import spring.web.java.domain.model.enums.DeliveryStatus;

public class DeliveryStatusConverter implements Converter<String, DeliveryStatus> {

	@Override
	public DeliveryStatus convert(String source) {
		return DeliveryStatus.of(source);
	}
}
