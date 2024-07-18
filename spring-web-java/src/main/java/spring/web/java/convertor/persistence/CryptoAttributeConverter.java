package spring.web.java.convertor.persistence;

import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spring.web.java.common.AESCrypto;

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
