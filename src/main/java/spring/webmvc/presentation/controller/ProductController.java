package spring.webmvc.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.ProductService;
import spring.webmvc.presentation.dto.request.ProductSaveRequest;
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
		return productService.findProducts(pageable, name);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_READER')")
	public ProductResponse findProduct(@PathVariable Long id) {
		return productService.findProduct(id);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse saveProduct(@RequestBody @Validated ProductSaveRequest productSaveRequest) {
		return productService.saveProduct(productSaveRequest);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	public ResponseEntity<ProductResponse> putProduct(
		@PathVariable Long id,
		@RequestBody @Validated ProductSaveRequest productSaveRequest
	) {
		Pair<Boolean, ProductResponse> pair = productService.putProduct(id, productSaveRequest);
		Boolean isNew = pair.getFirst();
		ProductResponse productResponse = pair.getSecond();
		HttpStatus status = isNew ? HttpStatus.CREATED : HttpStatus.OK;

		return ResponseEntity.status(status).body(productResponse);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
}
