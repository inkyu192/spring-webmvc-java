package spring.web.kotlin.global.common

import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AESCryptoUtils(secretKey: String) {
    private val secretKeySpec: SecretKeySpec = SecretKeySpec(secretKey.toByteArray(StandardCharsets.UTF_8), "AES")

    fun encrypt(plainText: String): String = runCatching {
        val cipher = getCipher(Cipher.ENCRYPT_MODE, secretKeySpec)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        Base64.getEncoder().encodeToString(encryptedBytes)
    }.getOrElse { throw RuntimeException(it) }

    fun decrypt(encryptedText: String): String = runCatching {
        val cipher = getCipher(Cipher.DECRYPT_MODE, secretKeySpec)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText))
        String(decryptedBytes)
    }.getOrElse { throw RuntimeException(it) }

    private fun getCipher(mode: Int, secretKeySpec: SecretKeySpec) =
        Cipher.getInstance("AES").apply { init(mode, secretKeySpec) }
}
