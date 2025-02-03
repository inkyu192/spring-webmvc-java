package spring.web.kotlin.global.common

import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AESCryptoUtils {
    private const val SECRET_KEY = "d9ANIqIyfTygI92m6jWFfAzUbEP73TNB"
    private const val ALGORITHM = "AES"

    fun encrypt(plainText: String): String = runCatching {
        val cipher = getCipher(Cipher.ENCRYPT_MODE)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        Base64.getEncoder().encodeToString(encryptedBytes)
    }.getOrElse { throw RuntimeException(it) }

    fun decrypt(encryptedText: String): String = runCatching {
        val cipher = getCipher(Cipher.DECRYPT_MODE)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText))
        String(decryptedBytes)
    }.getOrElse { throw RuntimeException(it) }

    private fun getSecretKeySpec() = SecretKeySpec(SECRET_KEY.toByteArray(StandardCharsets.UTF_8), ALGORITHM)

    private fun getCipher(mode: Int): Cipher = Cipher.getInstance(ALGORITHM).apply { init(mode, getSecretKeySpec()) }
}
