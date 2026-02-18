package spring.webmvc.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurationProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curation_id")
	private Curation curation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	private Long sortOrder;

	public static CurationProduct create(Curation curation, Product product, Long sortOrder) {
		CurationProduct curationProduct = new CurationProduct();

		curationProduct.curation = curation;
		curationProduct.product = product;
		curationProduct.sortOrder = sortOrder;

		return curationProduct;
	}
}
