package spring.webmvc.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationPlacement;

public interface CurationJpaRepository extends JpaRepository<Curation, Long> {
	List<Curation> findByPlacementAndIsExposedIsTrueOrderBySortOrder(CurationPlacement placement);
}
