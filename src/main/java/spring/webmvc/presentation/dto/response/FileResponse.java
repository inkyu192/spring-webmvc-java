package spring.webmvc.presentation.dto.response;

public record FileResponse(
	String key,
	String url
) {
	public static FileResponse of(String key, String cloudfrontDomain) {
		return new FileResponse(key, cloudfrontDomain + "/" + key);
	}
}
