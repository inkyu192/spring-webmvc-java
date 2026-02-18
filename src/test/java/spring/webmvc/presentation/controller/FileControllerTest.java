package spring.webmvc.presentation.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.webmvc.infrastructure.config.ControllerTest;
import spring.webmvc.infrastructure.external.s3.S3Service;
import spring.webmvc.infrastructure.properties.AppProperties;

@ControllerTest(FileController.class)
class FileControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private S3Service s3Service;

	@MockitoBean
	private AppProperties appProperties;

	@Test
	void uploadFile() throws Exception {
		String key = "temp/uuid.jpg";
		MockMultipartFile file = new MockMultipartFile(
			"file",
			"profile.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"test image content".getBytes()
		);

		when(s3Service.putObject(any())).thenReturn(key);

		AppProperties.AwsProperties aws = mock(AppProperties.AwsProperties.class);
		AppProperties.AwsProperties.CloudFrontProperties cloudfront = mock(
			AppProperties.AwsProperties.CloudFrontProperties.class);
		when(appProperties.aws()).thenReturn(aws);
		when(aws.cloudfront()).thenReturn(cloudfront);
		when(cloudfront.domain()).thenReturn("https://cdn.example.com");

		mockMvc.perform(
				RestDocumentationRequestBuilders.multipart("/files")
					.file(file)
					.header("Authorization", "Bearer access-token")
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(
				MockMvcRestDocumentation.document(
					"file-upload",
					HeaderDocumentation.requestHeaders(
						HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
					),
					RequestDocumentation.requestParts(
						RequestDocumentation.partWithName("file").description("업로드할 파일")
					),
					PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("key").description("S3 키"),
						PayloadDocumentation.fieldWithPath("url").description("파일 URL")
					)
				)
			);
	}
}
