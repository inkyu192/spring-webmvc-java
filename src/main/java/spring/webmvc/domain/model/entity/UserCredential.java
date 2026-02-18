package spring.webmvc.domain.model.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.vo.Email;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCredential {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Embedded
	private Email email;

	private String password;

	private Instant verifiedAt;

	public static UserCredential create(User user, Email email, String password) {
		UserCredential userCredential = new UserCredential();

		userCredential.user = user;
		userCredential.email = email;
		userCredential.password = password;

		return userCredential;
	}

	public void verify() {
		this.verifiedAt = Instant.now();
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public boolean isVerified() {
		return verifiedAt != null;
	}
}
