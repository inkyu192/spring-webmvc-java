package spring.web.kotlin.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.stereotype.Component
import spring.web.kotlin.infrastructure.util.crypto.CryptoUtil

@Component
@Converter
class CryptoAttributeConverter(
    private val hexAESCryptoUtil: CryptoUtil
) : AttributeConverter<String?, String?> {
    override fun convertToDatabaseColumn(attribute: String?) = attribute?.let { hexAESCryptoUtil.encrypt(it) }
    override fun convertToEntityAttribute(dbData: String?) = dbData?.let { hexAESCryptoUtil.decrypt(it) }
}
