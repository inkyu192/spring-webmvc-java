package spring.web.kotlin.infrastructure.util.crypto

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
class Base64AESCryptoUtil(
    @Value("\${crypto.secret-key}")
    private val SECRET_KEY: String,
    @Value("\${crypto.iv-parameter}")
    private val IV_PARAMETER: String,
) : CryptoUtil {
    override fun encrypt(plainText: String): String = runCatching {
        val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(StandardCharsets.UTF_8), "AES")
        val ivParameterSpec = IvParameterSpec(IV_PARAMETER.toByteArray(StandardCharsets.UTF_8))

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            .apply { init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec) }

        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        Base64.getEncoder().encodeToString(encryptedBytes)
    }.getOrElse { throw RuntimeException(it) }

    override fun decrypt(encryptedText: String): String = runCatching {
        val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(StandardCharsets.UTF_8), "AES")
        val ivParameterSpec = IvParameterSpec(IV_PARAMETER.toByteArray(StandardCharsets.UTF_8))

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            .apply { init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec) }

        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText))

        String(decryptedBytes, StandardCharsets.UTF_8)
    }.getOrElse { throw RuntimeException(it) }
}
