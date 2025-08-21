package spring.webmvc.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.CurationCreateCommand;
import spring.webmvc.application.dto.command.CurationProductCreateCommand;
import spring.webmvc.application.dto.result.CurationProductResult;
import spring.webmvc.application.dto.result.CurationResult;
import spring.webmvc.domain.model.cache.CurationCache;
import spring.webmvc.domain.model.cache.CurationProductCache;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.repository.CurationCacheRepository;
import spring.webmvc.domain.repository.CurationProductRepository;
import spring.webmvc.domain.repository.CurationRepository;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.domain.service.CurationDomainService;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurationService {

	private final CurationDomainService curationDomainService;
	private final CurationRepository curationRepository;
	private final CurationProductRepository curationProductRepository;
	private final ProductRepository productRepository;
	private final CurationCacheRepository curationCacheRepository;

	@Transactional
	public Long createCuration(CurationCreateCommand command) {
		List<Long> requestProductIds = command.products().stream()
			.map(CurationProductCreateCommand::productId)
			.toList();
		List<Product> products = productRepository.findAllById(requestProductIds);

		Curation curation = curationRepository.save(
			curationDomainService.createCuration(
				command.title(),
				command.isExposed(),
				command.sortOrder(),
				requestProductIds,
				products
			)
		);

		curationCacheRepository.deleteAll();

		return curation.getId();
	}

	public List<CurationResult> findCurations() {
		List<CurationCache> cached = curationCacheRepository.getCurations();
		if (!ObjectUtils.isEmpty(cached)) {
			return cached.stream().map(CurationResult::new).toList();
		}

		List<Curation> curations = curationRepository.findExposed();

		curationCacheRepository.setCurations(
			curations.stream().map(curationDomainService::createCurationCache).toList()
		);

		return curations.stream().map(CurationResult::new).toList();
	}

	public CurationProductResult findCurationProduct(Long curationId, Long cursorId, Integer size) {
		CurationProductCache cached = curationCacheRepository.getCurationProducts(curationId, cursorId, size);
		if (!ObjectUtils.isEmpty(cached)) {
			return new CurationProductResult(cached);
		}

		Curation curation = curationRepository.findById(curationId)
			.orElseThrow(() -> new EntityNotFoundException(Curation.class, curationId));

		CursorPage<CurationProduct> curationProductPage = curationProductRepository.findAll(curationId, cursorId, size);

		curationCacheRepository.setCurationProducts(
			curationId,
			cursorId,
			size,
			curationDomainService.createCurationProductCache(
				curation,
				curationProductPage.map(CurationProduct::getProduct)
			)
		);

		return new CurationProductResult(curation, curationProductPage.map(CurationProduct::getProduct));
	}
}
