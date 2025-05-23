package spring.webmvc.domain.model.entity;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
public class Notification extends BaseTime {

	@Id
	@GeneratedValue
	private Long id;
	private String title;
	private String message;
	private String url;
	private boolean isRead;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	public static Notification of(Member member, String title, String message, String url) {
		Notification notification = new Notification();

		notification.member = member;
		notification.title = title;
		notification.message = message;
		notification.url = url;
		notification.isRead = false;

		return notification;
	}
}
