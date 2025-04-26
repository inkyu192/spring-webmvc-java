package spring.webmvc.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.ProductService;
import spring.webmvc.presentation.dto.response.ProductResponse;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	@PreAuthorize("hasAuthority('PRODUCT_READER')")
	public Page<ProductResponse> findProducts(
		@PageableDefault Pageable pageable,
		@RequestParam(required = false) String name
	) {
		return productService.findProducts(pageable, name).map(ProductResponse::new);
	}
}
