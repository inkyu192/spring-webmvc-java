package spring.webmvc.presentation.controller.customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.query.ProductCursorPageQuery;
import spring.webmvc.application.dto.result.ProductDetailResult;
import spring.webmvc.application.dto.result.ProductSummaryResult;
import spring.webmvc.application.service.ProductService;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;
import spring.webmvc.presentation.dto.response.CursorPageResponse;
import spring.webmvc.presentation.dto.response.ProductDetailResponse;
import spring.webmvc.presentation.dto.response.ProductSummaryResponse;

@RestController("customerProductController")
@RequestMapping("/customer/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public CursorPageResponse<ProductSummaryResponse> findProducts(
		@RequestParam(required = false) Long cursorId,
		@RequestParam(required = false) String name
	) {
		ProductCursorPageQuery query = new ProductCursorPageQuery(cursorId, name, ProductStatus.SELLING);

		CursorPage<ProductSummaryResult> page = productService.findProductsWithCursorPage(query);

		return CursorPageResponse.of(page, ProductSummaryResponse::of);
	}

	@GetMapping("/{id}")
	public ProductDetailResponse findProduct(@PathVariable Long id) {
		ProductDetailResult result = productService.findProductCached(id);

		productService.incrementProductViewCount(id);

		return ProductDetailResponse.of(result);
	}
}
