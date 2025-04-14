package spring.webmvc.infrastructure.util.crypto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class Base64AESCryptoUtil implements CryptoUtil {

	private final SecretKeySpec secretKey;
	private final IvParameterSpec ivParameter;

	public Base64AESCryptoUtil(CryptoProperties cryptoProperties) {
		secretKey = new SecretKeySpec(cryptoProperties.getSecretKey().getBytes(StandardCharsets.UTF_8), "AES");
		ivParameter = new IvParameterSpec(cryptoProperties.getIvParameter().getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public String encrypt(String plainText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameter);

			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String decrypt(String encryptedText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameter);

			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

			return new String(decryptedBytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
