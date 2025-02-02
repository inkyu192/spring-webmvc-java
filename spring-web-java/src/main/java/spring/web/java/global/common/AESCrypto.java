package spring.web.java.global.common;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class AESCrypto {

	private final static String SECRET_KEY = "I5XyzQGnXTdnTzdiC1UXzJcKsF75rWg0";

	private static SecretKey getKeyFromString() {
		byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
		return new SecretKeySpec(keyBytes, "AES");
	}

	public static String encrypt(String plainText) {
		try {
			SecretKey key = getKeyFromString();
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
				 InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String encryptedText) {
		try {
			SecretKey key = getKeyFromString();
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
			byte[] decryptedBytes = cipher.doFinal(decodedBytes);
			return new String(decryptedBytes, StandardCharsets.UTF_8);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
				 InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}
}
