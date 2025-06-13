package spring.webmvc.infrastructure.common;

import org.springframework.web.multipart.MultipartFile;

public final class FileUtil {
	private FileUtil() {
	}

	public static void validate(FileType fileType, MultipartFile file) {
		String filename = file.getOriginalFilename();

		if (filename == null) {
			throw new IllegalArgumentException("파일 이름이 존재하지 않습니다.");
		}

		if (!filename.contains(".")) {
			throw new IllegalArgumentException("확장자가 없는 파일입니다.");
		}

		String extension = extractExtension(filename);
		if (!fileType.getAllowedExtensions().contains(extension)) {
			throw new IllegalArgumentException("허용되지 않은 확장자입니다: %s".formatted(extension));
		}

		if (file.getSize() > fileType.getMaxSize()) {
			throw new IllegalArgumentException("파일 크기가 허용된 범위를 초과했습니다.");
		}
	}

	public static String extractExtension(String filename) {
		int lastDot = filename.lastIndexOf('.');
		if (lastDot == -1 || lastDot == filename.length() - 1) {
			return "bin";
		}
		return filename.substring(lastDot + 1);
	}
}
