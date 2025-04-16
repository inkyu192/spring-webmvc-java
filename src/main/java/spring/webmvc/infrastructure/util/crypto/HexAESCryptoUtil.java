package spring.webmvc.infrastructure.util.crypto;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class HexAESCryptoUtil implements CryptoUtil {

	private final SecretKeySpec secretKey;
	private final IvParameterSpec ivParameter;

	public HexAESCryptoUtil(CryptoProperties cryptoProperties) {
		secretKey = new SecretKeySpec(cryptoProperties.getSecretKey().getBytes(StandardCharsets.UTF_8), "AES");
		ivParameter = new IvParameterSpec(cryptoProperties.getIvParameter().getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public String encrypt(String plainText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameter);

			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			return bytesToHex(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String decrypt(String encryptedText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameter);

			byte[] decryptedBytes = cipher.doFinal(hexToBytes(encryptedText));

			return new String(decryptedBytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xFF & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	private byte[] hexToBytes(String hex) {
		int length = hex.length();
		byte[] data = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			data[i / 2] = (byte)((Character.digit(hex.charAt(i), 16) << 4)
				+ Character.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}
}
