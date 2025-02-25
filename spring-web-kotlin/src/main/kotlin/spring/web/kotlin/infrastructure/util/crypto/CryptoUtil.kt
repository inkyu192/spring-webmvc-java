package spring.web.kotlin.infrastructure.util.crypto

interface CryptoUtil {
    fun encrypt(plainText: String): String
    fun decrypt(encryptedText: String): String
}