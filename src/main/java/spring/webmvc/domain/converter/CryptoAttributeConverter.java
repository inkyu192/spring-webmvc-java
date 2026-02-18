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

	private final CryptoService aesHexCryptoService;

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (attribute == null) {
			return null;
		}

		return aesHexCryptoService.encrypt(attribute);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}

		return aesHexCryptoService.decrypt(dbData);
	}
}
