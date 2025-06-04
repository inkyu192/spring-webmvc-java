package spring.webmvc.domain.model.document;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Document(collection = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

	@Id
	private String id;
	private Long memberId;
	private String title;
	private String message;
	private String url;

	@Indexed(expireAfter = "1d")
	private Instant createdAt;

	public static Notification create(Long memberId, String title, String message, String url) {
		Notification notification = new Notification();

		notification.memberId = memberId;
		notification.title = title;
		notification.message = message;
		notification.url = url;
		notification.createdAt = Instant.now();

		return notification;
	}
}
