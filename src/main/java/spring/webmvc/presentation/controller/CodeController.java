package spring.webmvc.presentation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.CodeGroupResult;
import spring.webmvc.application.service.CodeService;
import spring.webmvc.presentation.dto.response.CodeListResponse;

@RestController
@RequestMapping("/codes")
@RequiredArgsConstructor
public class CodeController {

	private final CodeService codeService;

	@GetMapping
	public CodeListResponse findCodes() {
		List<CodeGroupResult> codes = codeService.findCodes();

		return CodeListResponse.of(codes);
	}
}
