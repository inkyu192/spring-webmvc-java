package spring.web.kotlin.global.converter.persistence

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import spring.web.kotlin.global.common.AESCryptoUtils

@Converter
class CryptoAttributeConverter : AttributeConverter<String?, String?> {

    override fun convertToDatabaseColumn(attribute: String?): String? {
        return attribute?.let { AESCryptoUtils.encrypt(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        return dbData?.let { AESCryptoUtils.decrypt(it) }
    }
}
