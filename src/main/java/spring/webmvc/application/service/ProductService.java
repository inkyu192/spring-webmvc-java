package spring.webmvc.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.presentation.dto.request.ProductSaveRequest;
import spring.webmvc.presentation.dto.response.ProductResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public ProductResponse saveProduct(ProductSaveRequest productSaveRequest) {
		Product product = productRepository.save(
			Product.create(
				productSaveRequest.name(),
				productSaveRequest.description(),
				productSaveRequest.price(),
				productSaveRequest.quantity(),
				productSaveRequest.category()
			)
		);

		return new ProductResponse(product);
	}

	public Page<ProductResponse> findProducts(Pageable pageable, String name) {
		return productRepository.findAll(pageable, name).map(ProductResponse::new);
	}

	public ProductResponse findProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Product.class, id));

		return new ProductResponse(product);
	}

	@Transactional
	public Pair<Boolean, ProductResponse> putProduct(Long id, ProductSaveRequest productSaveRequest) {
		return productRepository.findById(id)
			.map(product -> Pair.of(false, updateProduct(product, productSaveRequest)))
			.orElseGet(() -> Pair.of(true, saveProduct(productSaveRequest)));
	}

	private ProductResponse updateProduct(Product product, ProductSaveRequest productSaveRequest) {
		product.update(
			productSaveRequest.name(),
			productSaveRequest.description(),
			productSaveRequest.price(),
			productSaveRequest.quantity(),
			productSaveRequest.category()
		);

		return new ProductResponse(product);
	}

	@Transactional
	public void deleteProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Product.class, id));

		productRepository.delete(product);
	}
}