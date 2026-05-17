package spring.webmvc.presentation.controller.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.ProductSummaryResult;
import spring.webmvc.application.dto.result.WishlistResult;
import spring.webmvc.application.service.ProductService;
import spring.webmvc.application.service.WishlistService;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.infrastructure.security.SecurityContextUtil;
import spring.webmvc.presentation.dto.request.WishlistCreateRequest;
import spring.webmvc.presentation.dto.response.CursorPageResponse;
import spring.webmvc.presentation.dto.response.ProductSummaryResponse;
import spring.webmvc.presentation.dto.response.WishlistResponse;

@RestController("customerMyController")
@RequestMapping("/customer/my")
@RequiredArgsConstructor
public class MyController {

	private final ProductService productService;
	private final WishlistService wishlistService;

	@GetMapping("/recently-viewed")
	public CursorPageResponse<ProductSummaryResponse> findRecentlyViewedProducts(
		@RequestParam(required = false) Long cursorId
	) {
		Long userId = SecurityContextUtil.getUserId();
		CursorPage<ProductSummaryResult> page = productService.findRecentlyViewedProducts(userId, cursorId);
		return CursorPageResponse.of(page, ProductSummaryResponse::of);
	}

	@GetMapping("/wishlists")
	public CursorPageResponse<WishlistResponse> findWishlists(
		@RequestParam(required = false) Long cursorId
	) {
		Long userId = SecurityContextUtil.getUserId();
		CursorPage<WishlistResult> page = wishlistService.findWishlists(userId, cursorId);
		return CursorPageResponse.of(page, WishlistResponse::of);
	}

	@PostMapping("/wishlists")
	@ResponseStatus(HttpStatus.CREATED)
	public void addWishlist(@Valid @RequestBody WishlistCreateRequest request) {
		Long userId = SecurityContextUtil.getUserId();
		wishlistService.addWishlist(userId, request.productId());
	}

	@DeleteMapping("/wishlists/{wishlistId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeWishlist(@PathVariable Long wishlistId) {
		Long userId = SecurityContextUtil.getUserId();
		wishlistService.removeWishlist(userId, wishlistId);
	}
}
