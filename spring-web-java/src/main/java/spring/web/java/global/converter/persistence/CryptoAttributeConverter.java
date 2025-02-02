package spring.web.java.global.converter.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import spring.web.java.global.common.AESCrypto;

@Converter
public class CryptoAttributeConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String s) {
		return AESCrypto.encrypt(s);
	}

	@Override
	public String convertToEntityAttribute(String s) {
		return AESCrypto.decrypt(s);
	}
}
