package spring.webmvc.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.entity.CurationProduct;

public interface CurationProductRepository {
	Page<CurationProduct> findAllByCurationId(Pageable pageable, Long curationId);
}
