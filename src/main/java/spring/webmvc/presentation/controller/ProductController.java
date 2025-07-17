package spring.webmvc.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.application.dto.result.AccommodationResult;
import spring.webmvc.application.dto.result.FlightResult;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.application.dto.result.TicketResult;
import spring.webmvc.application.service.ProductService;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.presentation.dto.request.ProductCreateRequest;
import spring.webmvc.presentation.dto.request.ProductUpdateRequest;
import spring.webmvc.presentation.dto.response.AccommodationResponse;
import spring.webmvc.presentation.dto.response.FlightResponse;
import spring.webmvc.presentation.dto.response.ProductResponse;
import spring.webmvc.presentation.dto.response.TicketResponse;

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

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_READER')")
	public ProductResponse findProduct(@PathVariable Long id, @RequestParam Category category) {
		ProductResult productResult = productService.findProduct(id, category);
		return toProductResponse(productResult);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse createProduct(@RequestBody @Validated ProductCreateRequest productCreateRequest) {
		ProductCreateCommand command = productCreateRequest.toCommand();
		ProductResult productResult = productService.createProduct(command);
		return toProductResponse(productResult);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	public ProductResponse updateProduct(
		@PathVariable Long id,
		@RequestBody @Validated ProductUpdateRequest productUpdateRequest
	) {
		ProductUpdateCommand productUpdateCommand = productUpdateRequest.toCommand();
		ProductResult productResult = productService.updateProduct(id, productUpdateCommand);
		return toProductResponse(productResult);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable Long id, @RequestParam Category category) {
		productService.deleteProduct(category, id);
	}

	private ProductResponse toProductResponse(ProductResult productResult) {
		return switch (productResult.getCategory()) {
			case TICKET -> new TicketResponse((TicketResult)productResult);
			case FLIGHT -> new FlightResponse((FlightResult)productResult);
			case ACCOMMODATION -> new AccommodationResponse((AccommodationResult)productResult);
		};
	}
}
