package spring.web.java.domain.requestlock;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash(value = "request_lock", timeToLive = 1)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestLock {

	@Id
	private String requestId;

	public static RequestLock create(String requestId) {
		RequestLock requestLock = new RequestLock();

		requestLock.requestId = requestId;

		return requestLock;
	}
}
