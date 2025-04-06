package spring.webmvc.domain.repository;

import java.util.Optional;

public interface TokenRepository {

	Optional<String> findByMemberId(Long memberId);

	String save(Long memberId, String token);
}
