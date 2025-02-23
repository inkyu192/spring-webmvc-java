package spring.web.java.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends Base {

	@Id
	@GeneratedValue
	@Column(name = "notification_id")
	private Long id;
	private String title;
	private String message;
	private boolean isRead;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public static Notification of(String title, String message, Member member) {
		Notification notification = new Notification();

		notification.title = title;
		notification.message = message;
		notification.isRead = false;
		notification.member = member;

		return notification;
	}
}
