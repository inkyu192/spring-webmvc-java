package spring.webmvc.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import spring.webmvc.domain.model.entity.Curation;

public interface CurationJpaRepository extends JpaRepository<Curation, Long> {

	@Query("""
		select c
		from Curation c
		where c.isExposed = true
		order by c.sortOrder
		""")
	List<Curation> findExposed();
}
