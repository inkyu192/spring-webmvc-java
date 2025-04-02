package spring.webmvc.infrastructure.util.crypto;

public interface CryptoUtil {

	String encrypt(String plainText);
	String decrypt(String encryptedText);
}
