package spring.webmvc.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.presentation.dto.response.ProductResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Page<ProductResponse> findProducts(Pageable pageable, String name) {
		return productRepository.findAll(pageable, name).map(ProductResponse::new);
	}
}