package spring.webmvc.domain.converter;

import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import spring.webmvc.infrastructure.crypto.CryptoService;

@Component
@Converter
@RequiredArgsConstructor
public class CryptoAttributeConverter implements AttributeConverter<String, String> {

	private final CryptoService hexAESCryptoService;

	@Override
	public String convertToDatabaseColumn(String s) {
		return hexAESCryptoService.encrypt(s);
	}

	@Override
	public String convertToEntityAttribute(String s) {
		return hexAESCryptoService.decrypt(s);
	}
}
