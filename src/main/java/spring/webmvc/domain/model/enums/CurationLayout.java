package spring.webmvc.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurationLayout {
	GRID,
	CAROUSEL,
	LIST,
}
