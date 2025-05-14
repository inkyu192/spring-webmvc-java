package spring.webmvc.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record MenuCreateRequest(
	@NotBlank
	String name,
	@NotBlank
	String path,
	Long parentId,
	List<Long> permissionIds
) {
	public MenuCreateRequest {
		if (permissionIds == null) {
			permissionIds = List.of();
		}
	}
}
