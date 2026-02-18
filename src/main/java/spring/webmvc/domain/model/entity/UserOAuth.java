package spring.webmvc.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.enums.OauthProvider;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOAuth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.STRING)
	private OauthProvider oauthProvider;

	private String oauthUserId;

	public static UserOAuth create(User user, OauthProvider oauthProvider, String oauthUserId) {
		UserOAuth userOAuth = new UserOAuth();

		userOAuth.user = user;
		userOAuth.oauthProvider = oauthProvider;
		userOAuth.oauthUserId = oauthUserId;

		return userOAuth;
	}
}
