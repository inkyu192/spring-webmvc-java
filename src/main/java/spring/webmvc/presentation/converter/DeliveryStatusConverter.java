package spring.webmvc.presentation.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import spring.webmvc.domain.model.enums.DeliveryStatus;

@Component
public class DeliveryStatusConverter implements Converter<String, DeliveryStatus> {

	@Override
	public DeliveryStatus convert(String source) {
		return DeliveryStatus.of(source);
	}
}
