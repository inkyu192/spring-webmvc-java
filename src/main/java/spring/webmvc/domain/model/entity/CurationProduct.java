package spring.webmvc.domain.model.entity;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
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
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curation_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Curation curation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Product product;

	private long sortOrder;

	public static CurationProduct create(Curation curation, Product product, long sortOrder) {
		CurationProduct curationProduct = new CurationProduct();

		curationProduct.curation = curation;
		curationProduct.product = product;
		curationProduct.sortOrder = sortOrder;

		return curationProduct;
	}
}
