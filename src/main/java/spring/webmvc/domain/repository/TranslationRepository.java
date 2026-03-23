package spring.webmvc.domain.repository;

import java.util.List;

import spring.webmvc.domain.model.entity.Translation;

public interface TranslationRepository {
	List<Translation> findAll();
}
