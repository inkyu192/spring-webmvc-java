package spring.webmvc.domain.model.entity;

import java.time.Instant;

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
import spring.webmvc.domain.model.enums.DeviceType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDevice extends BaseTime {

	public static final int MAX_DEVICES = 5;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private String deviceId;

	private String deviceName;

	@Enumerated(EnumType.STRING)
	private DeviceType deviceType;

	private String token;

	private Instant lastLoginAt;

	public static UserDevice create(User user, String deviceId, String deviceName, DeviceType deviceType,
		String token) {
		UserDevice userDevice = new UserDevice();
		userDevice.user = user;
		userDevice.deviceId = deviceId;
		userDevice.deviceName = deviceName;
		userDevice.deviceType = deviceType;
		userDevice.token = token;
		userDevice.lastLoginAt = Instant.now();
		return userDevice;
	}

	public void updateLastLoginAt() {
		this.lastLoginAt = Instant.now();
	}

	public void updateToken(String token) {
		this.token = token;
	}
}
