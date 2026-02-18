package spring.webmvc.infrastructure.common;

public final class StringUtil {

	private StringUtil() {
	}

	public static byte[] hexToByteArray(String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte)((Character.digit(hex.charAt(i), 16) << 4)
				+ Character.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}
}
