package spring.webmvc.infrastructure.crypto;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import spring.webmvc.infrastructure.common.ByteArrayUtil;
import spring.webmvc.infrastructure.common.StringUtil;
import spring.webmvc.infrastructure.properties.AppProperties;

@Primary
@Component
public class AesHexCryptoService implements CryptoService {

	private final SecretKeySpec secretKey;
	private final IvParameterSpec ivParameter;

	public AesHexCryptoService(AppProperties appProperties) {
		this.secretKey = new SecretKeySpec(appProperties.crypto().secretKey().getBytes(StandardCharsets.UTF_8), "AES");
		this.ivParameter = new IvParameterSpec(appProperties.crypto().ivParameter().getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public String encrypt(String plainText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameter);

			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			return ByteArrayUtil.toHexString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String decrypt(String encryptedText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameter);

			byte[] decryptedBytes = cipher.doFinal(StringUtil.hexToByteArray(encryptedText));

			return new String(decryptedBytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
