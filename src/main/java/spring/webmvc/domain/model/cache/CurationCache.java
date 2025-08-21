package spring.webmvc.domain.model.cache;

public record CurationCache(
	Long id,
	String title
) {
	public static CurationCache create(Long id, String title) {
		return new CurationCache(id, title);
	}
}
