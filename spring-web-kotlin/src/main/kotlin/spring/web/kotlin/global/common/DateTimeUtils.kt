package spring.web.kotlin.global.common

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

object DateTimeConstants {
    val KST_ZONE: ZoneId = ZoneId.of("Asia/Seoul")
}

fun LocalDateTime.convertToKst(): LocalDateTime =
    this.atZone(ZoneOffset.UTC).withZoneSameInstant(DateTimeConstants.KST_ZONE).toLocalDateTime()

fun LocalDateTime.convertToUtc(): LocalDateTime =
    this.atZone(DateTimeConstants.KST_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()

fun LocalDateTime.toOffsetDateTime(): OffsetDateTime =
    this.atZone(DateTimeConstants.KST_ZONE).toOffsetDateTime()
