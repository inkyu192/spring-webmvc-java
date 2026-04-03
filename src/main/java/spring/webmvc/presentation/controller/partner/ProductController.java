package spring.webmvc.presentation.controller.partner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.application.dto.query.ProductOffsetPageQuery;
import spring.webmvc.application.dto.result.ProductDetailResult;
import spring.webmvc.application.dto.result.ProductSummaryResult;
import spring.webmvc.application.service.ProductService;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.presentation.dto.request.ProductCreateRequest;
import spring.webmvc.presentation.dto.request.ProductUpdateRequest;
import spring.webmvc.presentation.dto.response.OffsetPageResponse;
import spring.webmvc.presentation.dto.response.ProductDetailResponse;
import spring.webmvc.presentation.dto.response.ProductSummaryResponse;

@RestController("partnerProductController")
@RequestMapping("/partner/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public OffsetPageResponse<ProductSummaryResponse> findProducts(
		@PageableDefault Pageable pageable,
		@RequestParam(required = false) ProductStatus status,
		@RequestParam(required = false) String name
	) {
		ProductOffsetPageQuery query = new ProductOffsetPageQuery(pageable, name, status);

		Page<ProductSummaryResult> page = productService.findProductsWithOffsetPage(query);

		return OffsetPageResponse.of(page, ProductSummaryResponse::of);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_READ')")
	public ProductDetailResponse findProduct(@PathVariable Long id) {
		ProductDetailResult result = productService.findProduct(id);

		return ProductDetailResponse.of(result);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('PRODUCT_CREATE')")
	@ResponseStatus(HttpStatus.CREATED)
	public ProductDetailResponse createProduct(
		@RequestBody @Validated ProductCreateRequest request
	) {
		ProductCreateCommand command = request.toCommand();
		ProductDetailResult result = productService.createProduct(command);

		return ProductDetailResponse.of(result);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
	public ProductDetailResponse updateProduct(
		@PathVariable Long id,
		@RequestBody @Validated ProductUpdateRequest request
	) {
		ProductUpdateCommand command = request.toCommand(id);
		ProductDetailResult result = productService.updateProduct(command);

		return ProductDetailResponse.of(result);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_DELETE')")
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
}
