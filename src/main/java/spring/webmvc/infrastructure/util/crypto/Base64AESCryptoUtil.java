package spring.webmvc.infrastructure.util.crypto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Base64AESCryptoUtil implements CryptoUtil {

	@Value("${crypto.secret-key}")
	private String secretKey;
	@Value("${crypto.iv-parameter}")
	private String ivParameter;

	@Override
	public String encrypt(String plainText) {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter.getBytes(StandardCharsets.UTF_8));

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String decrypt(String encryptedText) {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter.getBytes(StandardCharsets.UTF_8));

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

			return new String(decryptedBytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
