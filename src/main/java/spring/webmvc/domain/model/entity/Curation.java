package spring.webmvc.domain.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.enums.CurationCategory;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curation extends BaseCreator {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;

	@Enumerated(EnumType.STRING)
	private CurationCategory category;

	private Boolean isExposed;
	private Long sortOrder;

	@OneToMany(mappedBy = "curation", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<CurationProduct> curationProducts = new ArrayList<>();

	public List<CurationProduct> getCurationProducts() {
		return Collections.unmodifiableList(curationProducts);
	}

	public static Curation create(
		String title,
		CurationCategory category,
		Boolean isExposed,
		Long sortOrder
	) {
		Curation curation = new Curation();

		curation.title = title;
		curation.category = category;
		curation.isExposed = isExposed;
		curation.sortOrder = sortOrder;

		return curation;
	}

	public void addProduct(Product product, Long sortOrder) {
		CurationProduct curationProduct = CurationProduct.create(this, product, sortOrder);

		curationProducts.add(curationProduct);
	}
}
