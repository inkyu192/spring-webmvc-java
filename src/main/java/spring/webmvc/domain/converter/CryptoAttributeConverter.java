package spring.webmvc.domain.converter;

import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import spring.webmvc.infrastructure.util.crypto.CryptoUtil;

@Component
@Converter
@RequiredArgsConstructor
public class CryptoAttributeConverter implements AttributeConverter<String, String> {

	private final CryptoUtil hexAESCryptoUtil;

	@Override
	public String convertToDatabaseColumn(String s) {
		return hexAESCryptoUtil.encrypt(s);
	}

	@Override
	public String convertToEntityAttribute(String s) {
		return hexAESCryptoUtil.decrypt(s);
	}
}
