package spring.web.java.global.converter.persistence;

import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import spring.web.java.global.common.AESCrypto;

@Component
@RequiredArgsConstructor
public class CryptoAttributeConverter implements AttributeConverter<String, String> {

	private final AESCrypto aesCrypto;

	@Override
	public String convertToDatabaseColumn(String s) {
		return aesCrypto.encrypt(s);
	}

	@Override
	public String convertToEntityAttribute(String s) {
		return aesCrypto.decrypt(s);
	}
}
