package spring.webmvc.infrastructure.common;

public final class ByteArrayUtil {

	private ByteArrayUtil() {
	}

	public static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
