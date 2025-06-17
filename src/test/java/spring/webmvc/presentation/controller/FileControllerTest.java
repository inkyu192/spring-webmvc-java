package spring.webmvc.presentation.controller;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import spring.webmvc.infrastructure.common.FileType;
import spring.webmvc.infrastructure.config.WebMvcTestConfig;
import spring.webmvc.infrastructure.external.S3Service;

@WebMvcTest(FileController.class)
@Import(WebMvcTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class FileControllerTest {

	@MockitoBean
	private S3Service s3Service;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation, WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
				.operationPreprocessors()
				.withRequestDefaults(Preprocessors.prettyPrint())
				.withResponseDefaults(Preprocessors.prettyPrint()))
			.build();
	}

	@Test
	void uploadFile() throws Exception {
		// Given
		MockMultipartFile file = new MockMultipartFile(
			"file",
			"test.jpg",
			"image/jpeg",
			"test-image-content".getBytes()
		);

		MockMultipartFile data = new MockMultipartFile(
			"data",
			"",
			"application/json",
			"{\"type\": \"PROFILE\"}".getBytes(StandardCharsets.UTF_8)
		);

		String key = "profile/20240610/uuid.jpg";
		Mockito.when(s3Service.putObject(FileType.TEMP, file)).thenReturn(key);

		// When & Then
		mockMvc.perform(
				RestDocumentationRequestBuilders.multipart("/files")
					.file(file)
					.file(data)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document("file-upload",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.requestParts(
						RequestDocumentation.partWithName("file").description("파일"),
						RequestDocumentation.partWithName("data").description("데이터")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("key").description("키")
					)
				)
			);
	}
}