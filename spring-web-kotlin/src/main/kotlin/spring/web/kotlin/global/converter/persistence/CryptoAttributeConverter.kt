package spring.web.kotlin.global.converter.persistence

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import spring.web.kotlin.global.common.AESCryptoUtils

@Converter
class CryptoAttributeConverter : AttributeConverter<String?, String?> {
    private val aesCryptoUtils = AESCryptoUtils("d9ANIqIyfTygI92m6jWFfAzUbEP73TNB")

    override fun convertToDatabaseColumn(attribute: String?): String? {
        return attribute?.let { aesCryptoUtils.encrypt(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        return dbData?.let { aesCryptoUtils.decrypt(it) }
    }
}
