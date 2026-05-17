package spring.webmvc.domain.repository.cache;

public interface OrderCacheRepository {
	Long incrementSequence(String date);

	boolean setSequence(String date, Long value);
}
