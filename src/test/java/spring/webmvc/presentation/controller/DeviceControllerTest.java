package spring.webmvc.presentation.controller;

import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import spring.webmvc.application.service.DeviceService;
import spring.webmvc.domain.model.entity.UserDevice;
import spring.webmvc.domain.model.enums.DeviceType;
import spring.webmvc.infrastructure.config.ControllerTest;
import spring.webmvc.infrastructure.security.SecurityContextUtil;

@ControllerTest(DeviceController.class)
class DeviceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DeviceService deviceService;

	private final Long userId = 1L;

	@Test
	void getMyDevices() throws Exception {
		try (MockedStatic<SecurityContextUtil> mockedStatic = mockStatic(SecurityContextUtil.class)) {
			mockedStatic.when(SecurityContextUtil::getUserId).thenReturn(userId);

			UserDevice device = mock(UserDevice.class);
			when(device.getDeviceId()).thenReturn("device-1");
			when(device.getDeviceName()).thenReturn("iPhone 15");
			when(device.getDeviceType()).thenReturn(DeviceType.IOS);
			when(device.getLastLoginAt()).thenReturn(Instant.parse("2024-01-01T00:00:00Z"));
			when(device.getCreatedAt()).thenReturn(Instant.parse("2024-01-01T00:00:00Z"));

			when(deviceService.getMyDevices(userId)).thenReturn(List.of(device));

			mockMvc.perform(
					RestDocumentationRequestBuilders.get("/devices")
						.header("Authorization", "Bearer accessToken")
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(
					MockMvcRestDocumentation.document(
						"device-list",
						HeaderDocumentation.requestHeaders(
							HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
						),
						PayloadDocumentation.responseFields(
							PayloadDocumentation.fieldWithPath("size").description("디바이스 수"),
							PayloadDocumentation.fieldWithPath("devices[].deviceId").description("디바이스 ID"),
							PayloadDocumentation.fieldWithPath("devices[].deviceName").description("디바이스 이름"),
							PayloadDocumentation.fieldWithPath("devices[].deviceType").description("디바이스 타입"),
							PayloadDocumentation.fieldWithPath("devices[].lastLoginAt").description("마지막 로그인 시간"),
							PayloadDocumentation.fieldWithPath("devices[].createdAt").description("등록 시간")
						)
					)
				);
		}
	}

	@Test
	void deleteDevice() throws Exception {
		try (MockedStatic<SecurityContextUtil> mockedStatic = mockStatic(SecurityContextUtil.class)) {
			mockedStatic.when(SecurityContextUtil::getUserId).thenReturn(userId);

			doNothing().when(deviceService).deleteDevice(userId, "device-1");

			mockMvc.perform(
					RestDocumentationRequestBuilders.delete("/devices/{deviceId}", "device-1")
						.header("Authorization", "Bearer accessToken")
				)
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				.andDo(
					MockMvcRestDocumentation.document(
						"device-delete",
						HeaderDocumentation.requestHeaders(
							HeaderDocumentation.headerWithName("Authorization").description("액세스 토큰")
						),
						RequestDocumentation.pathParameters(
							RequestDocumentation.parameterWithName("deviceId").description("디바이스 ID")
						)
					)
				);
		}
	}
}
