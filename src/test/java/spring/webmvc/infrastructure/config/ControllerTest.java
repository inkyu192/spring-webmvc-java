package spring.webmvc.infrastructure.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebMvcTest(
	excludeFilters = {
		@ComponentScan.Filter(
			type = FilterType.REGEX,
			pattern = {".*Filter", ".*ExceptionHandler"}
		)
	}
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@Import(RestDocsTestConfig.class)
public @interface ControllerTest {

	@AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
	Class<?>[] value() default {};
}
