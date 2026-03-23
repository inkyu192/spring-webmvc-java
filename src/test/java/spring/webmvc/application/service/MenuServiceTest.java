package spring.webmvc.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import spring.webmvc.application.dto.result.MenuResult;
import spring.webmvc.domain.model.entity.Menu;
import spring.webmvc.domain.repository.MenuRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private TranslationService translationService;

	private MenuService menuService;

	private final Locale locale = Locale.KOREAN;

	@BeforeEach
	void setUp() {
		menuService = new MenuService(menuRepository, translationService);
		LocaleContextHolder.setLocale(locale);

		var authorities = List.of(
			new SimpleGrantedAuthority("PRODUCT_READ"),
			new SimpleGrantedAuthority("PRODUCT_WRITE")
		);
		var authentication = new UsernamePasswordAuthenticationToken("1", null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@AfterEach
	void tearDown() {
		LocaleContextHolder.resetLocaleContext();
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("메뉴 계층 구조 및 locale별 번역 정상 동작")
	void findMenus() throws Exception {
		Menu parentMenu = Menu.create("menu.products", null, null, 2L);
		setId(parentMenu, 2L);

		Menu childMenu1 = Menu.create("menu.products.transports", "/products/transports", parentMenu, 1L);
		setId(childMenu1, 6L);

		Menu childMenu2 = Menu.create("menu.products.accommodations", "/products/accommodations", parentMenu, 2L);
		setId(childMenu2, 7L);

		when(menuRepository.findAllWithRecursiveByPermissions(Set.of("PRODUCT_READ", "PRODUCT_WRITE")))
			.thenReturn(List.of(parentMenu, childMenu1, childMenu2));

		when(translationService.getMessage("menu.products", locale)).thenReturn("상품");
		when(translationService.getMessage("menu.products.transports", locale)).thenReturn("교통수단관리");
		when(translationService.getMessage("menu.products.accommodations", locale)).thenReturn("숙박관리");

		List<MenuResult> result = menuService.findMenus();

		assertThat(result).hasSize(1);
		assertThat(result.get(0).id()).isEqualTo(2L);
		assertThat(result.get(0).name()).isEqualTo("상품");
		assertThat(result.get(0).path()).isNull();
		assertThat(result.get(0).children()).hasSize(2);
		assertThat(result.get(0).children().get(0).id()).isEqualTo(6L);
		assertThat(result.get(0).children().get(0).name()).isEqualTo("교통수단관리");
		assertThat(result.get(0).children().get(0).path()).isEqualTo("/products/transports");
		assertThat(result.get(0).children().get(1).id()).isEqualTo(7L);
		assertThat(result.get(0).children().get(1).name()).isEqualTo("숙박관리");
		assertThat(result.get(0).children().get(1).path()).isEqualTo("/products/accommodations");
	}

	@Test
	@DisplayName("영어 locale로 메뉴 조회 시 영어 번역 반환")
	void findMenusEnglish() throws Exception {
		Locale enLocale = Locale.ENGLISH;
		LocaleContextHolder.setLocale(enLocale);

		Menu parentMenu = Menu.create("menu.products", null, null, 2L);
		setId(parentMenu, 2L);

		Menu childMenu = Menu.create("menu.products.transports", "/products/transports", parentMenu, 1L);
		setId(childMenu, 6L);

		when(menuRepository.findAllWithRecursiveByPermissions(Set.of("PRODUCT_READ", "PRODUCT_WRITE")))
			.thenReturn(List.of(parentMenu, childMenu));

		when(translationService.getMessage("menu.products", enLocale)).thenReturn("Products");
		when(translationService.getMessage("menu.products.transports", enLocale)).thenReturn("Transport Management");

		List<MenuResult> result = menuService.findMenus();

		assertThat(result).hasSize(1);
		assertThat(result.get(0).name()).isEqualTo("Products");
		assertThat(result.get(0).children()).hasSize(1);
		assertThat(result.get(0).children().get(0).name()).isEqualTo("Transport Management");
	}

	@Test
	@DisplayName("권한이 없으면 빈 리스트 반환")
	void findMenusEmptyPermissions() {
		var authentication = new UsernamePasswordAuthenticationToken("1", null, List.of());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		List<MenuResult> result = menuService.findMenus();

		assertThat(result).isEmpty();
	}

	private void setId(Menu menu, Long id) throws Exception {
		Field idField = Menu.class.getDeclaredField("id");
		idField.setAccessible(true);
		idField.set(menu, id);
	}
}