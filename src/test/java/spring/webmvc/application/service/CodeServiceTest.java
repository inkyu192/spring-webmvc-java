package spring.webmvc.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import spring.webmvc.application.dto.result.CodeGroupResult;

@ExtendWith(MockitoExtension.class)
class CodeServiceTest {

	@Mock
	private TranslationService translationService;

	private CodeService codeService;

	private final Locale locale = Locale.KOREAN;

	@BeforeEach
	void setUp() {
		codeService = new CodeService(translationService);
		LocaleContextHolder.setLocale(locale);
	}

	@AfterEach
	void tearDown() {
		LocaleContextHolder.resetLocaleContext();
	}

	@Test
	@DisplayName("전체 코드 그룹 및 하위 코드 조회")
	void findCodes() {
		when(translationService.getMessage(eq("Gender"), eq(locale))).thenReturn("성별");
		when(translationService.getMessage(eq("Gender.MALE"), eq(locale))).thenReturn("남성");
		when(translationService.getMessage(eq("Gender.FEMALE"), eq(locale))).thenReturn("여성");
		when(translationService.getMessage(eq("CurationCategory"), eq(locale))).thenReturn("큐레이션 카테고리");
		when(translationService.getMessage(startsWith("CurationCategory."), eq(locale))).thenReturn("큐레이션");
		when(translationService.getMessage(eq("OauthProvider"), eq(locale))).thenReturn("OAuth 제공자");
		when(translationService.getMessage(startsWith("OauthProvider."), eq(locale))).thenReturn("OAuth");
		when(translationService.getMessage(eq("OrderStatus"), eq(locale))).thenReturn("주문 상태");
		when(translationService.getMessage(startsWith("OrderStatus."), eq(locale))).thenReturn("주문");
		when(translationService.getMessage(eq("ProductCategory"), eq(locale))).thenReturn("상품 카테고리");
		when(translationService.getMessage(startsWith("ProductCategory."), eq(locale))).thenReturn("카테고리");
		when(translationService.getMessage(eq("ProductStatus"), eq(locale))).thenReturn("상품 상태");
		when(translationService.getMessage(startsWith("ProductStatus."), eq(locale))).thenReturn("상태");

		List<CodeGroupResult> result = codeService.findCodes();

		assertThat(result).hasSize(6);
		assertThat(result.stream().map(CodeGroupResult::name).toList()).containsExactlyInAnyOrder(
			"CurationCategory", "Gender", "OauthProvider", "OrderStatus", "ProductCategory", "ProductStatus"
		);

		CodeGroupResult genderGroup = result.stream()
			.filter(g -> g.name().equals("Gender"))
			.findFirst()
			.orElseThrow();

		assertThat(genderGroup.label()).isEqualTo("성별");
		assertThat(genderGroup.codes()).hasSize(2);
		assertThat(genderGroup.codes().get(0).code()).isEqualTo("MALE");
		assertThat(genderGroup.codes().get(0).label()).isEqualTo("남성");
		assertThat(genderGroup.codes().get(1).code()).isEqualTo("FEMALE");
		assertThat(genderGroup.codes().get(1).label()).isEqualTo("여성");
	}
}
