package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Translation;
import spring.webmvc.domain.repository.TranslationRepository;
import spring.webmvc.infrastructure.persistence.jpa.TranslationJpaRepository;

@Component
@RequiredArgsConstructor
public class TranslationRepositoryAdapter implements TranslationRepository {

	private final TranslationJpaRepository translationJpaRepository;

	@Override
	public List<Translation> findAll() {
		return translationJpaRepository.findAll();
	}
}
