package spring.web.java.global.common;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public abstract class DateTimeUtils {

	public static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

	public static LocalDateTime convertUtcToKst(LocalDateTime utcDateTime) {
		return utcDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(KST_ZONE).toLocalDateTime();
	}

	public static LocalDateTime convertKstToUtc(LocalDateTime kstDateTime) {
		return kstDateTime.atZone(KST_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
	}

	public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
		return localDateTime.atZone(KST_ZONE).toOffsetDateTime();
	}
}
