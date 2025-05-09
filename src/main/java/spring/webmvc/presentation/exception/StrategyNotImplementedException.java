package spring.webmvc.presentation.exception;

import org.springframework.http.HttpStatus;

import spring.webmvc.domain.model.enums.Category;

public class StrategyNotImplementedException extends AbstractHttpException {

	public StrategyNotImplementedException(Class<?> clazz, Category category) {
		super("%s 전략이 %s에 아직 구현되지 않았습니다.".formatted(category, clazz.getSimpleName()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
