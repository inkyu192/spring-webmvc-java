package spring.webmvc.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Accommodation;

public interface AccommodationJpaRepository extends JpaRepository<Accommodation, Long> {
}