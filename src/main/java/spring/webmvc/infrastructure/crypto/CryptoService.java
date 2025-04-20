package spring.webmvc.infrastructure.crypto;

public interface CryptoService {

	String encrypt(String plainText);
	String decrypt(String encryptedText);
}
