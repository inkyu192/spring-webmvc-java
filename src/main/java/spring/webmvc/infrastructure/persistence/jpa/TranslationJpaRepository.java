package spring.webmvc.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Translation;

public interface TranslationJpaRepository extends JpaRepository<Translation, Long> {
}
